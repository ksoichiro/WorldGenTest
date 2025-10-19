# Research: カスタムディメンション & ポータルシステム

**Feature**: カスタムディメンション追加
**Branch**: `003-mod`
**Date**: 2025-10-19

## 調査概要

Minecraft 1.21.1でカスタムディメンションとポータルシステムを実装するための技術調査。Architecturyフレームワークを使用したマルチプラットフォーム対応（Fabric + NeoForge）を前提とする。

---

## 決定事項（Decisions）

### D1: ディメンション実装方式 - データドリブン方式を採用

**決定**: Minecraft 1.21.1の完全データドリブンディメンションシステムを活用

**理由**:
- Minecraft 1.16以降、ディメンションは完全にJSONで定義可能
- Javaコードでの登録は不要（テレポート機能を除く）
- プラットフォーム間の完全な互換性
- メンテナンスが容易

**実装方法**:
```
common/src/main/resources/data/worldgentest/
├── dimension_type/crystal_dimension.json  # ディメンション特性
├── dimension/crystal_dimension.json       # Level Stem（生成器統合）
└── worldgen/biome/*.json                  # カスタムバイオーム定義
```

### D2: ポータルフレーム検出方式 - イベント駆動検出を採用

**決定**: ブロック使用（UseOnContext）イベントでフレーム検出をトリガー

**理由**:
- CPU負荷が低い（常時スキャン不要）
- プレイヤーのアクション（着火アイテム使用）と同期
- バニラのネザーポータル検出パターンと一致

**実装パターン**:
```java
// Architectury InteractionEvent
InteractionEvent.RIGHT_CLICK_BLOCK.register((player, hand, pos, face) -> {
    if (isPortalIgnitionItem(player.getItemInHand(hand)) &&
        isFrameBlock(world.getBlockState(pos))) {
        PortalFrame frame = detectPortalFrame(world, pos);
        if (frame.isValid()) {
            activatePortal(world, frame);
            return InteractionResult.SUCCESS;
        }
    }
    return InteractionResult.PASS;
});
```

### D3: ポータルフレーム構造 - コーナー任意の矩形フレーム

**決定**: バニラのネザーポータルと同様、コーナーブロックは任意（optional）

**理由**:
- プレイヤーの利便性向上（最小ブロック数でポータル作成可能）
- バニラの挙動と一致（学習コスト低減）
- フレームの辺のみ必須、コーナーは省略可能

**検証ロジック**:
```pseudocode
validateFrameEdgesFlexible(bottomLeft, width, height, frameBlockType):
    for each edge position (excluding corners):
        if !isFrameBlock(position):
            return false
    // コーナーは検証しない
    return true
```

### D4: バイオーム統合 - Terrablender Regionパターンを活用

**決定**: 既存のTerrablender統合パターンを再利用（Crystalline Cavesバイオームと同じ実装方法）

**理由**:
- プロジェクト内で実績あり（地下バイオーム統合成功）
- プラットフォーム固有mapping差異の解決パターン確立済み
- カスタムワールドプリセット不要（巨大JSONファイル回避）

**実装ファイル構成**:
```
common/src/main/java/.../biome/
└── CrystalDimensionRegion.java  # NeoForge用（Mojang mapping）

fabric/src/main/java/.../biome/
└── FabricCrystalDimensionRegion.java  # Fabric用（Yarn mapping）

neoforge/src/main/java/.../biome/
└── NeoForgeCrystalDimensionRegion.java  # 初期化（commonのRegion再利用）
```

### D5: テレポート実装 - @ExpectPlatformパターンを使用

**決定**: Architecturyの`@ExpectPlatform`でテレポート処理をプラットフォーム分離

**理由**:
- Fabric: `FabricDimensions.teleport()` 必須
- NeoForge: `player.teleportTo()` 使用
- API差異が大きいため、commonでの統一困難

