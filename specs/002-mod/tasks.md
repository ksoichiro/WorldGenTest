# Tasks: Crystalline Caves Biome World Generation

**Feature Branch**: `002-mod`
**Input**: Design documents from `/specs/002-mod/`
**Prerequisites**: plan.md, research.md, data-model.md, contracts/, quickstart.md

## Execution Flow (main)
```
1. Load plan.md from feature directory
   → Extract: Minecraft 1.21.1, Architectury, データパック実装
2. Load design documents:
   → data-model.md: Multi-noise parameters, Biome tags
   → contracts/: JSONスキーマ
   → quickstart.md: 8つのテストシナリオ
3. Generate tasks by category:
   → Setup: データパック作成
   → Core: ビルドと検証
   → Tests: 手動テストシナリオ
   → Polish: チューニングとドキュメント
4. Apply task rules:
   → データパック作成タスクは[P]（並行可能）
   → テストシナリオは[P]（独立実行可能）
5. Number tasks sequentially (T001, T002...)
6. Validate: すべてのJSONファイル作成とテストシナリオが含まれているか
7. Return: SUCCESS (tasks ready for execution)
```

## Format: `[ID] [P?] Description`
- **[P]**: Can run in parallel (different files, no dependencies)
- Include exact file paths in descriptions

## Path Conventions
Architecturyマルチプラットフォーム構造：
- **共通リソース**: `common/src/main/resources/data/`
- **ビルド成果物**: `fabric/build/libs/`, `neoforge/build/libs/`

---

## Phase 3.1: データパック作成（優先度: 最高）

### T001 [P] Multi-noise biome source parameter listファイルの作成

**ファイルパス**:
```
common/src/main/resources/data/worldgentest/worldgen/multi_noise_biome_source_parameter_list/overworld_caves.json
```

**内容**:
research.md（Decision 2）とdata-model.md（Entity 2）で定義されたパラメータを使用して、以下のJSONファイルを作成する：

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

**バリデーション**:
- contracts/multi-noise-parameters-schema.jsonに準拠していることを確認
- すべてのパラメータ範囲が `-1.0 <= value <= 1.0`
- `depth.min >= 0.2`（地表への漏出防止）

**依存**: なし

---

### T002 [P] バイオームタグファイル（is_overworld）の作成

**ファイルパス**:
```
common/src/main/resources/data/minecraft/tags/worldgen/biome/is_overworld.json
```

**内容**:
data-model.md（Entity 3, Tag 1）で定義されたバイオームタグを作成する：

```json
{
  "replace": false,
  "values": [
    "worldgentest:crystalline_caves"
  ]
}
```

**目的**:
- Crystalline Cavesをオーバーワールドのバイオームとして登録
- ワールド生成システムで認識されるようにする

**バリデーション**:
- contracts/biome-tag-schema.jsonに準拠していることを確認
- `replace: false`（バニラタグを上書きしない）

**依存**: なし

---

### T003 [P] バイオームタグファイル（has_structure/mineshaft）の作成

**ファイルパス**:
```
common/src/main/resources/data/worldgentest/tags/worldgen/biome/has_structure/mineshaft.json
```

**内容**:
data-model.md（Entity 3, Tag 2）で定義された構造物タグを作成する：

```json
{
  "replace": false,
  "values": [
    "worldgentest:crystalline_caves"
  ]
}
```

**目的**:
- 洞窟バイオームとして適切な構造物（廃坑）を生成
- 探索の楽しさとリソース獲得の機会を提供

**依存**: なし

---

## Phase 3.2: ビルドと検証（優先度: 高）

### T004 Gradleビルドの実行

**コマンド**:
```bash
cd /Users/ksoichiro/src/github.com/ksoichiro/WorldGenTest
./gradlew build
```

**期待される成果物**:
- `fabric/build/libs/worldgentest-fabric-1.0.0.jar`
- `neoforge/build/libs/worldgentest-neoforge-1.0.0.jar`

**検証ポイント**:
- ビルドがエラーなく完了すること
- 両プラットフォームのJARファイルが生成されること

**依存**: T001, T002, T003完了後

---

### T005 ビルド成果物の検証

**手順**:
1. **JARファイルの存在確認**:
   ```bash
   ls -lh fabric/build/libs/worldgentest-fabric-1.0.0.jar
   ls -lh neoforge/build/libs/worldgentest-neoforge-1.0.0.jar
   ```

