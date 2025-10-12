# WorldGenTest 開発ガイドライン

このドキュメントは、WorldGenTestプロジェクトの開発に関するガイドライン、注意事項、今後の計画をまとめたものです。

## プロジェクト概要

WorldGenTestは、Architecturyフレームワークを使用したMinecraft Modの開発学習プロジェクトです。将来的な大規模Mod開発を想定し、様々な技術やパターンを検証することを目的としています。

## 技術選択の理由

### Architectury選択理由
- **マルチプラットフォーム対応**: Forge、NeoForge、Fabricに対応可能
- **コード共有**: プラットフォーム間でのコード重複を最小化
- **メンテナンス性**: 一つのコードベースで複数のプラットフォームをサポート
- **コミュニティサポート**: 活発な開発とアップデート

### ModDevGradle 2.0採用理由
- **シンプルな設定**: NeoGradleより簡潔なビルドスクリプト
- **安定性**: 2025年1月に安定版リリース
- **パフォーマンス**: より高速なビルド処理

### Terrablender採用理由（2025年10月追加）
- **互換性の高さ**: 他のMODとのバイオーム競合を自動解決
- **保守性**: 巨大なバニラパラメータファイル（7593エントリー）の管理から解放
- **シンプルな統合**: カスタムワールドプリセット不要、バニラの「Default」ワールドタイプに自動統合
- **業界標準**: 多くの人気MODで採用されている実績あるライブラリ
- **プラットフォーム対応**: Fabric/NeoForge両対応の実装パターン確立

## 開発方針

### コード品質
1. **可読性重視**: 分かりやすい変数名、クラス名を使用
2. **モジュール化**: 機能ごとに適切にクラスを分離
3. **ドキュメント**: 複雑な処理にはコメントを追加
4. **テスト**: 可能な限りテストコードを作成

### 機能実装
1. **段階的実装**: 小さな機能から始めて徐々に拡張
2. **バージョン管理**: 機能追加時は適切にコミット
3. **互換性**: 既存機能を壊さないよう注意
4. **パフォーマンス**: 重い処理は最適化を検討

## 実装済み機能

### ✅ 完了済み

#### プロジェクト基盤
- Architecturyマルチプラットフォーム設定
- Gradle設定（ModDevGradle 2.0）
- 基本的なModエントリーポイント
- クリエイティブタブ設定

#### カスタムブロック
- クリスタルブロック（発光ブロック）
- ブロックモデル、アイテムモデル
- 多言語対応（英語・日本語）

#### カスタムアイテム
- クリスタルの欠片（Crystal Shard）
- クリスタルツール5種類（剣、ピッケル、斧、シャベル、クワ）
- カスタムツール材料（ModToolTiers）
- プラットフォーム固有実装（Fabric: ToolMaterial、NeoForge: Tier）
- レシピシステム統合

#### カスタムバイオーム
- **Crystalline Caves**（クリスタルの洞窟）- 2025年10月Terrablender統合完了
- バイオーム設定（温度、湿度、色彩）
- Mob生成設定
- 地形生成設定（洞窟、鉱石）
- **Terrablenderによる実装**:
  - Region-based統合システム（`CrystallineCavesRegion`）
  - 地下専用バイオーム（depth: 0.2-1.0）
  - バニラ「Default」ワールドタイプへの自動統合
  - プラットフォーム別実装（Fabric: `FabricCrystallineCavesRegion`、NeoForge: `CrystallineCavesRegion`）
  - Yarn/Mojang mapping差異の解決パターン確立

#### カスタムエンティティ
- [x] Crystal Golem（クリスタルゴーレム）- Fabric/NeoForge両対応
- エンティティ属性、AI行動の実装
- カスタムモデル・レンダリング・テクスチャ
- プラットフォーム固有の登録システム

#### レシピシステム
- クリスタルブロック crafting recipe（Minecraft 1.21.1対応）
- クリスタルツール5種類のcrafting recipe（Minecraft 1.21.1対応）
- [x] **精錬レシピ**（2025年9月実装完了）
  - クリスタルの欠片の精錬レシピ（furnace & blast_furnace）
  - 将来の鉱石対応レシピ（crystal_ore → crystal_shard）
  - 対応するアドバンスメントファイル
- アドバンスメントシステム統合
- レシピブック対応
- カテゴリ別アドバンスメント（decorations、equipment、misc）

#### カスタム構造物
- [⚠️] **地表装飾**（2025年9月実装・要改善）
  - クリスタル神殿（地表配置）- 現在限定的に動作
  - 古代クリスタル遺跡（地下配置）- 正常動作
  - featureベースの装飾システム（structure systemではない）
  - **改善が必要**: 地表配置の信頼性とサイズ

#### ワールド生成システム
- クリスタル鉱石システム（通常・深層岩バリアント）
- 洞窟装飾ブロック（クリスタル鍾乳石・光る苔）
- **Crystalline Cavesバイオーム統合**（Terrablender 4.1.0.3使用）
- ルートテーブルシステム（Minecraft 1.21.1対応）
- ブロックタグシステム（適正ツール要求）
- 高度範囲指定による鉱石生成制御
- **Terrablender統合**:
  - カスタムワールドプリセット不要
  - バニラワールドタイプへの自動統合
  - 他MODとの互換性を保持

## 今後の機能拡張計画

### 🔄 短期計画（1-2週間）

#### レシピシステム
- [x] クリスタルブロックのクラフトレシピ（Minecraft 1.21.1対応完了）
- [x] **精錬レシピ**（2025年9月完了）
- [ ] カスタムレシピタイプ

#### アイテム追加
- [x] クリスタルの欠片（アイテム）- 2025年9月実装完了
- [x] クリスタルツール（剣、ピッケル、斧、シャベル、クワ）- 2025年9月実装完了
- [x] クリスタル防具（ヘルメット、チェストプレート、レギンス、ブーツ）- 2025年9月実装完了

