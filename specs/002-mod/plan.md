# Implementation Plan: Crystalline Caves Biome World Generation

**Branch**: `002-mod` | **Date**: 2025-10-08 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/002-mod/spec.md`

## Execution Flow (/plan command scope)
```
1. Load feature spec from Input path
   → If not found: ERROR "No feature spec at {path}"
2. Fill Technical Context (scan for NEEDS CLARIFICATION)
   → Detect Project Type from context (web=frontend+backend, mobile=app+api)
   → Set Structure Decision based on project type
3. Fill the Constitution Check section based on the content of the constitution document.
4. Evaluate Constitution Check section below
   → If violations exist: Document in Complexity Tracking
   → If no justification possible: ERROR "Simplify approach first"
   → Update Progress Tracking: Initial Constitution Check
5. Execute Phase 0 → research.md
   → If NEEDS CLARIFICATION remain: ERROR "Resolve unknowns"
6. Execute Phase 1 → contracts, data-model.md, quickstart.md, agent-specific template file
7. Re-evaluate Constitution Check section
   → If new violations: Refactor design, return to Phase 1
   → Update Progress Tracking: Post-Design Constitution Check
8. Plan Phase 2 → Describe task generation approach (DO NOT create tasks.md)
9. STOP - Ready for /tasks command
```

**IMPORTANT**: The /plan command STOPS at step 8. Phases 2-4 are executed by other commands:
- Phase 2: /tasks command creates tasks.md
- Phase 3-4: Implementation execution (manual or via tools)

## Summary
Crystalline Cavesバイオームは既に実装されているが、通常のワールド生成に統合されていない。このプランでは、バイオームをMinecraftの標準的なワールド生成システムに登録し、サバイバルモードで自然に遭遇できるようにする。実装には、Architecturyのマルチプラットフォーム対応を維持しながら、Minecraft 1.21.1のバイオーム登録システムとワールド生成データパックを使用する。

## Technical Context
**Language/Version**: Java 21 (Minecraft 1.21.1対応)
**Primary Dependencies**:
- Architectury API 13.0.8
- Fabric API 0.116.6+1.21.1
- NeoForge 21.1.74
- Minecraft 1.21.1

**Storage**: データパック（JSON）でのワールド生成設定
**Testing**:
- ゲーム内での手動テスト（ワールド作成・探索）
- Fabricクライアントでの動作確認
- NeoForgeクライアントでの動作確認

**Target Platform**:
- Fabric Loader 0.17.2
- NeoForge 21.1.74
- クライアント・サーバー両対応

**Project Type**: Minecraft Mod（Architecturyマルチプラットフォーム）

**Performance Goals**:
- バニラ洞窟バイオーム（Lush Caves、Dripstone Caves）と同等のチャンク生成速度
- 既存のバイオーム生成に影響を与えない

**Constraints**:
- Architecturyの共通コード原則を維持
- プラットフォーム固有コードは最小限に
- 既存のバイオーム実装（ModBiomes.java、crystalline_caves.json）を変更せずに活用

**Scale/Scope**:
- 1つのバイオームを既存ワールド生成システムに統合
- バニラバイオームと同程度（中～高頻度）の出現率
- シングル・マルチプレイヤー両対応

## Constitution Check
*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

**Note**: プロジェクトの憲法ファイルはテンプレートのままで、具体的な原則が定義されていません。このプロジェクトはMinecraft Mod開発であり、以下のMinecraft Modding標準プラクティスに従います：

- ✅ **既存実装の再利用**: 既に実装されているバイオーム定義を活用
- ✅ **データ駆動設計**: ワールド生成はJSONデータパックで制御
- ✅ **マルチプラットフォーム対応**: Architecturyの共通コード原則を維持
- ✅ **バニラ互換性**: Minecraftの標準的なワールド生成システムを使用

**Initial Assessment**: PASS - 憲法違反なし、標準的なMinecraft Mod実装パターン

## Project Structure

### Documentation (this feature)
```
specs/002-mod/
├── plan.md              # This file (/plan command output)
├── research.md          # Phase 0 output (/plan command)
├── data-model.md        # Phase 1 output (/plan command)
├── quickstart.md        # Phase 1 output (/plan command)
├── contracts/           # Phase 1 output (/plan command) - JSONスキーマ
└── tasks.md             # Phase 2 output (/tasks command - NOT created by /plan)
```

### Source Code (repository root)
```
# Architecturyマルチプラットフォーム構造
common/
├── src/main/java/com/example/worldgentest/
│   ├── ModBiomes.java                    # 既存バイオーム定義
│   └── [新規] バイオーム登録関連クラス（必要に応じて）
└── src/main/resources/data/
    ├── minecraft/tags/worldgen/biome/    # [新規] バイオームタグ
    └── worldgentest/worldgen/
        ├── biome/crystalline_caves.json  # 既存バイオーム定義
        └── [新規] バイオーム生成設定

