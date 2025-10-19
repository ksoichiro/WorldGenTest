# Data Model: カスタムディメンション & ポータルシステム

**Feature**: カスタムディメンション追加
**Branch**: `003-mod`
**Date**: 2025-10-19

## エンティティ概要

カスタムディメンションシステムの主要エンティティと、それらの関係性を定義する。

---

## エンティティ定義

### E1: PortalFrame（ポータルフレーム）

**説明**: クリスタルブロックで構築される矩形ポータルフレームの構造情報

**属性**:

| 属性名 | 型 | 必須 | 説明 | 制約 |
|-------|---|------|------|------|
| `bottomLeft` | `BlockPos` | ✓ | フレーム左下コーナー座標 | ワールド内の有効座標 |
| `width` | `int` | ✓ | フレーム幅（内側空間） | 2 ≤ width ≤ 21 |
| `height` | `int` | ✓ | フレーム高さ（内側空間） | 3 ≤ height ≤ 21 |
| `axis` | `Direction.Axis` | ✓ | ポータルの向き | `X` または `Z` |
| `isActive` | `boolean` | ✓ | ポータルの起動状態 | true=起動済み、false=未起動 |
| `frameBlockType` | `Block` | ✓ | フレームを構成するブロック | `ModBlocks.CRYSTAL_BLOCK` |

**検証ルール**:
- `width >= 2 && width <= 21`
- `height >= 3 && height <= 21`
- フレームの辺すべてが`frameBlockType`で構成されること（コーナーは任意）
- 内側空間が空気またはポータルブロックであること

**状態遷移**:
```
[未検出] --フレーム完成--> [検出済み（未起動）]
[検出済み（未起動）] --着火アイテム使用--> [アクティブ]
[アクティブ] --フレーム破壊--> [破壊]
```

**関連エンティティ**:
- `PortalBlock`: フレーム内部に生成されるポータルブロック（1対多）
- `CrystalBlock`: フレームを構成するブロック（多対1）

---

### E2: PortalBlock（ポータルブロック）

**説明**: 起動されたポータルフレーム内部に生成される透過ブロック。エンティティがこのブロック内に留まることでディメンション移動が発生。

**属性**:

| 属性名 | 型 | 必須 | 説明 | 制約 |
|-------|---|------|------|------|
| `position` | `BlockPos` | ✓ | ブロック座標 | ワールド内の有効座標 |
| `axis` | `Direction.Axis` | ✓ | ポータルの向き | `X` または `Z`（親フレームと一致） |
| `parentFrame` | `PortalFrame` | - | 所属するポータルフレーム | 論理的参照（実装時は再検出） |

**BlockState Properties**:
```java
public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
// AXIS: X または Z
```

**検証ルール**:
- 隣接する6方向のいずれかに`frameBlockType`が存在すること（フレームに所属）
- または、隣接ブロックが同じ`PortalBlock`であること（ポータル内部）

**状態遷移**:
```
[未生成] --フレーム起動--> [生成]
[生成] --フレーム破壊--> [削除]
[生成] --4秒間エンティティ滞在--> [テレポート発動]
```

**関連エンティティ**:
- `PortalFrame`: 親ポータルフレーム（多対1）
- `CrystalDimension`: テレポート先ディメンション（多対1）

---

### E3: CrystalDimension（カスタムディメンション）

**説明**: MODが追加する新しいディメンション。常に昼間、紫がかった空、カスタムバイオームのみで構成される。

**属性**:

| 属性名 | 型 | 必須 | 説明 | 制約 |
|-------|---|------|------|------|
| `dimensionKey` | `ResourceKey<Level>` | ✓ | ディメンション識別子 | `worldgentest:crystal_dimension` |
| `dimensionType` | `DimensionType` | ✓ | ディメンション特性 | JSONで定義 |
| `generator` | `ChunkGenerator` | ✓ | ワールド生成器 | Noise generator + Multi-noise biome source |
| `coordinateScale` | `double` | ✓ | 座標スケール | 1.0（オーバーワールド:ディメンション = 4:1） |
| `fixedTime` | `long` | ✓ | 固定時刻 | 6000（正午） |
| `skyColor` | `int` | ✓ | 空の色 | `0x8E44AD`（紫系） |