**実装パターン**:
```java
// common: インターフェース定義
@ExpectPlatform
public static void teleportToDimension(ServerPlayer player, ResourceKey<Level> dimension) {
    throw new AssertionError();
}

// fabric: Fabric API実装
public static void teleportToDimension(ServerPlayer player, ResourceKey<Level> dimension) {
    ServerLevel level = player.getServer().getLevel(dimension);
    FabricDimensions.teleport(player, level, new TeleportTarget(...));
}

// neoforge: バニラAPI実装
public static void teleportToDimension(ServerPlayer player, ResourceKey<Level> dimension) {
    ServerLevel level = player.getServer().getLevel(dimension);
    player.teleportTo(level, x, y, z, yaw, pitch);
}
```

---

## 技術詳細（Technical Details）

### 1. Dimension Type JSON構造（Minecraft 1.21.1）

**ファイルパス**: `data/worldgentest/dimension_type/crystal_dimension.json`

**カスタムディメンション推奨設定**:
```json
{
  "ultrawarm": false,
  "natural": true,
  "piglin_safe": false,
  "respawn_anchor_works": false,
  "bed_works": true,
  "has_raids": true,
  "has_skylight": true,
  "has_ceiling": false,
  "coordinate_scale": 1.0,
  "ambient_light": 0.0,
  "logical_height": 256,
  "effects": "minecraft:overworld",
  "infiniburn": "#minecraft:infiniburn_overworld",
  "min_y": -64,
  "height": 384,
  "monster_spawn_light_level": {
    "type": "minecraft:uniform",
    "min_inclusive": 0,
    "max_inclusive": 7
  },
  "monster_spawn_block_light_limit": 0,
  "fixed_time": 6000
}
```

**主要フィールド説明**:

| フィールド | 値 | 説明 |
|-----------|---|------|
| `fixed_time` | 6000 | 固定時刻（6000=正午、常に昼間） |
| `effects` | `"minecraft:overworld"` | 視覚効果（空、雲、霧） |
| `coordinate_scale` | 1.0 | 座標スケール（1.0=オーバーワールドと同じ、ネザーは8.0） |
| `bed_works` | true | ベッドでリスポーン地点設定可能 |
| `has_skylight` | true | 空からの光あり |
| `ambient_light` | 0.0 | 環境光なし（完全に光レベル依存） |

**紫がかった空の実現方法**:
- `effects`フィールドではカスタム色設定不可
- カスタム空色はリソースパックまたはクライアントMODで実現
- 代替案: バイオームの`sky_color`設定（紫系の色コード: `0x8E44AD`）

### 2. Level Stem JSON構造

**ファイルパス**: `data/worldgentest/dimension/crystal_dimension.json`

**Multi-Noise Biome Source with Custom Biomes**:
```json
{
  "type": "worldgentest:crystal_dimension",
  "generator": {
    "type": "minecraft:noise",
    "settings": "minecraft:overworld",
    "biome_source": {
      "type": "minecraft:multi_noise",
      "biomes": [
        {
          "biome": "worldgentest:crystal_plains",
          "parameters": {
            "temperature": [-0.5, 0.5],
            "humidity": [-0.5, 0.5],
            "continentalness": [0.0, 1.0],
            "erosion": [-1.0, 1.0],
            "depth": [0.0, 0.3],
            "weirdness": [-1.0, 1.0],
            "offset": 0.0
          }
        },
        {
          "biome": "worldgentest:crystal_forest",
          "parameters": {
            "temperature": [-0.5, 0.5],
            "humidity": [0.3, 1.0],
            "continentalness": [0.0, 1.0],
            "erosion": [-1.0, 1.0],
            "depth": [0.0, 0.3],
            "weirdness": [-1.0, 1.0],
            "offset": 0.0
          }
        },
        {
          "biome": "worldgentest:crystal_desert",
          "parameters": {
            "temperature": [0.5, 1.0],
            "humidity": [-1.0, -0.5],
            "continentalness": [0.0, 1.0],
            "erosion": [-1.0, 1.0],
            "depth": [0.0, 0.3],
            "weirdness": [-1.0, 1.0],
            "offset": 0.0
          }
        },
        {
          "biome": "worldgentest:crystal_river",
          "parameters": {
            "temperature": [-0.5, 0.5],
            "humidity": [-0.5, 0.5],
            "continentalness": [-0.5, 0.0],
            "erosion": [-1.0, 1.0],
            "depth": [0.0, 0.2],
            "weirdness": [-1.0, 1.0],
            "offset": 0.0
          }
        }
      ]
    }
  }
}
```

