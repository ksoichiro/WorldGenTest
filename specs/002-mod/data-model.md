# Data Model: Crystalline Caves Biome World Generation

**Feature**: Crystalline Cavesバイオームのワールド生成統合
**Date**: 2025-10-08
**Status**: Complete

## 概要

このドキュメントでは、Crystalline Cavesバイオームをワールド生成に統合するために必要なデータ構造、パラメータ、エンティティを定義します。実装は主にMinecraftのデータパック（JSON）形式で行われます。

---

## Entity 1: Crystalline Caves Biome

### 説明
既に実装されているカスタム洞窟バイオーム。クリスタルベースのユニークな地形、専用ブロック、カスタム照明効果、特定のMobスポーンルールを持つ。

### 既存の実装

#### Javaコード（`ModBiomes.java`）
```java
public static Biome createCrystallineCaves()
```

#### JSONデータ（`crystalline_caves.json`）
```json
{
  "has_precipitation": false,
  "temperature": 0.8,
  "downfall": 0.4,
  "effects": { ... },
  "spawners": { ... },
  "carvers": { ... },
  "features": [ ... ]
}
```

### 主要な属性

| 属性 | 値 | 説明 |
|-----|---|------|
| `temperature` | 0.8 | 温暖な気候 |
| `downfall` | 0.4 | 中程度の降水量（湿度に相当） |
| `has_precipitation` | false | 洞窟バイオームのため降雨なし |
| `sky_color` | 7842815 | 空の色（洞窟では見えないが定義必要） |
| `water_color` | 4159204 | 水の色 |
| `fog_color` | 12638143 | 霧の色 |

### Features（装飾要素）
既に実装されている配置済みFeature：
- `worldgentest:crystal_block_placed`: クリスタルブロック生成
- `worldgentest:crystal_ore_placed`: クリスタル鉱石（通常）
- `worldgentest:deepslate_crystal_ore_placed`: クリスタル鉱石（深層岩）
- `worldgentest:crystal_stone_ore_placed`: クリスタル石鉱石
- `worldgentest:crystal_decorations_placed`: クリスタル装飾（鍾乳石・苔）
- `worldgentest:crystal_shrine_placed`: クリスタル神殿
- `worldgentest:ancient_ruins_placed`: 古代遺跡

### Mob Spawners
既に実装されているMobスポーン設定：
- **Monster**: Zombie, Skeleton, Creeper, Slime, Enderman, Witch
- **Ambient**: Bat
- **Creature**: Crystal Golem（カスタムMob）

### 変更の必要性
**なし** - 既存のバイオーム定義は完全で、ワールド生成統合のために変更不要。

---

## Entity 2: Multi-Noise Biome Source Parameters

### 説明
Minecraft 1.21.1のワールド生成システムで、バイオームをどこに配置するかを決定する6次元パラメータ空間。

### データ構造

#### ファイルパス
`/common/src/main/resources/data/worldgentest/worldgen/multi_noise_biome_source_parameter_list/overworld_caves.json`

#### JSONスキーマ
```json
{
  "preset": "minecraft:overworld",
  "biomes": [
    {
      "biome": "worldgentest:crystalline_caves",
      "parameters": {
        "temperature": [min, max],
        "humidity": [min, max],
        "continentalness": [min, max],
        "erosion": [min, max],
        "depth": [min, max],
        "weirdness": [min, max],
        "offset": number
      }
    }
  ]
}
```

### パラメータ詳細

#### 1. Temperature（温度）
- **型**: Float範囲 `[min, max]`
- **有効範囲**: -1.0（寒冷）～ 1.0（熱帯）
- **Crystalline Caves設定**: `[0.5, 1.0]`
- **理由**: 既存バイオーム定義の`temperature: 0.8`と整合。温暖な地域で出現。

#### 2. Humidity（湿度）
- **型**: Float範囲 `[min, max]`
- **有効範囲**: -1.0（乾燥）～ 1.0（湿潤）
- **Crystalline Caves設定**: `[0.2, 0.6]`
- **理由**: 既存バイオーム定義の`downfall: 0.4`に対応。Lush Caves（≥0.6）と差別化。

#### 3. Continentalness（大陸性）
- **型**: Float範囲 `[min, max]`
- **有効範囲**: -1.0（海洋）～ 1.0（内陸）
- **Crystalline Caves設定**: `[0.3, 0.9]`
- **理由**: 内陸寄りで出現。Dripstone Caves（≥0.8）と部分的に重複しつつ差別化。

#### 4. Erosion（侵食）
- **型**: Float範囲 `[min, max]`
- **有効範囲**: -1.0（山岳）～ 1.0（平地）
- **Crystalline Caves設定**: `[-0.5, 0.5]`
- **理由**: 幅広い侵食レベルで出現。多様な地形で発見可能。

#### 5. Depth（深度）⭐ 最重要
- **型**: Float範囲 `[min, max]`
- **有効範囲**: 0.0（地表）～ 1.0（深層）
- **Crystalline Caves設定**: `[0.2, 1.0]`
- **理由**: 最小値0.2で地表への漏出を防止。地下探索で発見される。