**DimensionType JSON定義** (`data/worldgentest/dimension_type/crystal_dimension.json`):
```json
{
  "ultrawarm": false,
  "natural": true,
  "coordinate_scale": 1.0,
  "has_skylight": true,
  "has_ceiling": false,
  "ambient_light": 0.0,
  "fixed_time": 6000,
  "piglin_safe": false,
  "bed_works": true,
  "respawn_anchor_works": false,
  "has_raids": true,
  "logical_height": 256,
  "min_y": -64,
  "height": 384,
  "infiniburn": "#minecraft:infiniburn_overworld",
  "effects": "minecraft:overworld",
  "monster_spawn_light_level": {
    "type": "minecraft:uniform",
    "min_inclusive": 0,
    "max_inclusive": 7
  },
  "monster_spawn_block_light_limit": 0
}
```

**Level Stem JSON定義** (`data/worldgentest/dimension/crystal_dimension.json`):
```json
{
  "type": "worldgentest:crystal_dimension",
  "generator": {
    "type": "minecraft:noise",
    "settings": "minecraft:overworld",
    "biome_source": {
      "type": "minecraft:multi_noise",
      "biomes": [
        { "biome": "worldgentest:crystal_plains", "parameters": {...} },
        { "biome": "worldgentest:crystal_forest", "parameters": {...} },
        { "biome": "worldgentest:crystal_desert", "parameters": {...} },
        { "biome": "worldgentest:crystal_river", "parameters": {...} }
      ]
    }
  }
}
```

**検証ルール**:
- `fixedTime`は0-24000の範囲（ただし6000固定）
- `height`は16の倍数かつ16-4064の範囲
- `logical_height <= height`
- バイオーム生成割合: Plains 40%, Forest 30%, Desert 20%, River 10%

**状態遷移**:
```
[未読み込み] --ゲーム起動--> [読み込み済み]
[読み込み済み] --プレイヤーテレポート--> [アクティブ]
[アクティブ] --プレイヤー全員退出--> [読み込み済み]
```

**関連エンティティ**:
- `CustomBiome`: ディメンション内のカスタムバイオーム（1対4）
- `PortalBlock`: ディメンションへのアクセス手段（多対多）

---

### E4: CustomBiome（カスタムバイオーム）

**説明**: カスタムディメンション内に生成される4種類の独自バイオーム

**サブタイプ**:

#### 4.1 Crystal Plains（クリスタルの草原）

**属性**:

| 属性名 | 型 | 必須 | 説明 | 値 |
|-------|---|------|------|---|
| `biomeKey` | `ResourceKey<Biome>` | ✓ | バイオーム識別子 | `worldgentest:crystal_plains` |
| `temperature` | `float` | ✓ | 温度 | 0.0（平均） |
| `downfall` | `float` | ✓ | 降水量 | 0.4 |
| `spawnWeight` | `int` | ✓ | 生成頻度 | 40（40%相当） |
| `surfaceBlock` | `Block` | ✓ | 地表ブロック | `ModBlocks.CRYSTAL_GRASS_BLOCK` |
| `underBlock` | `Block` | ✓ | 地下ブロック | `ModBlocks.CRYSTAL_DIRT` |

#### 4.2 Crystal Forest（クリスタルの森林）

**属性**:

| 属性名 | 型 | 必須 | 説明 | 値 |
|-------|---|------|------|---|
| `biomeKey` | `ResourceKey<Biome>` | ✓ | バイオーム識別子 | `worldgentest:crystal_forest` |
| `temperature` | `float` | ✓ | 温度 | 0.0 |
| `downfall` | `float` | ✓ | 降水量 | 0.8 |
| `spawnWeight` | `int` | ✓ | 生成頻度 | 30（30%相当） |
| `surfaceBlock` | `Block` | ✓ | 地表ブロック | `ModBlocks.CRYSTAL_GRASS_BLOCK` |
| `treeFeature` | `ConfiguredFeature` | ✓ | 樹木生成 | カスタム木（crystal_log使用） |

