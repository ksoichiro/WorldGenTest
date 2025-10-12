# Research: Crystalline Caves Biome World Generation

**Feature**: Crystalline Cavesバイオームのワールド生成統合
**Date**: 2025-10-08
**Status**: Complete

## 調査の目的

Minecraft 1.21.1で既存のCrystalline Cavesバイオームを通常のワールド生成に統合し、サバイバルモードで自然に遭遇できるようにする最適な実装方法を決定する。

## 主要な調査項目

1. バイオーム登録方法（データパック vs Javaコード）
2. Minecraft 1.21.1のワールド生成システム（multi-noise biome source）
3. 洞窟バイオームの生成メカニズム
4. Architecturyマルチプラットフォーム対応の考慮事項

---

## Decision 1: バイオーム登録方法

### 採用する方法
**データパック + プラットフォーム固有登録の併用方式**

### Rationale（選択理由）

#### データパック側での実装（メイン方式）
1. **Minecraft 1.21.1の標準アプローチ**
   - バニラの洞窟バイオーム（Lush Caves、Dripstone Caves）はmulti-noise biome sourceシステムで統合されている
   - 同じ方式を採用することでバニラとの互換性と一貫性を保証

2. **Architecturyとの優れた互換性**
   - `common/src/main/resources/data/`のデータパックファイルが自動的にFabric/NeoForge両方に共有される
   - プラットフォーム固有コードを最小限に抑えられる

3. **メンテナンス性**
   - バイオーム生成ロジックがJSONで管理され、コード変更なしでパラメータ調整可能
   - デバッグが容易（JSONの読み取りのみでバイオーム配置を理解できる）
   - Modユーザーがデータパックで出現頻度をカスタマイズ可能

#### プラットフォーム固有コードの役割
現在の実装を維持：
- **NeoForge**: `DeferredRegister`でバイオームをコード登録（`NeoForgeBiomes.java`）
- **Fabric**: `RegistryKey`のみ定義、データパックによる登録を想定（`FabricBiomes.java`）

この組み合わせにより、バイオーム定義自体はコードで管理しつつ、ワールド生成統合はデータパックで制御できる。

### Alternatives Considered（検討した代替案）

#### 代替案1: Javaコードのみでの実装
**NeoForge**: Biome Modifierを使用
- ❌ **問題点**: 「Biome modifiers CANNOT add noise caves to biomes」（公式ドキュメント）
- ❌ **理由**: Noise cavesはdimensionのnoise settings systemの一部であり、バイオームに直接紐付いていない
- ❌ Feature追加は可能だが、洞窟バイオームの生成配置自体は制御不可

**Fabric**: BiomeModifications APIを使用
- ⚠️ **制限**: Feature追加は可能だが、洞窟バイオームの実際の配置はmulti-noise parametersで制御される
- ⚠️ **追加依存**: CaveBiomeAPI（サードパーティMOD）が必要になる可能性
- ❌ Architecturyの原則（共通コード優先）から逸脱

#### 代替案2: カスタムディメンションの作成
- オーバーワールドではなく専用ディメンションにCrystalline Cavesを配置
- ❌ **不採用の理由**: 「バニラのワールド生成への統合」という要件から逸脱
- ❌ プレイヤーがポータル等を使わずに自然に遭遇できない

### Implementation Summary（実装方針）
1. **主要実装**: Multi-noise biome source parameter listをデータパックで作成
2. **補助実装**: バイオームタグ（`is_overworld.json`等）
3. **プラットフォーム固有コード**: 現状維持（変更不要）

---

## Decision 2: ワールド生成統合メカニズム

### 採用する方法
**Multi-Noise Biome Source Systemを使用した6次元パラメータ空間での配置**

### Rationale（選択理由）

#### Multi-Noise Biome Sourceの仕組み
Minecraft 1.21.1のバイオーム生成は6つのパラメータで制御：

1. **Temperature（温度）**: -1.0～1.0（寒冷～熱帯）
2. **Humidity（湿度）**: -1.0～1.0（乾燥～湿潤）
3. **Continentalness（大陸性）**: 海洋/海岸/内陸の区別
4. **Erosion（侵食）**: 平地/山岳の区別
5. **Weirdness（奇妙さ）**: バイオームバリアントの生成
6. **Depth（深度）**: 地表vs洞窟の区別 ⭐**最重要**