**生成割合の調整方法**:
- パラメータ範囲の重なりで相対的な生成頻度が決まる
- 草原40%、森林30%、砂漠20%、川10%の比率を実現するには、パラメータ範囲の幅を調整
- Terrablender使用時は、各バイオームのweight設定で制御可能

### 3. ポータルフレーム検出アルゴリズム

**バニラの`PortalShape`クラス参考パターン**:

**ステップ1: 左下コーナー検出**
```java
private BlockPos findBottomLeftCorner(Level world, BlockPos startPos, Block frameBlock) {
    BlockPos current = startPos;

    // 下方向へスキャン
    while (world.getBlockState(current.below()).is(frameBlock)) {
        current = current.below();
    }

    // 左方向へスキャン（X軸またはZ軸）
    while (world.getBlockState(current.west()).is(frameBlock)) {
        current = current.west();
    }

    return current;
}
```

**ステップ2: フレーム検証**
```java
private boolean validateFrame(Level world, BlockPos bottomLeft, int width, int height, Block frameBlock) {
    // 下辺
    for (int x = 0; x < width; x++) {
        if (!world.getBlockState(bottomLeft.offset(x, 0, 0)).is(frameBlock)) {
            return false;
        }
    }

    // 上辺
    for (int x = 0; x < width; x++) {
        if (!world.getBlockState(bottomLeft.offset(x, height - 1, 0)).is(frameBlock)) {
            return false;
        }
    }

    // 左辺（コーナー除く）
    for (int y = 1; y < height - 1; y++) {
        if (!world.getBlockState(bottomLeft.offset(0, y, 0)).is(frameBlock)) {
            return false;
        }
    }

    // 右辺（コーナー除く）
    for (int y = 1; y < height - 1; y++) {
        if (!world.getBlockState(bottomLeft.offset(width - 1, y, 0)).is(frameBlock)) {
            return false;
        }
    }

    return true;
}
```

**ステップ3: 内部空間検証**
```java
private boolean validateInterior(Level world, BlockPos bottomLeft, int width, int height) {
    for (int x = 1; x < width - 1; x++) {
        for (int y = 1; y < height - 1; y++) {
            BlockState state = world.getBlockState(bottomLeft.offset(x, y, 0));
            if (!state.isAir() && !state.is(ModBlocks.CRYSTAL_PORTAL)) {
                return false;  // 内部が空気またはポータルブロックでない
            }
        }
    }
    return true;
}
```

**パフォーマンス最適化**:
- イベント駆動（UseOnContext）でのみ検証実行
- キャッシュ不要（ポータルは静的構造）
- 最大21×21でも検証時間<1ms

### 4. ポータルブロック管理

**ポータルアクティベーション**:
```java
private void activatePortal(Level world, PortalFrame frame) {
    // 内部をポータルブロックで充填
    Direction.Axis axis = frame.getAxis();  // X or Z

    for (int x = 1; x < frame.width - 1; x++) {
        for (int y = 1; y < frame.height - 1; y++) {
            BlockPos pos = frame.bottomLeft.offset(x, y, 0);
            BlockState portalState = ModBlocks.CRYSTAL_PORTAL.defaultBlockState()
                .setValue(CrystalPortalBlock.AXIS, axis);
            world.setBlock(pos, portalState, Block.UPDATE_ALL);
        }
    }

    // サウンド・パーティクル効果
    world.playSound(null, frame.bottomLeft, SoundEvents.PORTAL_TRIGGER,
                    SoundSource.BLOCKS, 1.0F, 1.0F);
    // パーティクル: 紫色のパーティクル効果（FR-008要件）
}
```