#### ワールド生成強化
- [x] クリスタル鉱石（通常・深層岩） - 2025年9月実装完了
- [x] 洞窟装飾（クリスタル鍾乳石・光る苔） - 2025年9月実装完了
- [x] 適正ツール設定（ダイヤモンド以上要求） - 2025年9月実装完了
- [⚠️] **カスタム構造物**（2025年9月部分実装・要改善）
  - 地表のクリスタル神殿の配置改善が必要
  - より確実で視覚的に魅力的な構造物実装

## 🎯 次回優先タスク（パターンBフェーズ1完了後）

### 1. 地表構造物の改善（優先度: 高）
- **問題**: 現在の地表配置が限定的で見つけにくい
- **解決策候補**:
  1. `.nbt`テンプレートを使った真の構造物システム
  2. より効果的なfeature配置設定
  3. 目立つ装飾ブロック（柱、台座など）の追加
- **推定工数**: 4-6時間

### 2. 次フェーズ（パターンB）への移行
- **フェーズ2**: クリスタル鉱石実装 → 洞窟装飾
- **フェーズ3**: クリスタル防具 + 特殊効果アイテム
- **フェーズ4**: カスタムレシピタイプ

## 📋 短期開発計画詳細（並行開発用）

### 1. レシピシステム強化 🍳

#### 1.1 精錬レシピ（優先度: 高、競合リスク: 低）✅**完了**
- **実装工数**: 2時間（2025年9月完了）
- **技術要件**: Minecraft 1.21.1対応の精錬レシピフォーマット
- **実装済み内容**:
  - クリスタルの欠片精錬レシピ（furnace & blast_furnace）
  - 将来のクリスタル鉱石対応レシピ
  - 対応するアドバンスメントファイル（misc カテゴリ）
- **実装ファイル**:
  ```
  common/src/main/resources/data/worldgentest/recipe/
  ├── smelting_crystal_shard.json
  ├── blasting_crystal_shard.json
  ├── smelting_crystal_ore.json
  └── blasting_crystal_ore.json
  common/src/main/resources/data/worldgentest/advancement/recipe/misc/
  ├── smelting_crystal_shard.json
  └── blasting_crystal_shard.json
  ```

#### 1.2 カスタムレシピタイプ（優先度: 中、競合リスク: 中）
- **推定工数**: 8-12時間
- **技術要件**: カスタムRecipeTypeとRecipeSerializerの実装
- **実装内容**:
  - クリスタル加工台用のレシピタイプ
  - プラットフォーム固有の登録システム
- **ファイル構成**:
  ```
  common/src/main/java/com/example/worldgentest/recipe/
  ├── CrystalProcessingRecipe.java
  ├── CrystalProcessingRecipeType.java
  └── CrystalProcessingRecipeSerializer.java
  fabric/src/main/java/com/example/worldgentest/FabricModRecipes.java
  neoforge/src/main/java/com/example/worldgentest/ModRecipes.java
  ```
- **注意**: 他のレシピシステムとの競合可能性があるため、基本実装完了後に実行推奨

### 2. アイテム追加機能 ⚔️

#### 2.1 クリスタル防具（✅ 2025年9月完了）
- **実装工数**: 6-8時間（完了）
- **技術要件**: カスタム防具材料とプラットフォーム固有実装
- **実装内容**:
  - 4種類の防具（ヘルメット、チェストプレート、レギンス、ブーツ）
  - カスタム防具材料（ModArmorMaterials）
  - プラットフォーム固有の材料実装（Fabric/NeoForge）
  - ダイヤモンド+0.5の防御力設定
  - 完全なレシピ・アドバンスメント統合
- **実装ファイル**:
  ```
  common/src/main/java/com/example/worldgentest/ModArmorMaterials.java
  fabric/src/main/java/com/example/worldgentest/FabricModItems.java（防具追加）
  neoforge/src/main/java/com/example/worldgentest/ModBlocks.java（防具追加）
  common/src/main/resources/assets/worldgentest/textures/item/
  ├── crystal_helmet.png
  ├── crystal_chestplate.png
  ├── crystal_leggings.png
  └── crystal_boots.png
  common/src/main/resources/assets/worldgentest/textures/models/armor/
  ├── crystal_layer_1.png
  └── crystal_layer_2.png
  common/src/main/resources/data/worldgentest/recipe/
  ├── crystal_helmet.json
  ├── crystal_chestplate.json
  ├── crystal_leggings.json
  └── crystal_boots.json
  ```

#### 2.2 特殊効果アイテム（❌ 実装取り消し - 2025年9月）
- **状態**: 複雑化回避のため実装取り消し
- **理由**: シンプルなMod構成を維持するため、ユーザー判断で削除
- **削除されたアイテム**:
  - クリスタルポーション（一時的効果）
  - クリスタルアミュレット（常時効果）

### 3. ワールド生成強化 🌍

#### 3.0 カスタム構造物（優先度: 高、競合リスク: 中）⚠️**部分完了・要改善**
- **実装工数**: 6時間（2025年9月部分完了）
- **技術要件**: Feature-based装飾システム
- **実装済み内容**:
  - 地表クリスタル神殿（limited functionality）
  - 地下古代クリスタル遺跡（正常動作）
  - 既存バイオーム（Crystalline Caves）への統合
- **実装ファイル**:
  ```
  common/src/main/resources/data/worldgentest/worldgen/
  ├── configured_feature/
  │   ├── crystal_shrine_feature.json
  │   └── ancient_ruins_feature.json
  └── placed_feature/
      ├── crystal_shrine_placed.json
      └── ancient_ruins_placed.json
  ```
- **要改善点**:
  - 地表配置の信頼性向上
  - より視覚的に魅力的な構造
  - 真の構造物システム(.nbt)への移行検討

