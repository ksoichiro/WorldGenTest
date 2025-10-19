# Implementation Plan: カスタムディメンション追加

**Branch**: `003-mod` | **Date**: 2025-10-19 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/003-mod/spec.md`

## Summary

カスタムディメンションシステムの実装。プレイヤーは既存のMODアイテム（クリスタルブロックとクリスタルの欠片）を使用してネザーポータル風のポータルフレームを構築し、新しいディメンションにアクセスできる。ディメンション内は4種類のカスタムバイオーム（平原40%、森林30%、砂漠20%、川10%）で構成され、常に昼間で紫がかった空を持つ。座標スケールは1:4（ディメンション:オーバーワールド）。

技術的アプローチ：
- Minecraftのディメンションシステム（Dimension Type、Level Stem）を活用
- Terrablenderでカスタムバイオーム統合（既存の実装パターン再利用）
- ポータル検出・起動ロジックはバニラのNetherPortalパターンを参考
- プラットフォーム分離（Common/Fabric/NeoForge）によるマルチプラットフォーム対応

## Technical Context

**Language/Version**: Java 21 (Minecraft 1.21.1要件)
**Primary Dependencies**:
  - Architectury 13.0.8 (マルチプラットフォーム抽象化)
  - TerraBlender 4.1.0.3 (バイオーム統合、既存利用中)
  - Minecraft 1.21.1 (NeoForge 21.1.76 / Fabric Loader 0.16.9)

**Storage**: Minecraftワールドセーブシステム（NBT形式、Level Storage API）
**Testing**:
  - Minecraft実行時テスト（`./gradlew runClient`）
  - 可能な範囲でユニットテスト（バニラAPIモック困難なため限定的）

**Target Platform**:
  - Fabric (Yarn mapping)
  - NeoForge (Mojang mapping)
  - クライアント＆サーバーサイド両対応

**Project Type**: Architectury マルチプラットフォームMOD（common/fabric/neoforge構成）

**Performance Goals**:
  - ポータルテレポート: <200ms（バニラネザーポータルと同等）
  - チャンク生成: バニラオーバーワールドと同等速度
  - ポータル検出: フレーム構築時の遅延<50ms

**Constraints**:
  - Minecraft 1.21.1 API互換性必須
  - マルチプレイヤー同期対応
  - プラットフォーム間のmapping差異対応（Yarn vs Mojang）
  - メモリ使用量: バニラディメンション+10%以内

**Scale/Scope**:
  - 新規カスタムブロック: 最低4種（草、土、木材、砂） + バリアント
  - 新規バイオーム: 4種類
  - 新規ディメンション: 1つ
  - ポータルシステム: フレーム検出、起動、テレポート、リンク管理
  - 推定実装規模: 1500-2500行（Javaコード）+ JSONデータ

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### WorldGenTest開発原則との整合性

**原則1: 段階的実装**
- ✅ **適合**: 既存機能（クリスタルブロック、クリスタルの欠片、Terrablender統合）を活用
- ✅ **適合**: 小規模なポータルシステムから開始し、ディメンション→バイオーム→ブロックと段階的に拡張
- ⚠️ **注意**: 大規模機能のため、フェーズ分割を厳守（Phase 0研究→Phase 1設計→Phase 2実装タスク化）

**原則2: プラットフォーム分離（Architectury原則）**
- ✅ **適合**: commonモジュールに最大限コードを集約
- ✅ **適合**: プラットフォーム固有処理（ディメンション登録、ポータルブロック登録）のみFabric/NeoForgeに分離
- ✅ **適合**: 既存のYarn/Mojang mapping差異対応パターンを活用（Terrablender Region実装で実績あり）

**原則3: リソース管理**
- ✅ **適合**: テクスチャ、バイオームJSON、ブロック定義はcommon/resources/に配置
- ✅ **適合**: プラットフォーム固有リソース（エントリーポイント設定）のみfabric/neoforge側に配置

**原則4: パフォーマンス最適化**
- ✅ **適合**: チャンク生成速度はバニラと同等（NFR-002）
- ✅ **適合**: ポータル検出はイベントドリブン（BlockPlaceEvent）で実装、常時スキャン回避

**原則5: Minecraft 1.21.1対応**
- ✅ **適合**: ディレクトリ構造（data/.../worldgen/）は単数形
- ✅ **適合**: JSONフォーマットは1.21.1仕様準拠
- ✅ **適合**: Dimension Type JSONは最新フォーマット使用

### 複雑性の正当化

**新規システム追加の妥当性:**

| 追加要素 | 必要性 | 代替案を棄却する理由 |
|---------|-------|---------------------|
| カスタムディメンション | カスタムバイオーム専用空間として必須 | オーバーワールドへの統合では既存バイオームとの混在が発生、仕様要件「すべてカスタムバイオーム」を満たせない |
| ポータルシステム | ディメンションアクセス手段として必須 | コマンド `/execute in` のみでは一般プレイヤー体験を損なう |
| 座標リンクシステム(1:4) | ポータル位置対応付けに必須 | バニラネザーと同様の挙動をユーザーが期待、座標無関係配置では混乱を招く |
| 4種カスタムバイオーム | 仕様要件の最小セット | 1-2種では探索要素が不足、5種以上は初期実装スコープ過大 |

**ゲート判定**: ✅ **PASS** - すべての複雑性は仕様要件から導出され、代替案より優れている

## Project Structure

### Documentation (this feature)

```
specs/003-mod/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (技術調査結果)
├── data-model.md        # Phase 1 output (エンティティとデータ構造)
├── quickstart.md        # Phase 1 output (開発者向けクイックスタート)
├── contracts/           # Phase 1 output (Minecraft JSONスキーマ定義)
│   ├── dimension_type.schema.json
│   ├── level_stem.schema.json
│   ├── biome.schema.json
│   └── portal_block_state.schema.json
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)