**Depth計算式**:
```
depth = 地下ブロック数 / 128
```

**具体例**:
- Y=64（地表）: depth ≈ 0.0
- Y=38（地下26ブロック）: depth ≈ 0.2 ⬅️ Crystalline Caves最小深度
- Y=0（基盤岩付近）: depth ≈ 0.5
- Y=-64（基盤岩レベル）: depth ≈ 1.0

#### 6. Weirdness（奇妙さ）
- **型**: Float範囲 `[min, max]`
- **有効範囲**: -1.0～ 1.0
- **Crystalline Caves設定**: `[-0.3, 0.3]`
- **理由**: 標準的な地形で出現。極端なバリアント地形を避ける。

#### 7. Offset（出現頻度補正）
- **型**: Float
- **有効範囲**: -∞ ～ +∞（実用的には-1.0～1.0）
- **Crystalline Caves設定**: `0.0`
- **効果**:
  - `0.0`: 標準的な出現頻度（Lush Caves、Dripstone Cavesと同等）
  - `-0.2`: やや稀（推奨代替値）
  - `-0.5`: 非常に稀（Deep Dark相当）
  - `+0.5`: より頻繁（テスト用）

### パラメータ範囲の選定根拠

| パラメータ | バニラバイオーム比較 | Crystalline Caves設定 | 差別化の理由 |
|-----------|------------------|---------------------|-------------|
| Temperature | Lush: -1.0～1.0 | 0.5～1.0 | 温暖地域に限定 |
| Humidity | Lush: 0.6～1.0 | 0.2～0.6 | Lushより乾燥 |
| Continentalness | Dripstone: 0.8～1.0 | 0.3～0.9 | より広範囲に出現 |
| Erosion | Deep Dark: -1.0～-0.5 | -0.5～0.5 | 多様な地形 |
| Depth | Lush/Dripstone: 0.0～1.0 | 0.2～1.0 | 地表を避ける |
| Weirdness | 標準 | -0.3～0.3 | 標準的な地形 |

---

## Entity 3: Biome Tags

### 説明
Minecraftのタグシステムで、バイオームをグループ化し、ワールド生成システム、構造物、Mobスポーンルールで参照する。

### Tag 1: is_overworld

#### ファイルパス
`/common/src/main/resources/data/minecraft/tags/worldgen/biome/is_overworld.json`

#### JSONスキーマ
```json
{
  "replace": false,
  "values": [
    "worldgentest:crystalline_caves"
  ]
}
```

#### 属性

| フィールド | 値 | 説明 |
|----------|---|------|
| `replace` | false | 既存のバニラタグに追加（上書きしない） |
| `values` | 配列 | このタグに含めるバイオームのリスト |

#### 目的
- Crystalline Cavesをオーバーワールドのバイオームとして登録
- ワールド生成システムで認識される
- 構造物生成（要塞、廃坑等）の対象となる
- Mobスポーンルールが適用される

#### 必須度
**必須** - このタグがないとバイオームがワールドに生成されない可能性がある。

### Tag 2: has_structure/mineshaft（推奨）

#### ファイルパス
`/common/src/main/resources/data/worldgentest/tags/worldgen/biome/has_structure/mineshaft.json`

#### JSONスキーマ
```json
{
  "replace": false,
  "values": [
    "worldgentest:crystalline_caves"
  ]
}
```

#### 目的
- 洞窟バイオームとして適切な構造物（廃坑）を生成
- 探索の楽しさとリソース獲得の機会を提供

#### 必須度
**推奨** - バイオームの魅力を高めるが、機能要件の達成には必須ではない。

### その他の検討タグ

| タグ | ファイル名 | 目的 | 必須度 |
|-----|----------|------|--------|
| `has_structure/stronghold` | `stronghold.json` | 要塞の生成 | オプション |
| `has_structure/ancient_city` | `ancient_city.json` | 古代都市の生成（Deep Dark相当） | オプション |

---

## Entity 4: Biome Registry Entry

### 説明
Minecraft内部でバイオームを識別するためのレジストリエントリ。プラットフォーム固有コードで定義される。

### Fabric実装（`FabricBiomes.java`）

```java
public static final RegistryKey<Biome> CRYSTALLINE_CAVES = RegistryKey.of(
    RegistryKeys.BIOME,
    Identifier.of("worldgentest", "crystalline_caves")
);
```

#### 属性
- **型**: `RegistryKey<Biome>`
- **Namespace**: `worldgentest`
- **Path**: `crystalline_caves`
- **完全なID**: `worldgentest:crystalline_caves`

#### 役割
- データパックで登録されるバイオームへの参照を提供
- Fabricの動的レジストリシステムで使用

### NeoForge実装（`NeoForgeBiomes.java`）

```java
public static final Supplier<Biome> CRYSTALLINE_CAVES = BIOMES.register(
    "crystalline_caves",
    () -> ModBiomes.createCrystallineCaves()
);
```

#### 属性
- **型**: `Supplier<Biome>`（DeferredRegister）
- **登録名**: `crystalline_caves`
- **完全なID**: `worldgentest:crystalline_caves`