#### 4.3 Crystal Desert（クリスタルの砂漠）

**属性**:

| 属性名 | 型 | 必須 | 説明 | 値 |
|-------|---|------|------|---|
| `biomeKey` | `ResourceKey<Biome>` | ✓ | バイオーム識別子 | `worldgentest:crystal_desert` |
| `temperature` | `float` | ✓ | 温度 | 2.0（高温） |
| `downfall` | `float` | ✓ | 降水量 | 0.0（降雨なし） |
| `spawnWeight` | `int` | ✓ | 生成頻度 | 20（20%相当） |
| `surfaceBlock` | `Block` | ✓ | 地表ブロック | `ModBlocks.CRYSTAL_SAND` |

#### 4.4 Crystal River（クリスタルの川）

**属性**:

| 属性名 | 型 | 必須 | 説明 | 値 |
|-------|---|------|------|---|
| `biomeKey` | `ResourceKey<Biome>` | ✓ | バイオーム識別子 | `worldgentest:crystal_river` |
| `temperature` | `float` | ✓ | 温度 | 0.0 |
| `downfall` | `float` | ✓ | 降水量 | 0.5 |
| `spawnWeight` | `int` | ✓ | 生成頻度 | 10（10%相当） |
| `waterColor` | `int` | ✓ | 水色 | `0x3FA3D3`（水色っぽい薄い色） |

**検証ルール**:
- バイオーム生成割合の合計が100%であること（weight合計=100）
- `temperature`範囲: -2.0 ～ 2.0
- `downfall`範囲: 0.0 ～ 1.0

**状態遷移**:
```
[未生成] --ワールド生成--> [生成済み]
[生成済み] --プレイヤー探索--> [読み込み]
```

**関連エンティティ**:
- `CrystalDimension`: 親ディメンション（多対1）
- `CustomBlock`: バイオームを構成するブロック（多対多）

---

### E5: CustomBlock（カスタムブロック）

**説明**: カスタムディメンションを構成するMOD独自のブロック群。採掘特性はバニラの対応ブロックと同等。

**共通属性**:

| 属性名 | 型 | 必須 | 説明 | 制約 |
|-------|---|------|------|------|
| `blockId` | `ResourceLocation` | ✓ | ブロック識別子 | `worldgentest:<name>` |
| `hardness` | `float` | ✓ | ブロック硬度 | バニラ対応ブロックと同値 |
| `toolType` | `ToolType` | ✓ | 適正ツール | `SHOVEL`, `AXE`, etc. |
| `dropItem` | `Item` | ✓ | ドロップアイテム | 自分自身 |
| `textureSource` | `String` | ✓ | テクスチャ元 | バニラテクスチャ + 色調変更 |

**サブタイプ**:

#### 5.1 Crystal Grass Block

| 属性 | 値 |
|------|---|
| `blockId` | `worldgentest:crystal_grass_block` |
| `hardness` | 0.6（バニラ草ブロックと同じ） |
| `toolType` | `SHOVEL` |
| `dropItem` | `worldgentest:crystal_grass_block` |
| `textureBase` | `minecraft:textures/block/grass_block_*.png` |

#### 5.2 Crystal Dirt Block

| 属性 | 値 |
|------|---|
| `blockId` | `worldgentest:crystal_dirt` |
| `hardness` | 0.5 |
| `toolType` | `SHOVEL` |
| `dropItem` | `worldgentest:crystal_dirt` |
| `textureBase` | `minecraft:textures/block/dirt.png` |

#### 5.3 Crystal Log Block

| 属性 | 値 |
|------|---|
| `blockId` | `worldgentest:crystal_log` |
| `hardness` | 2.0 |
| `toolType` | `AXE` |
| `dropItem` | `worldgentest:crystal_log` |
| `textureBase` | `minecraft:textures/block/oak_log*.png` |

#### 5.4 Crystal Sand Block