#### 3.1 クリスタル鉱石（優先度: 高、競合リスク: 高）
- **推定工数**: 8-10時間
- **技術要件**: カスタムブロックとワールド生成統合
- **実装内容**:
  - 通常クリスタル鉱石（石の中）
  - 深層クリスタル鉱石（深層岩の中）
  - ワールド生成での配置ルール
- **ファイル構成**:
  ```
  common/src/main/java/com/example/worldgentest/
  ├── ModBlocks.java（鉱石ブロック追加）
  └── ModItems.java（鉱石アイテム追加）
  common/src/main/resources/assets/worldgentest/textures/block/
  ├── crystal_ore.png
  └── deepslate_crystal_ore.png
  common/src/main/resources/data/worldgentest/worldgen/
  └── placed_feature/crystal_ore_placed.json
  ```
- **注意**: 3.2と同時実行不可（ワールド生成システムの競合）

#### 3.2 洞窟装飾（優先度: 中、競合リスク: 高）
- **推定工数**: 6-8時間
- **実装内容**:
  - クリスタルの鍾乳石
  - 光る苔
  - 小さなクリスタル結晶
- **注意**: 3.1完了後に実行（ワールド生成システムの競合）

#### 3.3 カスタム構造物（優先度: 低、競合リスク: 中）
- **推定工数**: 12-16時間
- **技術要件**: Structure generation system
- **実装内容**:
  - 小さなクリスタル神殿
  - 古代クリスタル遺跡
- **並行開発**: 1.1、2.1、2.2と並行可能

### 🎯 推奨並行開発パターン

#### パターンA: アイテム重視（✅ フェーズ1完了）
```
✅ フェーズ1（並行）: 1.1 精錬レシピ + 2.1 クリスタル防具 [8-11時間] - 2.1完了、1.1未実装
❌ フェーズ2（並行）: 2.2 特殊効果アイテム + 3.3 カスタム構造物 [16-22時間] - 2.2削除、3.3未実装
⏳ フェーズ3（順次）: 1.2 カスタムレシピタイプ [8-12時間] - 未実装
⏳ フェーズ4（順次）: 3.1 クリスタル鉱石 → 3.2 洞窟装飾 [14-18時間] - 未実装
```

#### パターンB: ワールド生成重視（🎯 推奨継続ルート）
```
⏳ フェーズ1（並行）: 1.1 精錬レシピ + 3.3 カスタム構造物 [14-19時間] - 未実装
⏳ フェーズ2（順次）: 3.1 クリスタル鉱石 → 3.2 洞窟装飾 [14-18時間] - 未実装
✅ フェーズ3（並行）: 2.1 クリスタル防具 + 2.2 特殊効果アイテム [10-14時間] - 2.1完了、2.2削除
⏳ フェーズ4（順次）: 1.2 カスタムレシピタイプ [8-12時間] - 未実装
```

### ⚠️ 競合回避ルール

1. **同一システム変更の回避**
   - ワールド生成関連（3.1 ↔ 3.2）は順次実行
   - レシピシステム拡張（1.2）は他のレシピ作業完了後

2. **プラットフォーム固有ファイルの調整**
   - 同じプラットフォーム固有ファイルを変更する場合は事前調整
   - 例: FabricModItems.java を複数機能で同時変更する場合

3. **テスト段階での統合**
   - 各機能完了後は必ず個別テスト
   - 複数機能完了後は統合テスト実施

4. **並行開発時のビルド方針**
   - **重要**: 並行開発中は `runClient` を実行せず、`build` のみで動作確認
   - 理由: 複数のクライアント同時起動によるマシン負荷を回避
   - コマンド例:
     ```bash
     ./gradlew build                    # 全体ビルド確認
     ./gradlew fabric:build            # Fabricプラットフォームのみ
     ./gradlew neoforge:build          # NeoForgeプラットフォームのみ
     ```
   - クライアントでの動作確認は各機能完了後にユーザーが手動で実行
   - 並行開発完了後の統合テスト時に初めて `runClient` を使用

### 🎯 中期計画（1-2ヶ月）

#### エンティティ
- [x] クリスタルゴーレム（友好Mob）- 完了
- [ ] クリスタルスパイダー（敵対Mob）
- [x] カスタムエンティティレンダリング - 完了

#### 高度なワールド生成
- [ ] マルチバイオーム構造
- [ ] 地下都市生成
- [ ] ディメンション追加

#### GUI・ユーザーインターフェース
- [ ] クリスタル加工台（カスタムGUI）
- [ ] エナジーシステム
- [ ] 進捗システム

### 🚀 長期計画（3ヶ月以上）

#### 魔法システム
- [ ] クリスタルベースの魔法
- [ ] 呪文システム
- [ ] マナシステム

#### ネットワーク機能
- [ ] サーバー間通信
- [ ] プレイヤーデータ同期
- [ ] マルチプレイヤー対応強化

#### MOD連携
- [ ] JEI連携
- [ ] Curios API連携
- [ ] Applied Energistics連携

## 開発時の注意事項

### Minecraft 1.21.1での重要な変更点（2025年9月確認）
1. **リソースディレクトリ構造変更**:
   - `advancements` → `advancement` (単数形)
   - `recipes` → `recipe` (単数形)
   - この変更により従来の複数形ディレクトリは認識されない
2. **レシピフォーマット変更**:
   - 旧: `"G": {"item": "minecraft:glass"}`
   - 新: `"G": "minecraft:glass"` (簡潔な文字列形式)
3. **アドバンスメントフォーマット変更**:
   - 旧: `"items": ["minecraft:diamond"]` (配列)
   - 新: `"items": "minecraft:diamond"` (文字列)
4. **レシピカテゴリとアドバンスメントディレクトリは別概念**:
   - レシピのcategoryフィールド: `misc`, `building_blocks`, `decorations`等
   - アドバンスメントディレクトリ: `recipe/decorations/`, `recipe/food/`等