#### 役割
- バイオームをNeoForgeのレジストリシステムに登録
- `ModBiomes.createCrystallineCaves()`を呼び出してバイオームインスタンスを生成

### 変更の必要性
**なし** - 両プラットフォームの既存実装は完全で、ワールド生成統合のために変更不要。データパックが自動的にこれらの登録を参照する。

---

## データフロー図

```
┌─────────────────────────────────────────────────────────────────┐
│                    ワールド生成開始                               │
└─────────────────────────────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────┐
│   Multi-Noise Biome Source System                               │
│   - 各チャンクのmulti-noise parametersを計算                     │
│   - Temperature, Humidity, Continentalness, Erosion,            │
│     Depth, Weirdnessの6次元空間                                  │
└─────────────────────────────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────┐
│   overworld_caves.json の参照                                    │
│   - 計算されたparameterに一致するバイオームを検索                │
│   - Crystalline Cavesのparameter範囲と比較                       │
└─────────────────────────────────────────────────────────────────┘
                                ↓
                      一致した場合 ✓
                                ↓
┌─────────────────────────────────────────────────────────────────┐
│   Crystalline Caves バイオームの配置決定                         │
│   - biome: "worldgentest:crystalline_caves"                     │
└─────────────────────────────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────┐
│   Biome Registry Entryの解決                                     │
│   - Fabric: RegistryKey参照                                      │
│   - NeoForge: DeferredRegister参照                              │
└─────────────────────────────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────┐
│   crystalline_caves.json の読み込み                              │
│   - バイオーム設定（temperature, effects等）                     │
│   - Features配列からFeatureを配置                                │
│   - Spawners設定からMobスポーン                                  │
└─────────────────────────────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────┐
│   Biome Tagsのチェック                                           │
│   - is_overworld: 構造物生成の対象か？                           │
│   - has_structure/mineshaft: 廃坑を生成するか？                  │
└─────────────────────────────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────┐
│   チャンク生成完了                                               │
│   - Crystalline Cavesバイオーム                                  │
│   - クリスタルブロック、鉱石、装飾                               │
│   - Mobスポーン準備完了                                          │
└─────────────────────────────────────────────────────────────────┘
```

---

## バリデーションルール

### 1. Multi-Noise Parameters検証

#### ルール: Parameter範囲の有効性
```
すべてのparameterで: min <= max
かつ: -1.0 <= min, max <= 1.0
```

**Crystalline Cavesの検証**:
```
temperature: [0.5, 1.0]     ✓ 有効
humidity: [0.2, 0.6]        ✓ 有効
continentalness: [0.3, 0.9] ✓ 有効
erosion: [-0.5, 0.5]        ✓ 有効
depth: [0.2, 1.0]           ✓ 有効
weirdness: [-0.3, 0.3]      ✓ 有効
```

#### ルール: Depthの最小値
```
depth.min >= 0.2  （地表への漏出防止）
```

**Crystalline Cavesの検証**:
```
depth: [0.2, 1.0]  ✓ 有効（最小値0.2）
```

### 2. Biome ID検証

#### ルール: ID一貫性
```
すべての参照で同じID "worldgentest:crystalline_caves" を使用
```

**検証箇所**:
- ✓ `overworld_caves.json`: `"biome": "worldgentest:crystalline_caves"`
- ✓ `is_overworld.json`: `"worldgentest:crystalline_caves"`
- ✓ FabricBiomes.java: `Identifier.of("worldgentest", "crystalline_caves")`
- ✓ NeoForgeBiomes.java: `BIOMES.register("crystalline_caves", ...)`

### 3. Biome Tags検証

#### ルール: replace フィールド
```
replace: false  （バニラタグを上書きしない）
```

**Crystalline Cavesの検証**:
```
is_overworld.json: "replace": false  ✓ 有効
```

---

## パフォーマンスへの影響

### 1. Multi-Noise Parameter評価
- **影響**: 極めて低い
- **理由**: バニラのワールド生成システムと同じ方式。追加のparameterチェックは1バイオーム分のみ。

### 2. Feature配置
- **影響**: 既存のFeatureと同等
- **理由**: すべてのFeature（`crystal_ore_placed`等）は既にテスト済み。新規のFeature追加なし。

### 3. Mobスポーン
- **影響**: バニラ洞窟バイオームと同等
- **理由**: Mobスポーン設定はLush Caves、Dripstone Cavesと同程度。

### 4. 目標達成度（FR-008）
✅ **適合**: バニラ洞窟バイオーム（Lush Caves、Dripstone Caves）と同程度のパフォーマンス

---

## 変更履歴

| 日付 | バージョン | 変更内容 |
|------|----------|---------|
| 2025-10-08 | 1.0 | 初版作成 - Multi-noise parameters、Biome tags定義 |

---

## 次のステップ

1. ✅ Data model定義完了
2. ⏳ Contracts作成（JSONスキーマ）
3. ⏳ Quickstart作成（手動テスト手順）
4. ⏳ CLAUDE.md更新