| 属性 | 値 |
|------|---|
| `blockId` | `worldgentest:crystal_sand` |
| `hardness` | 0.5 |
| `toolType` | `SHOVEL` |
| `dropItem` | `worldgentest:crystal_sand` |
| `textureBase` | `minecraft:textures/block/sand.png` |

**検証ルール**:
- `hardness > 0`
- `dropItem`はブロック自身（シルクタッチ不要）
- テクスチャは色調変更のみ（構造変更なし）

**状態遷移**:
```
[未採掘] --プレイヤー採掘--> [破壊]
[破壊] --ドロップ--> [アイテム化]
```

**関連エンティティ**:
- `CustomBiome`: ブロックが生成されるバイオーム（多対多）
- `PortalFrame`: フレームブロックとして使用（`CrystalBlock`のみ、1対多）

---

### E6: PortalTeleportEvent（ポータルテレポートイベント）

**説明**: プレイヤーまたはエンティティがポータルを通過する際のイベントデータ

**属性**:

| 属性名 | 型 | 必須 | 説明 | 制約 |
|-------|---|------|------|------|
| `entity` | `Entity` | ✓ | テレポート対象エンティティ | プレイヤーまたはMob |
| `sourcePos` | `BlockPos` | ✓ | 出発地点座標 | ポータルブロック位置 |
| `sourceDimension` | `ResourceKey<Level>` | ✓ | 出発ディメンション | オーバーワールドまたはカスタムディメンション |
| `targetDimension` | `ResourceKey<Level>` | ✓ | 到着ディメンション | カスタムディメンションまたはオーバーワールド |
| `targetPos` | `BlockPos` | ✓ | 到着地点座標 | 座標変換済み |
| `dwellTime` | `int` | ✓ | ポータル内滞在時間（tick） | 80 tick = 4秒 |
| `coordinateScale` | `double` | ✓ | 座標スケール | 4.0（オーバーワールド→ディメンション）、0.25（逆方向） |

**検証ルール**:
- `dwellTime >= 80`（4秒 = 80 tick）
- `targetPos`は安全な位置（y=64基準、固体ブロックの上）
- `coordinateScale`は方向により異なる
  - オーバーワールド → カスタムディメンション: 0.25（4で除算）
  - カスタムディメンション → オーバーワールド: 4.0（4で乗算）

**状態遷移**:
```
[エンティティ接触] --0秒経過--> [滞在開始]
[滞在開始] --4秒経過--> [テレポート発動]
[テレポート発動] --座標変換--> [ディメンション移動]
[ディメンション移動] --ポータル生成--> [完了]
```

**関連エンティティ**:
- `PortalBlock`: テレポート発火点（多対1）
- `CrystalDimension`: テレポート先ディメンション（多対1）
- `PortalFrame`: 到着地点のポータルフレーム（自動生成または既存利用）

---

## エンティティ関係図（ER Diagram）

```
┌───────────────┐
│ CrystalBlock  │
│ (既存)        │
└───────┬───────┘
        │ 使用
        ▼
┌───────────────┐         ┌──────────────┐
│ PortalFrame   │◄──────┤ PortalBlock  │
│               │ 1    * │              │
│ - bottomLeft  │         │ - position   │
│ - width       │         │ - axis       │
│ - height      │         └──────┬───────┘
│ - axis        │                │ 通過
│ - isActive    │                ▼
└───────┬───────┘         ┌──────────────┐
        │ アクセス          │ Portal       │
        ▼                 │ Teleport     │
┌───────────────┐         │ Event        │
│ Crystal       │◄────────┤              │
│ Dimension     │         │ - entity     │
│               │         │ - sourcePos  │
│ - dimensionKey│         │ - targetPos  │
│ - fixedTime   │         │ - dwellTime  │
│ - skyColor    │         └──────────────┘
└───────┬───────┘
        │ 構成
        ▼
┌───────────────┐         ┌──────────────┐
│ CustomBiome   │◄──────┤ CustomBlock  │
│               │ *    * │              │
│ - Plains      │         │ - Grass      │
│ - Forest      │         │ - Dirt       │
│ - Desert      │         │ - Log        │
│ - River       │         │ - Sand       │
└───────────────┘         └──────────────┘
```