2. **JARファイル内のデータパック確認**:
   ```bash
   # Fabric JAR内のデータパック確認
   unzip -l fabric/build/libs/worldgentest-fabric-1.0.0.jar | grep "worldgen/multi_noise_biome_source_parameter_list/overworld_caves.json"
   unzip -l fabric/build/libs/worldgentest-fabric-1.0.0.jar | grep "tags/worldgen/biome/is_overworld.json"

   # NeoForge JAR内のデータパック確認
   unzip -l neoforge/build/libs/worldgentest-neoforge-1.0.0.jar | grep "worldgen/multi_noise_biome_source_parameter_list/overworld_caves.json"
   unzip -l neoforge/build/libs/worldgentest-neoforge-1.0.0.jar | grep "tags/worldgen/biome/is_overworld.json"
   ```

**期待される結果**:
- すべてのデータパックファイルがJAR内に含まれている
- ファイルパスが正しい（`data/worldgentest/worldgen/...`、`data/minecraft/tags/...`）

**依存**: T004完了後

---

## Phase 3.3: 手動テスト（優先度: 中）
*quickstart.mdの各シナリオに対応*

### T006 シナリオ1: バイオームの基本生成確認（FR-001, FR-003）

**参照**: quickstart.md シナリオ1

**手順概要**:
1. Fabric/NeoForge環境でMinecraft 1.21.1を起動
2. 新規ワールドを作成（ワールドタイプ: **Crystalline World**、ゲームモード: クリエイティブ）
3. `/locate biome worldgentest:crystalline_caves` コマンドを実行
4. バイオームが見つかることを確認
5. バイオームに移動し、F3デバッグ画面で確認
6. クリスタルブロック、鉱石、装飾が生成されていることを確認

**成功基準**:
- ✅ `/locate biome`でバイオームが見つかる（1000～5000ブロック以内推奨）
- ✅ バイオーム内で既存のクリスタル関連ブロックが生成されている
- ✅ F3画面で `Biome: worldgentest:crystalline_caves` が表示される

**依存**: T005完了後

---

### T007 [P] シナリオ2: 出現頻度の検証（FR-002）

**参照**: quickstart.md シナリオ2

**手順概要**:
1. 3つの異なるシード値（12345, 67890, 11111）で**Crystalline World**タイプのワールドを作成
2. 各ワールドで `/locate biome worldgentest:crystalline_caves` を実行
3. 距離を記録

**成功基準**:
- ✅ 3つのワールドすべてでバイオームが見つかる
- ✅ 平均距離が10000ブロック以内（発見可能な範囲）

**依存**: T006完了後（T008, T009, T010と並行実行可能）

---

### T008 [P] シナリオ3: 深度範囲の検証（FR-003）

**参照**: quickstart.md シナリオ3

**手順概要**:
1. T006で見つけたCrystalline Cavesの座標に移動
2. バイオームの上端（最も浅い部分）のY座標を確認
3. 地表（Y=64付近）で同じX,Z座標のバイオームを確認

**成功基準**:
- ✅ Crystalline Cavesの最高Y座標が約38以下
- ✅ 地表（Y=64付近）ではCrystalline Cavesが表示されない

**依存**: T006完了後（T007, T009, T010と並行実行可能）

---

### T009 [P] シナリオ4: 既存特徴の保持確認（FR-004）

**参照**: quickstart.md シナリオ4

**手順概要**:
1. Crystalline Caves内を10分程度探索（サバイバルモード推奨）
2. 以下の要素を確認:
   - クリスタルブロック（発光ブロック）
   - クリスタル鉱石（通常・深層岩）
   - クリスタル装飾（鍾乳石・光る苔）
   - 水の色（青緑色）
   - Mobスポーン（敵対Mob、Crystal Golem）
   - 廃坑（構造物）

**成功基準**:
- ✅ すべてのクリスタル関連ブロックが生成されている
- ✅ Mobスポーンが正常に機能
- ✅ バイオームの視覚的特徴（水の色、霧等）が保たれている

**依存**: T006完了後（T007, T008, T010と並行実行可能）

---

### T010 [P] シナリオ6: シード値再現性の検証（FR-007）

**参照**: quickstart.md シナリオ6

**手順概要**:
1. シード値 `999999` でワールド1を作成
2. `/locatebiome worldgentest:crystalline_caves` を実行し、座標を記録
3. 同じシード値 `999999` でワールド2を作成
4. `/locatebiome worldgentest:crystalline_caves` を実行し、座標を記録
5. 2つのワールドで座標が一致することを確認

**成功基準**:
- ✅ 同じシード値で同じ座標にCrystalline Cavesが生成される