5. **Modレシピには必ずアドバンスメントを追加**:
   - Minecraftの自動発見システムはバニラレシピ（`minecraft:` namespace）に最適化
   - カスタムnamespace（例：`worldgentest:`）のレシピは自動発見されない
   - レシピブック表示のため、Modレシピには対応するアドバンスメントファイルが必須
   - アドバンスメントなしのModレシピは材料入手時にレシピブックに表示されない

### Architecturyでの開発
1. **共通コード**: できるだけcommonモジュールに実装
2. **プラットフォーム固有**: 必要最小限に留める
3. **@ExpectPlatform**: プラットフォーム固有の処理で使用
4. **registries**: Architecturyの登録システムを活用
5. **リソース管理の重要原則**:
   - **commonのリソースは自動共有**: `common/src/main/resources/`内のassetsとdataは、ビルド時に自動的にfabricとneoforgeにコピーされる
   - **プラットフォーム固有リソースのみ個別配置**: プラットフォーム間で異なる必要がある場合のみ、`fabric/src/main/resources/`や`neoforge/src/main/resources/`に配置
   - **重複リソースの回避**: 同じリソースをcommonとプラットフォーム固有の両方に置くと競合が発生する可能性がある
   - **ベストプラクティス**:
     - テクスチャ、モデル、言語ファイルなど共通リソースは`common/src/main/resources/`に配置
     - プラットフォーム固有の設定ファイル（mod.jsonなど）のみプラットフォーム側に配置
     - リソース変更が必要な場合のみプラットフォーム側にオーバーライド用ファイルを配置

### バージョン管理
1. **依存関係**: 定期的にバージョンアップデート
2. **Minecraft**: 新バージョンへの対応計画
3. **互換性**: 下位バージョンとの互換性検討

### パフォーマンス
1. **クライアント負荷**: レンダリング処理の最適化
2. **サーバー負荷**: ワールド生成の効率化
3. **メモリ使用量**: 不要なオブジェクト生成を避ける

### セキュリティ
1. **入力検証**: ユーザー入力は必ず検証
2. **権限管理**: 管理者機能の適切な制限
3. **データ保護**: プレイヤーデータの安全な処理

## 学習ポイント

このプロジェクトで学習・検証する技術領域：

### Minecraft Modding
- バイオーム生成とカスタマイズ
- ブロック・アイテムの実装パターン
- エンティティ作成とAI
- ワールド生成アルゴリズム

### Architectury Framework
- マルチプラットフォーム開発
- 共通コードとプラットフォーム固有コードの分離
- レジストリシステムの活用
- リソース管理

### ソフトウェア設計
- モジュラーアーキテクチャ
- プラグインシステム
- 設定管理
- テスト駆動開発

## リソース・参考資料