```
# Architectury マルチプラットフォーム構成（既存）
common/
├── src/main/java/com/example/worldgentest/
│   ├── dimension/                    # NEW: ディメンション管理
│   │   ├── ModDimensions.java       # Dimension Type, Level Stem登録
│   │   ├── CrystalDimensionTeleporter.java  # テレポート・座標変換ロジック
│   │   └── PortalForcer.java        # ポータル生成・リンク管理
│   ├── portal/                       # NEW: ポータルシステム
│   │   ├── CrystalPortalBlock.java  # ポータルブロック定義
│   │   ├── PortalFrameDetector.java # フレーム検出ロジック
│   │   └── PortalActivationHandler.java  # アイテム使用時の起動処理
│   ├── biome/                        # NEW: カスタムバイオーム（Terrablender統合）
│   │   └── CrystalDimensionRegion.java  # NeoForge用Region (Mojang mapping)
│   ├── block/                        # NEW: カスタムブロック
│   │   ├── CrystalGrassBlock.java
│   │   ├── CrystalDirtBlock.java
│   │   ├── CrystalLogBlock.java
│   │   ├── CrystalSandBlock.java
│   │   └── ModBlocks.java (追加登録)
│   └── [既存ファイル]
├── src/main/resources/
│   ├── assets/worldgentest/
│   │   ├── textures/block/          # NEW: カスタムブロックテクスチャ（バニラベース色調変更）
│   │   │   ├── crystal_grass_block_top.png
│   │   │   ├── crystal_grass_block_side.png
│   │   │   ├── crystal_dirt.png
│   │   │   ├── crystal_log.png
│   │   │   └── crystal_sand.png
│   │   ├── blockstates/             # NEW: ブロック状態定義
│   │   │   ├── crystal_portal.json
│   │   │   ├── crystal_grass_block.json
│   │   │   └── [...]
│   │   └── models/block/            # NEW: ブロックモデル
│   │       ├── crystal_grass_block.json
│   │       └── [...]
│   ├── data/worldgentest/
│   │   ├── worldgen/
│   │   │   ├── dimension_type/      # NEW: ディメンション定義
│   │   │   │   └── crystal_dimension.json
│   │   │   ├── dimension/           # NEW: Level Stem (Dimension + Generator)
│   │   │   │   └── crystal_dimension.json
│   │   │   ├── biome/               # NEW: カスタムバイオーム定義
│   │   │   │   ├── crystal_plains.json
│   │   │   │   ├── crystal_forest.json
│   │   │   │   ├── crystal_desert.json
│   │   │   │   └── crystal_river.json
│   │   │   ├── configured_feature/ # NEW: バイオーム特徴（樹木等）
│   │   │   │   └── crystal_tree.json
│   │   │   └── placed_feature/     # NEW: 特徴配置
│   │   │       └── crystal_tree_placed.json
│   │   ├── loot_table/block/       # NEW: カスタムブロックドロップ
│   │   │   ├── crystal_grass_block.json
│   │   │   └── [...]
│   │   └── tags/block/              # NEW: ブロックタグ（採掘ツール設定）
│   │       ├── mineable/
│   │       │   ├── pickaxe.json    # ポータルブロック追加
│   │       │   └── shovel.json     # 草・土・砂追加
│   │       └── needs_iron_tool.json
│   └── [既存リソース]

fabric/
├── src/main/java/com/example/worldgentest/
│   ├── FabricModDimensions.java     # NEW: Fabric固有ディメンション登録
│   ├── FabricModBlocks.java         # 既存（カスタムブロック追加）
│   ├── biome/
│   │   └── FabricCrystalDimensionRegion.java  # NEW: Fabric用Region (Yarn mapping)
│   └── [既存ファイル]
├── src/main/resources/
│   └── fabric.mod.json              # entrypointsに"terrablender"追加
└── [既存ファイル]

neoforge/
├── src/main/java/com/example/worldgentest/
│   ├── ModDimensions.java           # NEW: NeoForge固有ディメンション登録
│   ├── ModBlocks.java               # 既存（カスタムブロック追加）
│   ├── biome/
│   │   └── NeoForgeCrystalDimensionRegion.java  # NEW: NeoForge初期化（commonのRegion再利用）
│   └── [既存ファイル]
└── [既存ファイル]

tests/                               # Minecraftテストは限定的
├── unit/                            # ロジックのみユニットテスト可能
│   ├── PortalFrameDetectorTest.java # フレーム検出ロジック
│   └── CoordinateScalerTest.java    # 座標変換ロジック
└── README.md                        # テスト方針説明（実行時テスト中心）
```

**Structure Decision**: Architectury マルチプラットフォーム構成を継続。ディメンション・ポータル・バイオーム関連コードはcommonに集約し、プラットフォーム固有の登録処理（Dimension Type登録、Terrablender Region登録）のみfabric/neoforgeに配置。これはプロジェクトの既存パターン（Terrablender統合、エンティティ登録）と一致し、Yarn/Mojang mapping差異を適切に分離する。

## Complexity Tracking

*GATE条件を満たすため、このセクションは空（違反なし）*

Constitution Checkにてすべての複雑性が正当化されたため、追加の正当化は不要。

