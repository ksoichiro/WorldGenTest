# Contracts: Crystalline Caves Biome World Generation

**Feature**: Crystalline Cavesバイオームのワールド生成統合
**Date**: 2025-10-08

## 概要

このディレクトリには、Crystalline Cavesバイオームをワールド生成に統合するために必要なデータ構造のJSONスキーマファイルが含まれています。

## Contracts（契約）

Minecraft Mod開発では、従来のREST APIではなく、**データパックのJSON形式**が実装契約として機能します。これらのJSONスキーマファイルは、実装が準拠すべき構造を定義します。

### 1. Multi-Noise Biome Source Parameters

**ファイル**: `multi-noise-parameters-schema.json`

**目的**: Minecraft 1.21.1のmulti-noise biome sourceシステムでバイオームをどこに配置するかを定義する。

**主要な要素**:
- **preset**: ワールド生成プリセット（`minecraft:overworld`）
- **biomes**: バイオームエントリのリスト
  - **biome**: バイオームID（`worldgentest:crystalline_caves`）
  - **parameters**: 6次元パラメータ空間
    - temperature: 温度範囲
    - humidity: 湿度範囲
    - continentalness: 大陸性範囲
    - erosion: 侵食範囲
    - depth: 深度範囲（⭐ 洞窟バイオームで最重要）
    - weirdness: 奇妙さ範囲
    - offset: 出現頻度補正

**バリデーションルール**:
- すべてのパラメータ範囲で `min <= max`
- すべての値が `-1.0 <= value <= 1.0`
- `depth.min >= 0.2`（洞窟バイオームの推奨値）

**実装ファイルパス**:
```
common/src/main/resources/data/worldgentest/worldgen/multi_noise_biome_source_parameter_list/overworld_caves.json
```

### 2. Biome Tags

**ファイル**: `biome-tag-schema.json`

**目的**: バイオームをタグでグループ化し、ワールド生成システム、構造物、Mobスポーンで参照できるようにする。

**主要な要素**:
- **replace**: タグ内容を置き換えるか追加するか（`false`推奨）
- **values**: バイオームIDのリスト

**バリデーションルール**:
- `replace: false`（バニラタグを上書きしない）
- `values`配列に少なくとも1つのバイオームID

**実装ファイルパス**:
```
# 必須タグ
common/src/main/resources/data/minecraft/tags/worldgen/biome/is_overworld.json

# 推奨タグ
common/src/main/resources/data/worldgentest/tags/worldgen/biome/has_structure/mineshaft.json
```

## スキーマの使用方法

### 1. スキーマ検証（オプション）

JSON SchemaバリデータツールでJSONファイルを検証：

```bash
# npm install -g ajv-cli
ajv validate -s contracts/multi-noise-parameters-schema.json -d path/to/overworld_caves.json
ajv validate -s contracts/biome-tag-schema.json -d path/to/is_overworld.json
```

### 2. IDEでのスキーマ参照

Visual Studio CodeやIntelliJ IDEAでは、JSONファイルの先頭に以下を追加してスキーマを参照できます：

```json
{
  "$schema": "../../specs/002-mod/contracts/multi-noise-parameters-schema.json",
  ...
}
```

これにより、自動補完、バリデーション、ドキュメント表示が有効になります。

## Contract Test（契約テスト）

Minecraft Modでは自動化されたユニットテストよりも、**ゲーム内での統合テストと手動検証**が主流です。

### 検証項目

#### 1. Multi-Noise Parameters契約

**テスト方法**: ゲーム内での検証
- ✅ バイオームが生成されるか（`/locatebiome worldgentest:crystalline_caves`）
- ✅ 出現頻度が適切か（複数のワールドで検証）
- ✅ 地表に漏出していないか（Y座標確認）
- ✅ 既存特徴（クリスタルブロック、Mob等）が保たれているか

**期待される結果**:
- バイオームが約Y=38以下の洞窟で見つかる
- Lush Caves、Dripstone Cavesと同程度の頻度で出現
- 温暖な地域（温度0.5～1.0）で出現

#### 2. Biome Tags契約

**テスト方法**: ゲーム内での検証
- ✅ オーバーワールドのバイオームとして認識されているか
- ✅ 構造物（廃坑等）が生成されるか
- ✅ Mobスポーンが機能しているか

**期待される結果**:
- Crystalline Caves内で廃坑が見つかる（`has_structure/mineshaft`タグが有効な場合）
- バイオーム内でMobが正常にスポーンする

### 手動テスト手順

詳細な手動テスト手順は [`quickstart.md`](./quickstart.md) を参照してください。

## 機能要件との対応

| 契約 | 対応する機能要件 |
|------|----------------|
| Multi-Noise Parameters | FR-001, FR-002, FR-003, FR-006, FR-007 |
| Biome Tags (is_overworld) | FR-001, FR-005, FR-006 |
| Parameter: depth >= 0.2 | FR-003（地表に出現せず探索で発見） |
| Parameter: offset = 0.0 | FR-002（中～高頻度の出現） |
| 既存Features保持 | FR-004（既存特徴の維持） |
| バニラと同じ方式 | FR-008（パフォーマンス） |

## 参考資料

### Minecraft公式ドキュメント
- **Multi-Noise Parameters**: https://minecraft.wiki/w/Noise_settings
- **Biome Tags**: https://minecraft.wiki/w/Tag#Biome_tags
- **World Generation**: https://minecraft.wiki/w/World_generation

### ツール
- **Misode's Biome Generator**: https://misode.github.io/worldgen/biome/
  - Multi-noise parametersのビジュアル編集・検証ツール
- **JSON Schema Validator**: https://www.jsonschemavalidator.net/

### バニラ実装の参考
- Lush Caves: `minecraft:lush_caves`
- Dripstone Caves: `minecraft:dripstone_caves`
- Deep Dark: `minecraft:deep_dark`

これらのバニラバイオームのmulti-noise parametersとタグ設定を参考にして、Crystalline Cavesの実装を調整できます。

## 変更履歴

| 日付 | バージョン | 変更内容 |
|------|----------|---------|
| 2025-10-08 | 1.0 | 初版作成 - Multi-noise parameters、Biome tagsスキーマ |