### 公式ドキュメント
- [Architectury Documentation](https://docs.architectury.dev/)
- [NeoForge Documentation](https://docs.neoforged.net/)
- [Minecraft Wiki](https://minecraft.wiki/)

### コミュニティ
- [Architectury Discord](https://discord.gg/architectury)
- [ModdingByKaupenjoe](https://courses.kaupenjoe.net/)
- [r/feedthebeast](https://reddit.com/r/feedthebeast)

### 開発ツール
- IntelliJ IDEA（推奨IDE）
- MCreator（ビジュアル開発）
- Blockbench（モデル作成）

## コミット・リリース方針

### コミットメッセージ
Conventional Commitsに従う：
- `feat:` 新機能追加
- `fix:` バグ修正
- `docs:` ドキュメント更新
- `refactor:` リファクタリング
- `test:` テスト追加・修正

### バージョニング
Semantic Versioningに従う：
- MAJOR: 非互換性のある変更
- MINOR: 後方互換性のある機能追加
- PATCH: 後方互換性のあるバグ修正

### リリースノート
各リリースで以下を記載：
- 新機能
- バグ修正
- 破壊的変更
- 既知の問題

## 開発時のトラブルシューティング・学習事項

### エンティティ登録でのmapping問題（2025年9月）

#### 問題
- Minecraft 1.21.1 + Architectury + Fabric環境でエンティティ登録時にClassNotFoundError発生
- 具体的エラー：`java.lang.ClassNotFoundException: net.minecraft.class_2378`
- `net.minecraft.class_1429`（ResourceLocation関連）のmapping問題も発生

#### 原因と対策
1. **開発環境mappingの不整合**
   - 開発時とランタイムでのmapping差異が原因
   - commonモジュールでのエンティティ登録がplatform-specificなクラスにアクセスしようとしている

2. **Platform-specificコードの分離不足**
   - エンティティ登録はプラットフォーム固有処理として扱う必要がある
   - `FabricModEntities.java`として分離したが、まだmapping問題が残存

3. **ResourceLocation使用方法の変更**
   - 旧: `new ResourceLocation(MOD_ID, "name")`
   - 新: `ResourceLocation.parse(MOD_ID + ":name")`
   - コンストラクタがprivateアクセスになった影響

#### 一時的対応
- エンティティ登録を完全にコメントアウトして基本機能（ブロック・バイオーム）の動作確認を優先
- 基本機能が正常動作することを確認してからエンティティ機能の再実装を検討

#### 今後の対応方針
1. **より詳細なmapping調査**
   - 開発環境とランタイム環境でのmapping差異を詳しく調査
   - 正しいmapped nameの使用を確認

2. **代替アプローチの検討**
   - Architecturyの `@ExpectPlatform` パターンの使用
   - より確実なプラットフォーム分離方法の採用

3. **段階的実装**
   - まず基本機能（ブロック・バイオーム・レシピ）を完全に動作させる
   - その後、安定した方法でエンティティ機能を追加

#### 学習ポイント
- Minecraft ModのEntity実装は最も複雑な部分の一つ
- Architecturyでのmulti-platform開発では、platform-specificな処理の適切な分離が重要
- 開発環境特有の問題と実際のバグを区別して対応することが重要

### エンティティ実装の成功パターン（2025年9月解決）

#### 解決した実装アプローチ
1. **プラットフォーム完全分離方式の採用**
   - commonモジュールでエンティティクラス、モデル、レンダラーを実装
   - 各プラットフォーム（fabric/neoforge）で独立したエンティティ登録システムを構築
   - プラットフォーム固有の属性登録とクライアントレンダリング登録を実装

2. **NeoForge固有の実装要件**
   - `EntityAttributeCreationEvent`でのエンティティ属性登録が必須
   - `EntityRenderersEvent.RegisterLayerDefinitions`でのモデルレイヤー登録が必要
   - `EntityRenderersEvent.RegisterRenderers`でのレンダラー登録
   - 適切なEventBusSubscriber設定とクライアント専用アノテーション（@Dist.CLIENT）

3. **リソース統合の実践**
   - エンティティテクスチャをcommon/resources/assetsに統合
   - 重複ファイルの削除によるビルドエラー回避
   - Architecturyの自動リソース共有機能の活用

#### 成功要因
- プラットフォーム固有の登録システムを理解し、それぞれに適した実装方法を採用
- Architecturyの自動リソース共有を活用してコード重複を最小化
- 段階的なテストとエラー解析による問題の特定と解決

### クリスタルツール実装の成功パターン（2025年9月完了）

#### 実装アプローチ
1. **プラットフォーム別ツール実装**
   - **Fabric**: カスタムToolMaterialクラスでgetInverseTag()メソッド実装
   - **NeoForge**: 共通ModToolTiers（Tierインターフェース）で標準コンストラクタ使用
   - **共通**: ModToolTiersクラスでツール性能統一管理

2. **テクスチャ作成手法**
   - Minecraftの既存ダイヤモンドツールテクスチャをベースに加工
   - Pythonを使った自動色調変換で効率的なテクスチャ生成
   - クリスタルの欠片・ブロックと統一された色調で識別性向上

3. **レシピシステム統合**
   - Minecraft 1.21.1の新フォーマット対応
   - カテゴリ別アドバンスメント（equipment）でレシピブック連携
   - プラットフォーム間共通リソースによる一元管理

#### 技術的学習ポイント
- **プラットフォーム差異**: FabricとNeoForgeでのツールコンストラクタ仕様の違い
- **リソース効率化**: Python PIL活用による既存アセット再利用手法
- **ユーザビリティ**: ダイヤモンドツールとの視覚的差別化の重要性
- **システム統合**: レシピ、アドバンスメント、クリエイティブタブの一括対応

#### 実装規模
- 5種類のツール（剣、ピッケル、斧、シャベル、クワ）
- 2プラットフォーム対応（Fabric、NeoForge）
- 5レシピ + 5アドバンスメント
- 多言語対応（英語・日本語）

### ワールド生成・ルートテーブル実装の成功パターン（2025年9月完了）

#### 実装アプローチ
1. **フェーズ2完全実装**
   - **クリスタル鉱石システム**: 通常・深層岩バリアント対応
   - **洞窟装飾**: クリスタル鍾乳石・光る苔の生成統合
   - **ワールド生成**: configured_feature/placed_featureシステム活用
   - **ルートテーブル**: Minecraft 1.21.1完全対応

2. **Minecraft 1.21.1重要仕様変更対応**
   - **ディレクトリ名変更**: `loot_tables` → `loot_table`（単数形）
   - **ルートテーブルフォーマット**: 新形式JSON構造対応
   - **ブロックタグシステム**: 適正ツール要求の実装

3. **適正ツール設定の完全実装**
   - **ブロックタグ**: `minecraft:mineable/pickaxe`、`minecraft:needs_diamond_tool`
   - **ツールレベル**: `getIncorrectBlocksForDrops()`でダイヤモンドレベル認識
   - **プラットフォーム統一**: Fabric/NeoForge両対応

#### 技術的学習ポイント
- **Minecraft 1.21.1仕様変更**: ディレクトリ名とJSONフォーマットの重要な変更
- **ブロックタグシステム**: `requiresCorrectToolForDrops()`の正しい動作要件
- **ツールレベル認識**: `getIncorrectBlocksForDrops()`によるレベル制御方法
- **ワールド生成統合**: バイオーム・feature・ルートテーブルの連携実装
- **デバッグ手法**: 段階的テストとシステム的問題解析の重要性

#### 実装規模
- 4種類のブロック（鉱石2種・装飾2種）
- 2プラットフォーム対応（Fabric、NeoForge）
- ワールド生成統合（バイオーム・feature・配置）
- ルートテーブル + ブロックタグシステム
- 適正ツール設定（ダイヤモンド以上要求）

### クリエイティブタブ統合の重要性（2025年9月学習）

#### 問題発見
- **新規ブロック追加時の見落とし**: Crystal Stone、Crystal Cobblestone、Crystal Bricksがクリエイティブタブに表示されない
- **ユーザビリティ問題**: プレイヤーがクリエイティブモードで新ブロックにアクセスできない状況

#### 学習ポイント
1. **プラットフォーム別クリエイティブタブ管理**
   - **Fabric**: `FabricModCreativeTabs.java`での`.entries()`メソッド使用
   - **NeoForge**: `ModCreativeTabs.java`での`.displayItems()`メソッド使用
   - 両プラットフォームで同期的な更新が必要

2. **アイテム追加のベストプラクティス**
   - **論理的グループ化**: ブロック → アイテム → ツール → 防具の順序維持
   - **新規ブロック追加時の必須チェック**: ブロック定義 → クリエイティブタブ統合 → テスト
   - **タスク管理の重要性**: 実装タスクリストにクリエイティブタブ更新を含める

3. **開発プロセス改善**
   - **新機能実装チェックリスト**: 登録 → モデル → レシピ → **クリエイティブタブ** → テスト
   - **両プラットフォーム同期**: 一方のプラットフォームで実装したら、もう一方も即座に更新

#### 実装詳細
- **追加されたアイテム**: CRYSTAL_STONE_ITEM、CRYSTAL_COBBLESTONE_ITEM、CRYSTAL_BRICKS_ITEM
- **ファイル更新**: 2プラットフォーム対応（Fabric、NeoForge）
- **タスクドキュメント更新**: T025タスクとしてタスクリストに記録

### ブロックタグとルートテーブルの正しい実装（2025年9月学習）

#### 問題発見
- **Crystal Stoneが正しくドロップしない**: ダイヤのツルハシで採掘しても自分自身がドロップし、crystal_cobblestoneがドロップしない
- **全ブロックがドロップしない状態**: ブロックタグのディレクトリ構造が間違っていた
- **ルートテーブルの構造が不完全**: バニラと異なる構造で正常に機能しなかった

#### 学習ポイント

##### 1. Minecraft 1.21.1のディレクトリ構造（重要）
- **正しい**: `data/minecraft/tags/block/` （単数形）
- **間違い**: `data/minecraft/tags/blocks/` （複数形）
- **正しい**: `data/minecraft/tags/block/mineable/pickaxe.json`
- **間違い**: `data/minecraft/tags/block/mineable_with_pickaxe.json`

##### 2. ブロックタグシステムの階層
```
data/minecraft/tags/block/
├── mineable/
│   └── pickaxe.json          # ピッケルで採掘可能なブロック
├── needs_stone_tool.json     # 石ツール以上が必要
├── needs_iron_tool.json      # 鉄ツール以上が必要
└── needs_diamond_tool.json   # ダイヤツール以上が必要
```

##### 3. ツールレベル要求の設定
- **`requiresCorrectToolForDrops()`**: Javaコードで設定
- **ブロックタグ**: データパックで「正しいツール」を定義
- **両方が必要**: どちらか一方だけでは機能しない

##### 4. バニラ準拠のルートテーブル構造
```json
{
  "type": "minecraft:block",
  "pools": [
    {
      "bonus_rolls": 0.0,
      "conditions": [                    // ← pool全体の条件
        {
          "condition": "minecraft:survives_explosion"
        }
      ],
      "entries": [...],
      "rolls": 1.0
    }
  ],
  "random_sequence": "namespace:blocks/block_name"  // ← 必須（1.19.3+）
}
```

##### 5. Crystal Stoneのような「別アイテムをドロップ」するブロック
```json
{
  "type": "minecraft:alternatives",
  "children": [
    {
      "type": "minecraft:item",
      "conditions": [                    // ← entry個別の条件
        {
          "condition": "minecraft:match_tool",
          "predicate": {
            "predicates": {              // ← predicatesの入れ子構造
              "minecraft:enchantments": [
                {
                  "enchantments": "minecraft:silk_touch",
                  "levels": {"min": 1}
                }
              ]
            }
          }
        }
      ],
      "name": "worldgentest:crystal_stone"
    },
    {
      "type": "minecraft:item",
      "conditions": [                    // ← cobblestone側の条件
        {
          "condition": "minecraft:survives_explosion"
        }
      ],
      "name": "worldgentest:crystal_cobblestone"
    }
  ]
}
```

#### 重要な発見
1. **`survives_explosion`の配置**
   - 単純なブロック: `pools[0].conditions`に配置
   - alternativesブロック: 両方の場所に必要（poolとentry両方）

2. **`random_sequence`の重要性**
   - Minecraft 1.19.3以降で標準
   - 一貫した乱数生成を保証
   - デバッグ性とマルチプレイヤー同期の改善

3. **シルクタッチの構造変化**
   - 旧: `"enchantments": [...]`
   - 新: `"predicates": {"minecraft:enchantments": [...]}`
   - Minecraft 1.21.1での正式な構造

#### トラブルシューティングプロセス
1. **ブロックがドロップしない** → ブロックタグのディレクトリ確認
2. **間違ったアイテムがドロップ** → ルートテーブルの条件とalternatives構造確認
3. **バニラと比較** → 常に最新バージョンのバニラ実装を参照

#### 実装詳細
- **修正ファイル**: 7ファイル（ブロック定義、タグ、ルートテーブル）
- **プラットフォーム**: Fabric/NeoForge両対応
- **動作**: バニラの石系ブロックと完全に同じ動作を実現

### バニラレシピ統合の実装パターン（2025年10月学習）

#### 問題：石切台レシピへのクリスタル石の統合

**要件：**
- バニラの石でも、クリスタル石でも石切台を作成可能にする
- かまどはクリスタル丸石でも作成可能（丸石系との互換性）
- レシピは可能な限り少なく保つ

**試行錯誤の経緯：**
1. **MOD側で別レシピ作成** → レシピが2つになる ❌
2. **バニラレシピオーバーライド試行** → レシピが効かない ❌
3. **タグを使ったMODレシピ追加** → 最終的に成功 ✓

#### 学習ポイント

##### 1. バニラレシピのタグ使用状況の確認が重要

**かまど（成功例）：**
```json
// data/minecraft/recipe/furnace.json（バニラ）
"key": {
  "#": {
    "tag": "minecraft:stone_crafting_materials"
  }
}
```
- バニラですでにタグを使用 → タグに追加するだけでOK
- `stone_crafting_materials` タグに `crystal_cobblestone` を追加すれば動作

**石切台（複雑な例）：**
```json
// data/minecraft/recipe/stonecutter.json（バニラ）
"key": {
  "#": {
    "item": "minecraft:stone"
  }
}
```
- バニラは特定のアイテムを直接指定 → タグではない
- レシピオーバーライドを試みたが、バニラレシピが優先される問題

##### 2. バニラタグの内容の正確な把握

**`minecraft:stone_crafting_materials`（かまど用）：**
```json
{
  "values": [
    "minecraft:cobblestone",
    "minecraft:blackstone",
    "minecraft:cobbled_deepslate"
  ]
}
```
- 丸石系のみ、**通常の石は含まれない**

**`minecraft:stone_tool_materials`（ツール作成用）：**
```json
{
  "values": [
    "minecraft:cobblestone",
    "minecraft:blackstone",
    "minecraft:cobbled_deepslate"
  ]
}
```
- 同じく丸石系のみ
- 石切台レシピとは無関係（名前に惑わされない）

##### 3. 解決策：MOD専用タグの作成

**問題：**
既存のMinecraftタグは丸石系を含むため、石切台レシピに使えない

**解決策：**
MOD専用のタグ `worldgentest:stone_blocks` を作成
```json
// data/worldgentest/tags/item/stone_blocks.json
{
  "replace": false,
  "values": [
    "minecraft:stone",
    "worldgentest:crystal_stone"
  ]
}
```

**MODレシピでこのタグを使用：**
```json
// data/worldgentest/recipe/stonecutter_from_crystal_stone.json
{
  "type": "minecraft:crafting_shaped",
  "key": {
    "#": {
      "tag": "worldgentest:stone_blocks"
    },
    "I": {
      "item": "minecraft:iron_ingot"
    }
  },
  "result": {
    "id": "minecraft:stonecutter"
  }
}
```

##### 4. バニラレシピオーバーライドの制約

**試みた方法：**
- `data/minecraft/recipe/stonecutter.json` を作成してタグ使用に変更
- ビルド出力では正しく生成されている

**結果：**
- ゲーム内では効果なし
- バニラレシピがMODのオーバーライドより優先される

**教訓：**
- バニラレシピのオーバーライドは信頼性が低い
- 他のMODとの競合リスクもある
- MOD側で追加レシピを作る方が安全

##### 5. レシピフォーマット（Minecraft 1.21.1）

**タグ指定（オブジェクト形式）：**
```json
"key": {
  "#": {
    "tag": "namespace:tag_name"
  }
}
```

**アイテム指定（オブジェクト形式）：**
```json
"key": {
  "#": {
    "item": "namespace:item_name"
  }
}
```

**間違った形式（文字列）：**
```json
"key": {
  "#": "#namespace:tag_name"  // ❌ 動作しない
}
```

#### 最終実装パターン

**ファイル構成：**
```
data/
├── minecraft/tags/item/
│   └── stone_crafting_materials.json  # かまど用（丸石系に追加）
└── worldgentest/
    ├── tags/item/
    │   └── stone_blocks.json          # 石切台用（石系のみ）
    ├── recipe/
    │   └── stonecutter_from_crystal_stone.json
    └── advancement/recipe/misc/
        └── stonecutter_from_crystal_stone.json
```

**結果：**
- **かまどレシピ1つ**：丸石、深層岩の丸石、クリスタル丸石で作成可能
- **石切台レシピ2つ**：
  1. バニラレシピ（通常の石のみ）
  2. MODレシピ（通常の石、クリスタル石）
- レシピブック統合：正しくアドバンスメントで解放

#### ベストプラクティス

1. **バニラレシピの調査を最優先**
   - バニラのレシピファイルを必ず確認
   - タグを使っているか、アイテム直接指定か
   - 使用しているタグの内容を確認

2. **タグの内容を正確に把握**
   - タグ名から推測せず、実際の内容を確認
   - バニラタグと同名のタグを作る場合は `replace: false` を使用

3. **MOD専用タグの活用**
   - 既存タグが要件に合わない場合は、MOD専用タグを作成
   - 拡張性を考慮した命名（例：`stone_blocks` は将来的に他の石系ブロックも追加可能）

4. **バニラレシピオーバーライドは避ける**
   - 動作が不確実
   - 他のMODとの競合リスク
   - MOD側で追加レシピを作る方が安全

5. **レシピが複数になることを許容**
   - バニラとMODで別レシピになっても、ユーザビリティに大きな問題はない
   - 無理にオーバーライドするより安全性を優先

#### トラブルシューティングプロセス

1. **バニラレシピの確認**
   ```bash
   jar xf minecraft.jar data/minecraft/recipe/furnace.json
   ```

2. **バニラタグの確認**
   ```bash
   jar xf minecraft.jar data/minecraft/tags/item/stone_crafting_materials.json
   ```

3. **ビルド出力の確認**
   ```bash
   cat fabric/build/resources/main/data/minecraft/recipe/stonecutter.json
   ```

4. **段階的テスト**
   - レシピファイルが正しくビルドされているか
   - タグファイルが正しくビルドされているか
   - ゲーム内で正しく動作するか

#### 実装詳細
- **追加ファイル**: 3ファイル（タグ、レシピ、アドバンスメント）
- **プラットフォーム**: Fabric/NeoForge両対応
- **動作**: バニラとMODのレシピが共存、互換性を保つ

### Terrablenderによるバイオーム統合の成功パターン（2025年10月完了）

#### 問題：巨大なバニラパラメータファイルによる保守性の問題

**背景：**
- 当初、カスタムワールドプリセットでバイオーム統合を試みた
- `multi_noise_biome_source_parameter_list/overworld_caves.json`に7593エントリーの巨大ファイルを生成
- ファイルサイズ3.3MB、メンテナンス不可能
- ゲーム起動時にクラッシュが発生

**試行錯誤の経緯：**
1. **カスタムワールドプリセット作成** → `/locate biome`でバイオームが見つからない
2. **バニラパラメータ全コピー** → 巨大ファイルでクラッシュ、メンテナンス不可能
3. **Terrablenderライブラリ採用** → 最終的に成功 ✓

#### 学習ポイント

##### 1. Terrablenderの利点

**保守性：**
- コード量：100行程度のRegionクラスのみ
- 設定ファイル：不要（プログラマティックに定義）
- バニラ互換性：Terrablenderが自動管理

**互換性：**
- 他MODとの競合自動解決
- バニラワールドタイプに自動統合
- カスタムワールドプリセット不要

**プラットフォーム対応：**
- Fabric/NeoForge両対応
- プラットフォーム固有のmapping差異を適切に処理

##### 2. プラットフォーム別実装パターン

**Common モジュール：**
```java
// CrystallineCavesRegion.java (Mojang mapping for NeoForge)
public class CrystallineCavesRegion extends Region {
    public CrystallineCavesRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry,
                         Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        this.addBiome(mapper,
            Climate.parameters(
                Climate.Parameter.span(-1.0f, 1.0f),  // temperature
                Climate.Parameter.span(-1.0f, 1.0f),  // humidity
                Climate.Parameter.span(-1.0f, 1.0f),  // continentalness
                Climate.Parameter.span(-1.0f, 1.0f),  // erosion
                Climate.Parameter.span(0.2f, 1.0f),   // depth: underground only
                Climate.Parameter.span(-1.0f, 1.0f),  // weirdness
                0.0f                                   // offset
            ),
            CRYSTALLINE_CAVES
        );
    }
}
```

**Fabric モジュール（Yarn mapping対応）：**
```java
// FabricCrystallineCavesRegion.java
public class FabricCrystallineCavesRegion extends Region {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public FabricCrystallineCavesRegion(Identifier name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addBiomes(Registry registry, Consumer mapper) {
        this.addBiome(mapper,
            new MultiNoiseUtil.NoiseHypercube(
                MultiNoiseUtil.ParameterRange.of(-1.0f, 1.0f),  // temperature
                MultiNoiseUtil.ParameterRange.of(-1.0f, 1.0f),  // humidity
                MultiNoiseUtil.ParameterRange.of(-1.0f, 1.0f),  // continentalness
                MultiNoiseUtil.ParameterRange.of(-1.0f, 1.0f),  // erosion
                MultiNoiseUtil.ParameterRange.of(0.2f, 1.0f),   // depth
                MultiNoiseUtil.ParameterRange.of(-1.0f, 1.0f),  // weirdness
                0L                                               // offset (long!)
            ),
            CRYSTALLINE_CAVES
        );
    }
}
```

##### 3. Yarn/Mojang Mapping差異の解決

**主な差異：**
- `ResourceLocation` (Mojang) ↔ `Identifier` (Yarn)
- `Climate.ParameterPoint` (Mojang) ↔ `MultiNoiseUtil.NoiseHypercube` (Yarn)
- `Climate.Parameter.span()` (Mojang) ↔ `MultiNoiseUtil.ParameterRange.of()` (Yarn)
- offset型: `float` (Mojang) ↔ `long` (Yarn)

**解決策：**
- プラットフォーム別Regionクラスの作成
- `@SuppressWarnings`で型消去を許容
- 各プラットフォームのAPI仕様に完全準拠

##### 4. 依存関係設定

**gradle.properties：**
```properties
terrablender_version=4.1.0.3
```

**common/build.gradle.kts：**
```kotlin
dependencies {
    compileOnly("com.github.glitchfiend:TerraBlender-common:${minecraft_version}-${terrablender_version}")
}
```

**fabric/build.gradle.kts：**
```kotlin
dependencies {
    modImplementation("com.github.glitchfiend:TerraBlender-fabric:${minecraft_version}-${terrablender_version}")
}
```

**neoforge/build.gradle.kts：**
```kotlin
dependencies {
    implementation("com.github.glitchfiend:TerraBlender-neoforge:${minecraft_version}-${terrablender_version}")
}
```

##### 5. エントリーポイント設定

**Fabric（fabric.mod.json）：**
```json
"entrypoints": {
  "terrablender": [
    "com.example.worldgentest.FabricTerraBlenderInit"
  ]
}
```

**NeoForge（FMLCommonSetupEvent）：**
```java
public static void register(IEventBus modEventBus) {
    modEventBus.addListener(NeoForgeTerraBlenderInit::commonSetup);
}

private static void commonSetup(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
        Regions.register(new CrystallineCavesRegion(...));
    });
}
```

#### 技術的学習ポイント
- **バイオーム統合の2つのアプローチ**: データパック方式 vs ライブラリ方式
- **保守性の重要性**: 大規模JSONファイルの管理コストは非常に高い
- **業界標準の活用**: 実績あるライブラリの採用でトラブル回避
- **プラットフォーム差異の対処**: Yarn/Mojang mappingの違いを適切に処理
- **型システムの理解**: 型消去とジェネリクスの適切な使用

#### 実装規模
- **削除ファイル**: 3.3MBの巨大JSONファイル + カスタムワールドプリセット設定
- **追加コード**: 約200行（3つのRegionクラス + 2つの初期化クラス）
- **依存関係**: Terrablender 4.1.0.3
- **プラットフォーム**: Fabric/NeoForge両対応
- **動作**: バニラ「Default」ワールドタイプで自動統合、`/locate biome`で確認可能

#### ベストプラクティス

1. **バイオーム追加にはTerrablenderを使用**
   - カスタムワールドプリセットは保守性が低い
   - 巨大なパラメータファイルは避ける
   - ライブラリによる自動統合が最適

2. **プラットフォーム別実装の適切な分離**
   - commonモジュールにMojang mapping版を配置
   - Fabricモジュールに Yarn mapping版を配置
   - NeoForgeはcommon版を再利用

3. **mapping差異の確実な対処**
   - クラス名、メソッド名の違いを正確に把握
   - 型の違い（特にプリミティブ型）に注意
   - `@SuppressWarnings`を適切に使用

4. **段階的なトラブルシューティング**
   - まずビルドエラーを解決
   - 次にゲーム起動を確認
   - 最後に機能動作をテスト

#### トラブルシューティングプロセス

1. **バイオームが見つからない** → Terrablender登録の確認
2. **ビルドエラー** → mapping差異の確認
3. **クラッシュ** → 不要なカスタムワールドプリセットファイルの削除
4. **`/locate biome`で確認** → 地下バイオームは地上から探索が必要
