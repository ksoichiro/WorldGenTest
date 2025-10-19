# Contracts: Minecraft JSON Schema Definitions

**Feature**: カスタムディメンション追加
**Branch**: `003-mod`
**Date**: 2025-10-19

## 概要

このディレクトリには、Minecraft 1.21.1のカスタムディメンションシステムで使用するJSONファイルの構造定義を含みます。

実際のJSONファイルは以下に配置されます：
```
common/src/main/resources/data/worldgentest/
├── dimension_type/crystal_dimension.json
├── dimension/crystal_dimension.json
└── worldgen/biome/*.json
```

## JSON Schema参照

### Dimension Type

**ファイル**: `data/worldgentest/dimension_type/crystal_dimension.json`

**スキーマ**: Minecraft公式Dimension Type schema

**オンラインバリデーター**:
- https://misode.github.io/dimension-type/
- https://worldgen.syldium.dev/

**主要フィールド**:
- `fixed_time`: 6000（固定時刻、正午）
- `effects`: `"minecraft:overworld"`（視覚効果）
- `coordinate_scale`: 1.0（座標スケール）
- `bed_works`: true（ベッドでリスポーン設定可能）

### Level Stem (Dimension)

**ファイル**: `data/worldgentest/dimension/crystal_dimension.json`

**スキーマ**: Minecraft公式Dimension schema

**オンラインバリデーター**:
- https://misode.github.io/dimension/

**主要フィールド**:
- `type`: `"worldgentest:crystal_dimension"`（参照先：dimension_type）
- `generator.type`: `"minecraft:noise"`（ノイズジェネレーター）
- `generator.biome_source.type`: `"minecraft:multi_noise"`（マルチノイズバイオームソース）

### Biome

**ファイル**: `data/worldgentest/worldgen/biome/*.json`

**スキーマ**: Minecraft公式Biome schema

**オンラインバリデーター**:
- https://misode.github.io/biome/

**4種類のバイオーム**:
1. `crystal_plains.json`（草原、40%）
2. `crystal_forest.json`（森林、30%）
3. `crystal_desert.json`（砂漠、20%）
4. `crystal_river.json`（川、10%）

**主要フィールド**:
- `temperature`: 温度パラメータ
- `downfall`: 降水量
- `sky_color`: 空の色（`0x8E44AD`=紫系）
- `water_color`: 水の色（川バイオームのみ、`0x3FA3D3`=水色）

## BlockState Definition

**ファイル**: `common/src/main/resources/assets/worldgentest/blockstates/crystal_portal.json`

**Minecraft BlockState JSONフォーマット**:
```json
{
  "variants": {
    "axis=x": {
      "model": "worldgentest:block/crystal_portal_ew"
    },
    "axis=z": {
      "model": "worldgentest:block/crystal_portal_ns"
    }
  }
}
```

**Properties**:
- `axis`: `x` または `z`（ポータルの向き）

## バリデーション方法

### オンラインツール（推奨）

1. [Misode's Generator Suite](https://misode.github.io/)
   - Dimension Type, Dimension, Biome, Noise Settingsの生成・検証
   - Minecraft 1.21対応

2. [Syldium Worldgen Generator](https://worldgen.syldium.dev/)
   - 1.21.4対応の包括的ジェネレーター

### ローカル検証

```bash
# ビルド時にJSONバリデーションが自動実行される
./gradlew build

# JSONシンタックスチェック
jq . common/src/main/resources/data/worldgentest/dimension_type/crystal_dimension.json
```

## 参考資料

- [Minecraft Wiki - Dimension type](https://minecraft.wiki/w/Dimension_type)
- [Minecraft Wiki - Custom dimension](https://minecraft.fandom.com/wiki/Custom_dimension)
- [Misode's Data Pack Generators](https://misode.github.io/)