#### Crystalline Cavesに推奨するパラメータ設定

既存のバイオーム定義（`crystalline_caves.json`）の値を参考：
```json
{
  "temperature": 0.8,  // 温暖
  "downfall": 0.4      // 中程度の降水量
}
```

**推奨Multi-Noise Parameters**:
```json
{
  "parameters": {
    "temperature": [0.5, 1.0],      // 温暖な地域（既存定義と整合）
    "humidity": [0.2, 0.6],         // 中程度の湿度（Lush Cavesと差別化）
    "continentalness": [0.3, 0.9],  // 内陸寄り（Dripstoneとの差別化）
    "erosion": [-0.5, 0.5],         // 幅広い侵食レベル（多様な地形で出現）
    "depth": [0.2, 1.0],            // 洞窟深度（地表への漏出を防ぐ）
    "weirdness": [-0.3, 0.3],       // 標準的な地形
    "offset": 0.0                    // 標準的な出現頻度
  }
}
```

#### バニラ洞窟バイオームとの比較

| バイオーム | 主要パラメータ | 特徴的な範囲 |
|-----------|--------------|-------------|
| **Lush Caves** | Humidity | 高湿度（≥0.6） |
| **Dripstone Caves** | Continentalness | 高大陸性（≥0.8） |
| **Deep Dark** | Erosion | 低侵食（山岳地帯の地下） |
| **Crystalline Caves** | Temperature, Continentalness | 温暖＋中～高大陸性 |

この設定により、Crystalline Cavesは他の洞窟バイオームと適度に差別化されつつ、バニラと同程度の頻度で出現する。

### Implementation Details（実装詳細）

#### 1. Multi-Noise Parameter Listファイルの作成

**ファイルパス**: `/common/src/main/resources/data/worldgentest/worldgen/multi_noise_biome_source_parameter_list/overworld_caves.json`

**ファイル内容**:
```json
{
  "preset": "minecraft:overworld",
  "biomes": [
    {
      "biome": "worldgentest:crystalline_caves",
      "parameters": {
        "temperature": [0.5, 1.0],
        "humidity": [0.2, 0.6],
        "continentalness": [0.3, 0.9],
        "erosion": [-0.5, 0.5],
        "depth": [0.2, 1.0],
        "weirdness": [-0.3, 0.3],
        "offset": 0.0
      }
    }
  ]
}
```

**重要ポイント**:
- `preset: "minecraft:overworld"`: オーバーワールドの生成システムに統合
- `depth: [0.2, 1.0]`: 最小値0.2で地表への漏出を防止
- `offset: 0.0`: Lush Caves、Dripstone Cavesと同等の出現頻度

#### 2. Depth パラメータの理解

**Depthの計算**:
- 地表: depth ≈ 0
- 地下1ブロックごとに: +1/128 (0.0078125) ずつ増加
- Y=-64（基盤岩レベル）: depth ≈ 0.5

**Crystalline Cavesの深度範囲**:
- `depth: 0.2` → 約Y=38以下（地下26ブロック以深）
- `depth: 1.0` → すべての洞窟深度

この設定により、地表直下には出現せず、探索の価値がある適度な深さで発見される。

---

## Decision 3: バイオームタグとメタデータ

### 採用する方法
**Minecraftの標準的なバイオームタグシステムを使用**

### Rationale（選択理由）

バイオームタグは以下の目的で使用される：
1. オーバーワールドのバイオームとして認識
2. 構造物生成（廃坑、要塞等）の対象とする
3. Mobスポーンルールの適用

### Implementation Details（実装詳細）

#### 必須タグ1: オーバーワールド登録

**ファイルパス**: `/common/src/main/resources/data/minecraft/tags/worldgen/biome/is_overworld.json`

```json
{
  "replace": false,
  "values": [
    "worldgentest:crystalline_caves"
  ]
}
```

**目的**: Crystalline Cavesをオーバーワールドのバイオームとして登録し、ワールド生成システム、構造物、Mobスポーンで認識されるようにする。

#### 推奨タグ2: 廃坑生成

