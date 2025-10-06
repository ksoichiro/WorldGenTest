# Research: クリスタル石ブロックシステム実装調査

**Date**: 2025-09-22
**Focus**: 既存WorldGenTestプロジェクトの構造分析とベストプラクティス調査

## 1. ブロック実装パターン

### Decision: プラットフォーム分離方式の採用
- **Implementation**: 共通ロジックは`common`モジュール、プラットフォーム固有の登録は各プラットフォームモジュールで実装
- **Registry方式**: NeoForgeは`DeferredRegister`、Fabricは`Registry.register`を使用

### Rationale
- Architecturyの設計思想に準拠し、コードの重複を最小化
- プラットフォーム固有の登録システムの違いを適切に分離
- 既存のModBlocks.javaパターンとの一貫性維持

### Alternatives Considered
- **@ExpectPlatformパターン**: 統一インターフェースでプラットフォーム差異を隠蔽（実装複雑性増大）
- **完全統合方式**: Architecturyの抽象化層のみ使用（プラットフォーム固有機能利用不可）

## 2. レシピシステム実装方法

### Decision: Minecraft 1.21.1新フォーマット完全対応
- **Directory Structure**: 単数形ディレクトリ（`recipe/`、`advancement/`）使用
- **Required Pattern**: Modレシピには必ず対応するアドバンスメントを作成
- **Category System**: `misc`、`decorations`、`equipment`でレシピブック表示制御

### Rationale
```json
// Minecraft 1.21.1対応の新フォーマット例
{
  "type": "minecraft:crafting_shaped",
  "category": "misc",
  "key": {
    "G": "minecraft:glass",
    "D": "minecraft:diamond"
  }
}
```
- バニラレシピ自動発見システムがModには適用されないため
- ユーザビリティ向上（レシピブック連携）が必須
- 既存実装での成功パターンを踏襲

### Alternatives Considered
- **JEI依存方式**: レシピブック表示をJEIに委託（MOD依存が発生）
- **旧フォーマット**: Minecraft 1.20.x以前の複数形ディレクトリ（非推奨、動作不可）

## 3. ワールド生成への統合方法

### Decision: 段階的配置システムの採用
- **Feature分離**: Configured Feature + Placed Feature分離管理
- **Biome Integration**: カスタムバイオーム（Crystalline Caves）での統合
- **Priority System**: 鉱石→装飾→構造物の優先順位配置

### Rationale
```json
// configured_feature例（クリスタル石生成）
{
  "type": "minecraft:ore",
  "config": {
    "size": 12,
    "targets": [
      {
        "target": { "predicate_type": "minecraft:tag_match", "tag": "minecraft:stone_ore_replaceables" },
        "state": { "Name": "worldgentest:crystal_stone" }
      }
    ]
  }
}
```
- Minecraftの標準ワールド生成システムとの統合性確保
- デバッグとチューニングの容易性
- 他MODとの競合回避、既存バイオームとの調和

### Alternatives Considered
- **カスタムディメンション方式**: 独立したディメンションで生成（複雑性増大、スコープ外）
- **イベントベース生成**: ワールド生成イベントでカスタム制御（パフォーマンス問題、メンテナンス困難）

## 4. テクスチャとモデルの管理パターン

### Decision: 統一アセット管理 + Python自動生成
- **Asset Management**: `common/src/main/resources/assets/`で全アセット一元管理
- **Texture Generation**: Pythonスクリプトでバニラ石テクスチャを白調に変換
- **Model Hierarchy**: `blockstates/` → `models/block/` → `models/item/`の明確な依存関係

### Rationale
```python
# テクスチャ生成例
def whiten_stone_texture(input_path, output_path, white_factor=0.7):
    # バニラ石テクスチャを白っぽく変換
    from PIL import Image, ImageEnhance

    img = Image.open(input_path)
    enhancer = ImageEnhance.Brightness(img)
    brightened = enhancer.enhance(1.3)

    # 彩度を下げて白っぽく
    enhancer = ImageEnhance.Color(brightened)
    whitened = enhancer.enhance(0.6)

    whitened.save(output_path)
```
- Architecturyの自動リソース共有機能を最大活用
- 開発効率化とアセット一貫性の確保
- バニラとの視覚的整合性維持

### Alternatives Considered
- **プラットフォーム別アセット**: 各プラットフォームで独立管理（重複とメンテナンス負荷）
- **動的テクスチャ生成**: ランタイムでの色調変換（パフォーマンス影響、複雑性）
- **手動テクスチャ作成**: 完全カスタム作成（時間コスト、一貫性リスク）

## 5. 多言語対応の実装パターン

### Decision: 統一言語ファイル + 名前空間規則
- **Language Files**: `common/src/main/resources/assets/worldgentest/lang/`で多言語管理
- **Naming Convention**: `block.worldgentest.*`、`item.worldgentest.*`の一貫した命名
- **Complete Coverage**: ワールド生成要素も含めた完全な多言語対応

### Rationale
```json
// 多言語対応例
{
  "block.worldgentest.crystal_stone": "Crystal Stone",
  "block.worldgentest.crystal_cobblestone": "Crystal Cobblestone",
  "block.worldgentest.crystal_bricks": "Crystal Bricks",
  "advancement.worldgentest.crystal_stone_craft": "Crystal Architecture"
}
```
- 国際化対応の必要性（グローバルユーザーベース）
- ユーザーエクスペリエンス向上
- 既存実装との一貫性維持

### Alternatives Considered
- **英語のみ対応**: 開発効率高いが、ユーザビリティ限定的
- **動的翻訳システム**: 外部翻訳APIとの連携（複雑性とコスト増大）

## 推奨実装戦略

### Phase 1: 基本ブロック実装
- クリスタル石、クリスタル丸石、クリスタルレンガの3ブロック追加
- プラットフォーム分離での登録システム実装
- 基本的な物理特性（採掘、ドロップ）設定

### Phase 2: レシピシステム統合
- 精錬レシピ（クリスタル丸石→クリスタル石）
- クラフトレシピ（クリスタル石→クリスタルレンガ）
- 対応アドバンスメント作成

### Phase 3: ワールド生成統合
- Crystalline Cavesバイオームでのクリスタル石生成
- 適切な頻度と分布パターン設定
- ルートテーブル統合

### Phase 4: アセットと多言語化
- Pythonスクリプトでのテクスチャ生成
- モデルファイル作成
- 日英両言語対応

## Technical Requirements Summary

- **Java Version**: Java 21
- **Minecraft Version**: 1.21.1
- **Architectury Version**: 13.0.8
- **Target Platforms**: Fabric 0.16.5, NeoForge 21.1.70
- **Resource Format**: Minecraft 1.21.1新仕様準拠
- **Testing Strategy**: runClient + 手動検証
- **Performance Target**: ワールド生成軽量化、60 FPS維持

## Dependencies Identified

1. **PIL (Python Imaging Library)**: テクスチャ生成用
2. **既存ModBlocksパターン**: ブロック登録の一貫性維持
3. **既存BiomeRegistryパターン**: バイオーム統合の参照
4. **Minecraft 1.21.1データ生成**: レシピ・アドバンスメント・ワールド生成