**フレーム破壊時のポータル削除**:
```java
public class CrystalPortalBlock extends Block {
    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos,
                               Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);

        if (!isValidPortalFrame(world, pos)) {
            // BFSで連結ポータルブロックを全削除
            destroyConnectedPortal(world, pos);
        }
    }

    private void destroyConnectedPortal(Level world, BlockPos startPos) {
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();

        queue.add(startPos);
        visited.add(startPos);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();

            if (world.getBlockState(current).is(this)) {
                world.removeBlock(current, false);

                for (Direction dir : Direction.values()) {
                    BlockPos neighbor = current.relative(dir);
                    if (!visited.contains(neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                    }
                }
            }
        }
    }
}
```

### 5. プラットフォーム固有実装差異

**イベント登録**:

**Fabric**:
```java
// FabricModInitializer
UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
    return handlePortalActivation(player, world, hand, hitResult);
});
```

**NeoForge**:
```java
// Event Handler class
@SubscribeEvent
public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
    handlePortalActivation(event.getEntity(), event.getLevel(),
                          event.getHand(), event.getHitVec());
}
```

**Architectury統一**:
```java
// Common module
InteractionEvent.RIGHT_CLICK_BLOCK.register((player, hand, pos, face) -> {
    return handlePortalActivation(player, player.level(), hand, pos);
});
```

**Mapping差異**:
- `World` (Yarn) ↔ `Level` (Mojang)
- `ActionResult` (Yarn) ↔ `InteractionResult` (Mojang)
- `neighborUpdate` (Yarn) ↔ `neighborChanged` (Mojang)

---

## 検討された代替案（Alternatives Considered）

### A1: カスタムワールドプリセット方式

**内容**: `multi_noise_biome_source_parameter_list`でバイオーム統合

**棄却理由**:
- 巨大JSONファイル（3.3MB、7593エントリー）
- メンテナンス不可能
- Terrablender経験から保守性の低さが実証済み

### A2: 常時スキャン方式のポータル検出

**内容**: ティックごとにワールドをスキャンしてフレーム検出

**棄却理由**:
- CPU負荷が高い
- マルチプレイヤーで問題
- イベント駆動で十分

### A3: ポータルブロックにBlockEntity使用

**内容**: 各ポータルブロックにBlockEntityを付与してフレーム情報を保存

**棄却理由**:
- メモリオーバーヘッド（大型ポータルで数百個のBlockEntity）
- 不要な複雑性
- BlockStateのみで十分

### A4: カスタム空色のためのクライアントMOD

**内容**: Fabricクライアント拡張で紫色の空を実現

**棄却理由**:
- サーバー・クライアント分離違反（サーバー側MODとして一貫性保持）
- バイオームの`sky_color`設定で代替可能
- プレイヤーのクライアント追加インストール負担

---

## リスクと緩和策（Risks & Mitigation）

### R1: バージョン互換性リスク

**リスク**: Minecraft 1.22以降でDimension API変更の可能性

**緩和策**:
- データドリブン方式なのでコード変更最小限
- 公式Wikiとリリースノート定期確認
- JSONバリデーターツール（misode.github.io）活用

### R2: プラットフォーム間挙動差異

**リスク**: Fabric/NeoForgeでポータル挙動が異なる

**緩和策**:
- Architecturyイベント抽象化の活用
- 両プラットフォームでの統合テスト必須
- プラットフォーム固有コードは最小限に抑制

### R3: パフォーマンス懸念（大型ポータル）

**リスク**: 21×21ポータルでフレーム検証・ブロック配置が遅延