**関係の説明**:

- `PortalFrame` --使用--> `CrystalBlock`: フレームブロックとして使用（多対1）
- `PortalFrame` --生成--> `PortalBlock`: フレーム内部にポータルブロック生成（1対多）
- `PortalBlock` --発動--> `PortalTeleportEvent`: エンティティ滞在でイベント発生（多対多）
- `PortalTeleportEvent` --移動--> `CrystalDimension`: ディメンション間移動（多対1）
- `CrystalDimension` --構成--> `CustomBiome`: ディメンション内に4種バイオーム（1対4）
- `CustomBiome` --使用--> `CustomBlock`: バイオーム生成時にブロック使用（多対多）

---

## データ整合性ルール

### R1: ポータルフレーム整合性

**ルール**: アクティブなポータルフレームは、常に有効な矩形構造を維持しなければならない

**検証方法**:
```java
public boolean validateFrameIntegrity(PortalFrame frame) {
    for (BlockPos edgePos : frame.getEdgePositions()) {
        if (!world.getBlockState(edgePos).is(frame.frameBlockType)) {
            return false;  // フレーム破壊検出
        }
    }
    return true;
}
```

**違反時の処理**: フレーム破壊時、すべてのポータルブロックを削除（Flood Fillアルゴリズム）

### R2: ポータルブロック所属整合性

**ルール**: すべてのポータルブロックは、有効なポータルフレームに所属しなければならない

**検証方法**:
```java
@Override
public void neighborChanged(BlockState state, Level world, BlockPos pos, ...) {
    if (!isPartOfValidFrame(world, pos)) {
        world.removeBlock(pos, false);  // 孤立ポータルブロック削除
    }
}
```

### R3: 座標変換一貫性

**ルール**: オーバーワールド↔カスタムディメンション間の座標変換は、常に1:4のスケールを維持

**変換式**:
```
オーバーワールド → カスタムディメンション:
  targetX = sourceX / 4
  targetZ = sourceZ / 4
  targetY = 64 (基準高度)

カスタムディメンション → オーバーワールド:
  targetX = sourceX * 4
  targetZ = sourceZ * 4
  targetY = 64 (基準高度)
```

### R4: バイオーム生成割合一貫性

**ルール**: カスタムバイオームの生成割合は、Plains 40% + Forest 30% + Desert 20% + River 10% = 100%

**検証方法**:
- Terrablender Region定義でweightを設定
- 各バイオームのパラメータ範囲を適切に調整

### R5: ブロック採掘特性一貫性

**ルール**: カスタムブロックの採掘特性は、バニラの対応ブロックと同等でなければならない

**検証項目**:
- `hardness`: バニラと同値
- `requiresCorrectToolForDrops()`: 適正ツール要求
- ドロップアイテム: 自分自身（シルクタッチ不要）

---

## データ永続化

### Minecraft NBTデータ構造

**ディメンションデータ**: Minecraftのワールドセーブシステムで自動管理
- パス: `<world>/dimensions/worldgentest/crystal_dimension/`
- 形式: Region files (`.mca`) + Level data (`level.dat`)

**ポータル状態**: ブロックの配置情報で暗黙的に永続化
- フレーム: ワールド内のCrystalBlockの配置
- ポータルブロック: ワールド内のCrystalPortalBlockの配置
- 追加のNBTデータ不要（BlockStateのみ）

**プレイヤー位置**: 標準のプレイヤーデータで管理
- 現在ディメンション: `Dimension: "worldgentest:crystal_dimension"`
- 座標: `Pos: [x, y, z]`

---

## 次のステップ

data-model.md完了後のPhase 1残タスク:

1. **contracts/生成**: JSON schemaファイル
2. **quickstart.md生成**: 開発者向けセットアップ手順
3. **agent context更新**: `.specify/memory/claude-context.md`に技術スタック追加