**ファイルパス**: `/common/src/main/resources/data/worldgentest/tags/worldgen/biome/has_structure/mineshaft.json`

```json
{
  "replace": false,
  "values": [
    "worldgentest:crystalline_caves"
  ]
}
```

**目的**: 洞窟バイオームとして適切な構造物（廃坑）を生成し、探索の楽しさを向上させる。

#### その他の検討タグ

- `has_structure/stronghold`: 要塞の生成（オプション）
- `has_structure/ancient_city`: 古代都市の生成（Deep Dark相当、オプション）

### 出現頻度の制御

**Offsetパラメータによる調整**:
- `offset: 0.0`: 標準的な出現頻度（**推奨**）
- `offset: -0.2`: やや稀（探索の価値を高める）
- `offset: -0.5`: Deep Dark相当（非常に稀）
- `offset: 0.5`: より頻繁（テスト用）

**推奨設定**: `offset: 0.0～-0.2`
- バニラの洞窟バイオームと同程度の頻度
- プレイヤーが自然に遭遇できる
- 探索の価値を損なわない

---

## Decision 4: プラットフォーム固有の実装

### 採用する方法
**現在のプラットフォーム固有コードを維持、データパックで統合**

### Rationale（選択理由）

#### 重要な発見
両プラットフォーム（Fabric/NeoForge）とも、**洞窟バイオームの実際の配置はデータパックのmulti-noise parametersでのみ制御可能**。

プラットフォーム固有のAPIは以下にのみ使用可能：
- ✅ 追加のFeature配置
- ✅ Mobスポーン設定の調整
- ❌ バイオーム自体のワールドへの出現制御

### Architectury共通コードで実装できる範囲

#### ✅ 完全に共通化可能
1. **バイオーム定義JSON**: `crystalline_caves.json`（既存）
2. **Multi-noise parameters**: `overworld_caves.json`（新規作成）
3. **バイオームタグ**: `is_overworld.json`等（新規作成）
4. **Feature/PlacedFeature**: 既に実装済み

#### ⚠️ プラットフォーム固有コード

##### Fabric (`FabricBiomes.java`)
```java
// 現状維持 - データパック主導の登録
public static final RegistryKey<Biome> CRYSTALLINE_CAVES = RegistryKey.of(
    RegistryKeys.BIOME,
    Identifier.of("worldgentest", "crystalline_caves")
);
```
- **役割**: データパックで登録されるバイオームへの参照
- **変更不要**: 現在の実装で十分

##### NeoForge (`NeoForgeBiomes.java`)
```java
// 現状維持 - コード主導の登録
public static final Supplier<Biome> CRYSTALLINE_CAVES = BIOMES.register(
    "crystalline_caves",
    () -> ModBiomes.createCrystallineCaves()
);
```
- **役割**: DeferredRegisterによるバイオーム登録
- **変更不要**: データパックのJSONと並行して機能

### FabricとNeoForgeの違い

| 側面 | Fabric | NeoForge |
|------|--------|----------|
| **登録方式** | RegistryKey参照 | DeferredRegister |
| **バイオーム修正** | BiomeModifications API | Biome Modifier |
| **洞窟統合** | データパック必須 ⭐ | データパック必須 ⭐ |
| **動的変更** | より柔軟 | より制限的 |

**結論**: 両プラットフォームとも、今回の実装では**プラットフォーム固有コードの変更は不要**。すべてデータパックで完結する。

---

## 実装手順のサマリー

### Phase 1: Multi-Noise Parameter Listの作成
**優先度**: 最高
**ファイル**: `/common/src/main/resources/data/worldgentest/worldgen/multi_noise_biome_source_parameter_list/overworld_caves.json`

### Phase 2: バイオームタグの追加
**優先度**: 高
**ファイル**: `/common/src/main/resources/data/minecraft/tags/worldgen/biome/is_overworld.json`

### Phase 3: テストとチューニング
**優先度**: 中
- `/locatebiome worldgentest:crystalline_caves` コマンドでバイオーム検出をテスト
- 複数のシード値で出現頻度を検証
- `offset`パラメータを調整

### Phase 4: ドキュメント作成
**優先度**: 中
- `quickstart.md`に手動テスト手順を記載
- `data-model.md`にパラメータの詳細を記載