**緩和策**:
- フレーム検証の最適化（早期リターン）
- ポータルブロック配置は1フレームで完了（<16ms）
- 最大サイズでもO(n²)=O(441)で許容範囲

### R4: マルチプレイヤー同期問題

**リスク**: ポータル状態がクライアント・サーバー間で不整合

**緩和策**:
- サーバー側でのみフレーム検証・ブロック配置
- `Block.UPDATE_ALL`フラグでクライアント同期
- NFR-003要件を満たすテスト実施

---

## ベストプラクティス（Best Practices）

### BP1: JSON定義の段階的検証

1. **最小構成から開始**: Fixed biome sourceで単一バイオーム
2. **動作確認後に拡張**: Multi-noise biome sourceに移行
3. **オンラインツール活用**: misode.github.io でJSON検証
4. **ワールド再作成必須**: `/reload`では変更反映されない

### BP2: Terrablender統合パターン

```java
// common: Mojang mapping
public class CrystalDimensionRegion extends Region {
    public void addBiomes(Registry<Biome> registry,
                         Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // バイオーム追加...
    }
}

// fabric: Yarn mapping
public class FabricCrystalDimensionRegion extends Region {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addBiomes(Registry registry, Consumer mapper) {
        // Yarn API使用...
    }
}
```

### BP3: ポータルブロックのBlockState設計

```java
public class CrystalPortalBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS =
        BlockStateProperties.HORIZONTAL_AXIS;

    // AXIS保存で方向を記録
    // これによりフレーム再検出不要
}
```

### BP4: エラーハンドリング

```java
private InteractionResult handlePortalActivation(...) {
    try {
        PortalFrame frame = detectPortalFrame(world, pos);
        if (frame != null && frame.isValid()) {
            activatePortal(world, frame);
            return InteractionResult.SUCCESS;
        }
    } catch (Exception e) {
        // NFR-005: エラーログ
        LOGGER.error("Portal activation failed at {}", pos, e);
    }
    return InteractionResult.PASS;
}
```

---

## 参考資料（References）

### 公式ドキュメント
- [Minecraft Wiki - Dimension type](https://minecraft.wiki/w/Dimension_type)
- [Minecraft Wiki - Custom dimension](https://minecraft.fandom.com/wiki/Custom_dimension)
- [Architectury Documentation](https://docs.architectury.dev/)
- [Fabric Wiki - Dimensions (1.16+)](https://fabricmc.net/wiki/tutorial:dimensions)
- [NeoForge Documentation](https://docs.neoforged.net/)

### オンラインツール
- [Misode's Dimension Type Generator](https://misode.github.io/dimension-type/) - 1.21対応
- [Misode's Dimension Generator](https://misode.github.io/dimension/)
- [Syldium Worldgen Generator](https://worldgen.syldium.dev/) - 1.21.4対応

### 実装例・ライブラリ
- [Custom Portal API (kyrptonaught)](https://github.com/kyrptonaught/customportalapi) - Fabric/NeoForge両対応
- [TelepathicGrunt/UltraAmplifiedDimension](https://github.com/TelepathicGrunt/UltraAmplifiedDimension-Fabric)
- [Architectury API](https://github.com/architectury/architectury-api)

### コミュニティ
- [Kaupenjoe's NeoForge Tutorial](https://github.com/Tutorials-By-Kaupenjoe/NeoForge-Tutorial-1.21.X)
- [Minecraft Server Optimization Guide](https://github.com/YouHaveTrouble/minecraft-optimization)

---

## 次のステップ（Next Steps）

研究完了後のPhase 1タスク:

1. **data-model.md生成**: エンティティ定義（PortalFrame、PortalBlock、Dimension等）
2. **contracts/生成**: JSON schemaファイル（dimension_type、biome等）
3. **quickstart.md生成**: 開発者向けセットアップ手順
4. **agent context更新**: `.specify/memory/claude-context.md`に技術スタック追加

**Phase 2（/speckit.tasks）**: 実装タスク分解とタスク依存関係定義