**依存**: T006完了後（T007, T008, T009と並行実行可能）

---

### T011 [P] シナリオ7: パフォーマンスの確認（FR-008）

**参照**: quickstart.md シナリオ7

**手順概要**:
1. Crystalline Caves内を飛行しながら新しいチャンクを生成
2. F3デバッグ画面でFPSを確認
3. Lush Caves内で同様のテストを実施し、FPSを比較
4. ワールド作成時間を測定（統合前後で比較）

**成功基準**:
- ✅ Crystalline Caves内でのFPSがLush Cavesと同程度
- ✅ ワールド作成時間の増加が10%以内

**依存**: T006完了後（T007, T008, T009, T010と並行実行可能）

---

### T012 シナリオ5: マルチプレイヤー環境での動作確認（FR-005）（オプション）

**参照**: quickstart.md シナリオ5

**手順概要**:
1. Fabric ServerまたはNeoForge Serverをセットアップ
2. WorldGenTest Modをサーバーの`mods`フォルダに配置
3. サーバーを起動
4. `/locatebiome worldgentest:crystalline_caves` を実行
5. 複数プレイヤーでログインし、同じバイオームを体験できることを確認

**成功基準**:
- ✅ サーバー環境でバイオームが正常に生成
- ✅ 複数プレイヤー間でバイオームの一貫性がある

**依存**: T006完了後（オプション、時間があれば実行）

---

## Phase 3.4: チューニング（優先度: 低、必要に応じて）

### T013 出現頻度の調整（必要に応じて）

**条件**: T007の結果、出現頻度が不適切な場合のみ実行

**手順**:
1. T007のテスト結果を確認
2. 出現頻度が高すぎる/低すぎる場合、`overworld_caves.json`の`offset`パラメータを調整:
   - より稀にする場合: `offset: -0.2`
   - より頻繁にする場合: `offset: 0.2`
3. T004（ビルド）を再実行
4. T007（出現頻度テスト）を再実行

**依存**: T007の結果に応じて実行

---

### T014 ドキュメント更新

**ファイルパス**:
- `CLAUDE.md`
- `specs/002-mod/plan.md`（Progress Tracking）

**内容**:
1. **CLAUDE.mdにテスト結果を追加**:
   - 実装完了日
   - テスト結果サマリー（全シナリオ合格/一部不合格）
   - 既知の問題や制限事項

2. **plan.mdのProgress Trackingを更新**:
   - Phase 4: Implementation complete → チェック
   - Phase 5: Validation passed → チェック

**依存**: すべてのテスト（T006-T012）完了後

---

## Dependencies（依存関係グラフ）

```
T001, T002, T003 [P] → データパック作成（並行実行可能）
       ↓
     T004 → Gradleビルド
       ↓
     T005 → ビルド成果物の検証
       ↓
     T006 → シナリオ1: 基本生成確認（必須）
       ↓
T007, T008, T009, T010, T011 [P] → テストシナリオ（並行実行可能）
       ↓
     T012 → シナリオ5: マルチプレイヤー（オプション）
       ↓
     T013 → 出現頻度の調整（必要に応じて）
       ↓
     T014 → ドキュメント更新
```

---

## Parallel Execution Examples（並行実行例）

### データパック作成フェーズ（T001-T003）
```bash
# 3つのJSONファイルを並行作成可能（異なるファイル）
# Task 1: overworld_caves.json
# Task 2: is_overworld.json
# Task 3: has_structure/mineshaft.json
```

### テストシナリオフェーズ（T007-T011）
T006完了後、以下のテストを並行実行可能：
```bash
# Task 1: シナリオ2 - 出現頻度の検証（3つのワールドで検証）
# Task 2: シナリオ3 - 深度範囲の検証
# Task 3: シナリオ4 - 既存特徴の保持確認
# Task 4: シナリオ6 - シード値再現性の検証
# Task 5: シナリオ7 - パフォーマンスの確認
```

**注意**: これらは手動テストのため、実際の並行実行は困難。テストの優先度と時間に応じて順次実行を推奨。

---

## Notes（実装上の注意）

### Minecraft Mod開発特有の考慮事項

1. **Javaコードの変更は不要**
   - プラットフォーム固有コード（FabricBiomes.java、NeoForgeBiomes.java）は現状維持
   - すべての実装はJSONデータパックで完結

2. **手動テストが中心**
   - Minecraft Mod開発では自動化されたユニットテストより、ゲーム内での統合テストと手動検証が主流
   - quickstart.mdの各シナリオに従って丁寧にテストを実施