---

## 注意事項とベストプラクティス

### 1. バイオーム競合の回避
**問題**: 他の洞窟バイオームとパラメータ範囲が重複しすぎると、一方が出現しなくなる

**解決策**:
- Lush Caves（高Humidity ≥0.6）と差別化: Crystalline CavesはHumidity 0.2～0.6
- Dripstone Caves（高Continentalness ≥0.8）と差別化: Crystalline Cavesは0.3～0.9（部分的に重複）

### 2. Depth パラメータの重要性
**重要**: 最小値を0.2以上に設定して地表への漏出を防ぐ
- `depth: [0.0, 1.0]` ❌ 地表に出現してしまう
- `depth: [0.2, 1.0]` ✅ 地下のみに出現

### 3. 段階的なテスト戦略
1. **Creative Mode + /locatebiome**: バイオームが生成されているか確認
2. **複数のシード値**: 一貫性を検証（FR-007対応）
3. **他の洞窟バイオームとの境界**: 適切に切り替わるか確認

### 4. パフォーマンスの考慮
- Multi-noise parametersはバニラの標準的な方式であり、パフォーマンス影響は最小限
- 既存のFeature（`crystal_ore_placed`等）は既にテスト済み
- **FR-008対応**: Lush Caves、Dripstone Cavesと同等のパフォーマンス

---

## 参考リソース

### 公式ドキュメント
1. **Minecraft Wiki - World Generation**: https://minecraft.wiki/w/World_generation
2. **Minecraft Wiki - Noise Settings**: https://minecraft.wiki/w/Noise_settings
3. **Minecraft Wiki - Custom Biomes**: https://minecraft.wiki/w/Custom_biome

### ツール
4. **Misode's Biome Generator**: https://misode.github.io/worldgen/biome/
   - Multi-noise parametersのビジュアル編集・検証ツール
5. **Misode's Data Pack Generator**: https://misode.github.io/

### API ドキュメント
6. **Fabric BiomeModifications API**: https://maven.fabricmc.net/docs/fabric-api-0.116.0+1.21.1/
7. **NeoForge Biome Modifiers**: https://docs.neoforged.net/docs/1.21.1/worldgen/biomemodifier/
8. **Architectury Documentation**: https://docs.architectury.dev/

---

## まとめ

### 最終推奨アプローチ
1. **メイン実装**: Multi-noise biome source parameter list（データパック）
2. **補助実装**: バイオームタグ（`is_overworld.json`）
3. **プラットフォーム固有コード**: 変更不要

### このアプローチの利点
- ✅ Minecraft 1.21.1の標準的なワールド生成システムに完全準拠
- ✅ Architecturyのマルチプラットフォーム対応を維持
- ✅ バニラの洞窟バイオームと同じ実装方式
- ✅ メンテナンス性とデバッグ性が高い
- ✅ Modユーザーがデータパックでカスタマイズ可能
- ✅ 既存コードへの影響が最小限

### 実装工数見積もり
**合計: 4-6時間**
- Multi-noise parameters作成: 1-2時間
- バイオームタグ設定: 0.5-1時間
- テストとチューニング: 2-3時間

### すべての機能要件への対応

| 要件 | 対応方法 |
|------|---------|
| **FR-001**: 自然生成 | Multi-noise parameters |
| **FR-002**: 中～高頻度 | offset: 0.0～-0.2 |
| **FR-003**: サバイバルで発見可能 | 標準的なワールド生成システム統合 |
| **FR-004**: 既存特徴の維持 | 既存バイオーム定義を変更せず活用 |
| **FR-005**: シングル/マルチ対応 | データパックはすべての環境で動作 |
| **FR-006**: バニラ互換性 | Multi-noise biome source使用 |
| **FR-007**: シード値再現性 | Minecraftのワールド生成システムが保証 |
| **FR-008**: パフォーマンス | バニラと同じ方式でパフォーマンス同等 |

### 次のステップ
Phase 1（Design & Contracts）に進み、以下を作成：
1. `data-model.md`: パラメータとエンティティの詳細定義
2. `contracts/`: JSONスキーマファイル
3. `quickstart.md`: 手動テスト手順
4. `CLAUDE.md`: エージェントコンテキスト更新