fabric/
├── src/main/java/com/example/worldgentest/
│   └── [新規] Fabric固有のバイオーム登録（必要に応じて）
└── src/main/resources/
    └── [共通リソースが自動コピー]

neoforge/
├── src/main/java/com/example/worldgentest/
│   └── [新規] NeoForge固有のバイオーム登録（必要に応じて）
└── src/main/resources/
    └── [共通リソースが自動コピー]
```

**Structure Decision**: Architecturyマルチプラットフォーム構造を使用。ワールド生成の設定は主にJSONデータパック（`common/src/main/resources/data/`）で行い、プラットフォーム固有コードは必要最小限に留める。

## Phase 0: Outline & Research
1. **Extract unknowns from Technical Context** above:
   - Minecraft 1.21.1でのバイオーム登録方法（データパック vs コード）
   - Architecturyでのマルチプラットフォームバイオーム登録パターン
   - バニラのワールド生成における洞窟バイオームの出現頻度設定
   - バイオームタグの適切な使用方法

2. **Generate and dispatch research agents**:
   - Task: "Minecraft 1.21.1のバイオーム登録システムとワールド生成統合方法を調査"
   - Task: "Architecturyでのマルチプラットフォームバイオーム登録のベストプラクティスを調査"
   - Task: "バニラ洞窟バイオーム（Lush Caves、Dripstone Caves）の生成頻度パラメータを調査"
   - Task: "Minecraft 1.21.1のバイオームタグシステムを調査"

3. **Consolidate findings** in `research.md` using format:
   - Decision: [選択した実装方法]
   - Rationale: [選択理由]
   - Alternatives considered: [検討した代替案]

**Output**: research.md with all NEEDS CLARIFICATION resolved

## Phase 1: Design & Contracts
*Prerequisites: research.md complete*

1. **Extract entities from feature spec** → `data-model.md`:
   - Crystalline Caves Biome（既存）
   - World Generation Configuration（新規設定項目）
   - Biome Registry Entry（登録情報）
   - バイオームタグと生成条件

2. **Generate API contracts** from functional requirements:
   - Minecraft Mod開発では従来のREST APIではなく、以下を契約として定義：
   - バイオーム登録インターフェース仕様
   - ワールド生成データパックのJSONスキーマ
   - バイオームタグのJSONスキーマ
   - 出力: `/contracts/` ディレクトリにJSONスキーマファイル

3. **Generate contract tests** from contracts:
   - Minecraft Modでは自動化されたユニットテストより、統合テストとゲーム内検証が主流
   - 検証項目を `quickstart.md` に記載：
     - 新規ワールド作成でバイオームが生成されるか
     - バイオームの出現頻度が適切か
     - 既存の特徴（クリスタルブロック、Mob等）が保たれているか

4. **Extract test scenarios** from user stories:
   - quickstart.mdに手動テストシナリオを記載
   - 各機能要件（FR-001〜FR-008）に対応する検証手順

5. **Update agent file incrementally** (O(1) operation):
   - Run `.specify/scripts/bash/update-agent-context.sh claude`
   - Crystalline Cavesバイオーム統合の実装パターンを追加
   - 既存の手動追加を保持
   - 最近の変更（最新3件）を更新

**Output**: data-model.md, /contracts/*, quickstart.md, CLAUDE.md更新

## Phase 2: Task Planning Approach
*This section describes what the /tasks command will do - DO NOT execute during /plan*

**Task Generation Strategy**:
- Load `.specify/templates/tasks-template.md` as base
- Generate tasks from Phase 1 design docs（data-model.md, contracts/, quickstart.md）

**タスクカテゴリ**:

### カテゴリ1: データパック作成（優先度: 最高）
1. Multi-noise biome source parameter listファイルの作成
   - ファイル: `overworld_caves.json`
   - パラメータ: research.mdとdata-model.mdで定義された値を使用
   - 依存: なし [P]

2. バイオームタグファイルの作成
   - ファイル: `is_overworld.json`（必須）
   - ファイル: `has_structure/mineshaft.json`（推奨）
   - 依存: なし [P]

### カテゴリ2: ビルドと検証（優先度: 高）
3. Gradleビルドの実行
   - Fabric/NeoForge両方をビルド
   - 依存: タスク1,2完了後

4. ビルド成果物の確認
   - JARファイルの存在確認
   - データパックファイルがJAR内に含まれているか確認
   - 依存: タスク3完了後

### カテゴリ3: 手動テスト（優先度: 中）
quickstart.mdの各シナリオに対応：

5. シナリオ1: バイオームの基本生成確認（FR-001, FR-003）
   - `/locatebiome`でバイオーム検出
   - 依存: タスク4完了後

6. シナリオ2: 出現頻度の検証（FR-002）
   - 複数のシード値でテスト
   - 依存: タスク5完了後 [P]

7. シナリオ3: 深度範囲の検証（FR-003）
   - Y座標確認
   - 依存: タスク5完了後 [P]

8. シナリオ4: 既存特徴の保持確認（FR-004）
   - クリスタルブロック、鉱石、Mob確認
   - 依存: タスク5完了後 [P]

9. シナリオ5: マルチプレイヤー環境での動作確認（FR-005）
   - サーバーセットアップとテスト
   - 依存: タスク5完了後（オプション）

10. シナリオ6: シード値再現性の検証（FR-007）
    - 同じシード値で2つのワールド作成
    - 依存: タスク5完了後 [P]

11. シナリオ7: パフォーマンスの確認（FR-008）
    - FPS測定とバニラバイオームとの比較
    - 依存: タスク5完了後 [P]

### カテゴリ4: チューニング（優先度: 低、必要に応じて）
12. 出現頻度の調整
    - `offset`パラメータの調整
    - 依存: タスク6の結果に応じて

13. ドキュメント更新
    - CLAUDE.mdにテスト結果を追加
    - 依存: すべてのテスト完了後

**Ordering Strategy**:
1. データパック作成（タスク1, 2）→ ビルド（タスク3, 4）→ テスト（タスク5-11）→ チューニング（タスク12, 13）
2. データパック作成タスクは並行実行可能 [P]
3. テストタスク（6-8, 10-11）は並行実行可能 [P]
4. タスク9（マルチプレイヤーテスト）はオプション、時間があれば実行

**Estimated Output**: 13 numbered, ordered tasks in tasks.md

**Implementation Notes**:
- Javaコードの変更は不要（プラットフォーム固有コードは現状維持）
- すべての実装はJSONデータパックで完結
- テストは手動テストが中心（Minecraft Mod開発の標準）

**IMPORTANT**: This phase is executed by the /tasks command, NOT by /plan

## Phase 3+: Future Implementation
*These phases are beyond the scope of the /plan command*

**Phase 3**: Task execution (/tasks command creates tasks.md)
**Phase 4**: Implementation (execute tasks.md following Minecraft modding best practices)
**Phase 5**: Validation (execute quickstart.md, in-game testing)

## Complexity Tracking
*Fill ONLY if Constitution Check has violations that must be justified*

プロジェクト憲法がテンプレートのままであり、具体的な違反はありません。

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| N/A | N/A | N/A |

## Progress Tracking
*This checklist is updated during execution flow*

**Phase Status**:
- [x] Phase 0: Research complete (/plan command)
- [x] Phase 1: Design complete (/plan command)
- [x] Phase 2: Task planning complete (/plan command - describe approach only)
- [ ] Phase 3: Tasks generated (/tasks command)
- [ ] Phase 4: Implementation complete
- [ ] Phase 5: Validation passed

**Gate Status**:
- [x] Initial Constitution Check: PASS
- [x] Post-Design Constitution Check: PASS
- [x] All NEEDS CLARIFICATION resolved
- [x] Complexity deviations documented (N/A - no violations)

**Artifacts Generated**:
- [x] research.md - Multi-noise biome source調査完了
- [x] data-model.md - パラメータとエンティティ定義完了
- [x] contracts/ - JSONスキーマファイル作成完了
  - [x] multi-noise-parameters-schema.json
  - [x] biome-tag-schema.json
  - [x] README.md
- [x] quickstart.md - 手動テスト手順作成完了
- [x] CLAUDE.md - エージェントコンテキスト更新完了

---
*Based on Minecraft Mod Development Best Practices - Architectury Multi-Platform*