3. **ビルド後のテスト必須**
   - データパック変更後は必ずビルドを実行
   - JARファイル内にデータパックが含まれていることを確認

4. **プラットフォーム間の一貫性**
   - Fabric/NeoForge両方でテストを実施
   - commonモジュールのデータパックが両プラットフォームで自動共有される

5. **段階的なデバッグ**
   - バイオームが見つからない場合: T001-T003のJSONファイルを確認
   - 地表に出現する場合: T001の`depth`パラメータを確認
   - 出現頻度が不適切な場合: T001の`offset`パラメータを調整

---

## Validation Checklist（検証チェックリスト）
*GATE: 各フェーズ完了前に確認*

### データパック作成（T001-T003）
- [ ] すべてのJSONファイルが正しいディレクトリに作成されている
- [ ] JSONスキーマ（contracts/）に準拠している
- [ ] パラメータ値がdata-model.mdの定義と一致している

### ビルドと検証（T004-T005）
- [ ] Fabric/NeoForge両方のビルドが成功している
- [ ] JARファイル内にすべてのデータパックファイルが含まれている

### テストシナリオ（T006-T012）
- [ ] すべての機能要件（FR-001〜FR-008）がテストされている
- [ ] テスト結果が記録されている
- [ ] 不合格の場合、原因が特定されている

### 全体
- [ ] すべてのタスクが完了している
- [ ] 既知の問題が文書化されている
- [ ] CLAUDE.mdとplan.mdが更新されている

---

## Task Status Tracking（タスク進捗追跡）

| Task | Status | Platform | Notes |
|------|--------|----------|-------|
| T001 | ✅ Complete | - | Multi-noise parameters |
| T002 | ✅ Complete | - | Biome tag: is_overworld |
| T003 | ✅ Complete | - | Biome tag: has_structure/mineshaft |
| T004 | ✅ Complete | Both | Gradle build |
| T005 | ✅ Complete | Both | Build verification |
| T006 | ⏳ Pending | Both | 基本生成確認 |
| T007 | ⏳ Pending | Both | 出現頻度検証 |
| T008 | ⏳ Pending | Both | 深度範囲検証 |
| T009 | ⏳ Pending | Both | 既存特徴確認 |
| T010 | ⏳ Pending | Both | シード値再現性 |
| T011 | ⏳ Pending | Both | パフォーマンス確認 |
| T012 | ⏳ Pending | Both | マルチプレイヤー（オプション） |
| T013 | ⏳ Pending | - | 出現頻度調整（必要時） |
| T014 | ⏳ Pending | - | ドキュメント更新 |

**Status Legend**:
- ⏳ Pending: 未着手
- 🚧 In Progress: 実行中
- ✅ Complete: 完了
- ❌ Failed: 失敗（要調査）
- ⏭️ Skipped: スキップ（オプションタスク）

---

## Quick Reference（クイックリファレンス）

### ファイルパス一覧
```
# データパックファイル（common/src/main/resources/data/）
worldgentest/worldgen/multi_noise_biome_source_parameter_list/overworld_caves.json
minecraft/tags/worldgen/biome/is_overworld.json
worldgentest/tags/worldgen/biome/has_structure/mineshaft.json

# ビルド成果物
fabric/build/libs/worldgentest-fabric-1.0.0.jar
neoforge/build/libs/worldgentest-neoforge-1.0.0.jar

# 設計ドキュメント
specs/002-mod/plan.md
specs/002-mod/research.md
specs/002-mod/data-model.md
specs/002-mod/contracts/
specs/002-mod/quickstart.md
specs/002-mod/tasks.md (this file)
```

### 重要なコマンド
```bash
# ビルド
./gradlew build

# クリーンビルド
./gradlew clean build

# Fabricのみビルド
./gradlew fabric:build

# NeoForgeのみビルド
./gradlew neoforge:build

# JARファイル内容確認
unzip -l fabric/build/libs/worldgentest-fabric-1.0.0.jar | grep worldgen

# ゲーム内コマンド
/locatebiome worldgentest:crystalline_caves
/tp @s X Y Z
```

---

**総タスク数**: 14タスク（T001-T014）
**必須タスク**: 11タスク（T001-T011, T014）
**オプションタスク**: 2タスク（T012, T013）

**推定実装時間**: 4-6時間
- データパック作成: 1-2時間
- ビルドと検証: 0.5-1時間
- テストシナリオ: 2-3時間

**次のステップ**: T001から順次実行開始。データパック作成タスク（T001-T003）は並行実行可能。
