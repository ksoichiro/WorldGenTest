# Quick Start: カスタムディメンション開発

**Feature**: カスタムディメンション追加
**Branch**: `003-mod`
**Date**: 2025-10-19

## 概要

このガイドは、カスタムディメンション機能の開発を開始するための手順を示します。

---

## 前提条件

### 必須環境

- **Java**: JDK 21以降
- **IDE**: IntelliJ IDEA（推奨）
- **Minecraft**: 1.21.1
- **プロジェクト**: WorldGenTest（Architectury + Terrablender統合済み）

### 既存の実装

このプロジェクトには以下が既に実装されています：

- ✅ Architecturyマルチプラットフォーム設定（Fabric + NeoForge）
- ✅ ModDevGradle 2.0ビルドシステム
- ✅ Terrablender統合（Crystalline Cavesバイオーム実装済み）
- ✅ 既存MODブロック（`crystal_block`）
- ✅ 既存MODアイテム（`crystal_shard`）

---

## 開発環境セットアップ

### 1. リポジトリのクローンとビルド

```bash
cd /Users/ksoichiro/src/github.com/ksoichiro/WorldGenTest
git checkout 003-mod

# ビルド確認
./gradlew build

# 動作確認（Fabricクライアント起動）
./gradlew fabric:runClient
```

### 2. IDEセットアップ（IntelliJ IDEA）

```bash
# Gradleプロジェクトとしてインポート
# File > Open > build.gradle.kts選択

# Minecraft開発用の設定
# Settings > Build > Build Tools > Gradle
# - Build and run using: IntelliJ IDEA
# - Run tests using: IntelliJ IDEA
```

### 3. 既存コードの確認

**重要な既存ファイル**:
```
common/src/main/java/com/example/worldgentest/
├── ModBlocks.java           # crystal_blockが定義済み
├── ModItems.java            # crystal_shardが定義済み
└── biome/
    └── CrystallineCavesRegion.java  # Terrablender統合パターン

fabric/src/main/java/com/example/worldgentest/
└── biome/
    └── FabricCrystallineCavesRegion.java  # Yarn mapping対応

neoforge/src/main/java/com/example/worldgentest/
└── biome/
    └── NeoForgeCrystallineCavesRegion.java  # Mojang mapping対応
```

---

## 実装フェーズ

### Phase 0: 研究（完了）

✅ **完了済み**: [research.md](./research.md)参照

- Minecraft 1.21.1 ディメンションシステム調査
- ポータルフレーム検出アルゴリズム調査
- Terrablenderバイオーム統合パターン確認

### Phase 1: データモデル & 設計（完了）

✅ **完了済み**: [data-model.md](./data-model.md)参照

- エンティティ定義（PortalFrame、CrystalDimension等）
- ER図とデータ整合性ルール
- JSON構造定義

### Phase 2: 実装タスク分解

⏳ **次のステップ**: `/speckit.tasks`コマンド実行

タスク生成後、以下の順序で実装：

1. **ディメンション定義**（JSON）
   - `dimension_type/crystal_dimension.json`
   - `dimension/crystal_dimension.json`

2. **カスタムバイオーム**（JSON + Terrablender）
   - 4種バイオームJSON定義
   - `CrystalDimensionRegion.java`実装

3. **カスタムブロック**（Java + リソース）
   - `CrystalGrassBlock`, `CrystalDirtBlock`, `CrystalLogBlock`, `CrystalSandBlock`
   - テクスチャ作成（バニラベース + 色調変更）

4. **ポータルシステム**（Java）
   - `CrystalPortalBlock.java`
   - `PortalFrameDetector.java`
   - `PortalActivationHandler.java`

5. **テレポート機構**（Java + プラットフォーム分離）
   - `CrystalDimensionTeleporter.java`（common）
   - Fabric/NeoForge実装（`@ExpectPlatform`パターン）

---

## 開発ワークフロー

### 日常的な開発サイクル

```bash
# 1. コード変更
vim common/src/main/java/com/example/worldgentest/dimension/ModDimensions.java

# 2. ビルド
./gradlew build

# 3. テスト実行（Fabricクライアント）
./gradlew fabric:runClient

# 4. 動作確認
# - 新規ワールド作成（Defaultワールドタイプ）
# - クリスタルブロックでポータルフレーム構築
# - クリスタルの欠片で起動
# - ポータル通過
```

### 並行開発の注意事項（CLAUDE.mdより）

**重要**: 並行開発中は`runClient`を実行せず、`build`のみで動作確認

```bash
# 開発中のビルド確認
./gradlew build                # 全体
./gradlew fabric:build        # Fabricのみ
./gradlew neoforge:build      # NeoForgeのみ

# クライアント実行は機能完了後に手動で実行
```

**理由**: 複数のクライアント同時起動によるマシン負荷回避

---

## デバッグ方法

### ログ出力（NFR-005要件）

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortalFrameDetector {
    private static final Logger LOGGER = LoggerFactory.getLogger(PortalFrameDetector.class);

    public PortalFrame detectFrame(Level world, BlockPos pos) {
        try {
            // 検出ロジック...
        } catch (Exception e) {
            // エラー時のみログ出力
            LOGGER.error("Portal frame detection failed at {}", pos, e);
        }
        return null;
    }
}
```

### IntelliJ IDEA デバッガー使用

```bash
# Run Configurations作成
# 1. Edit Configurations...
# 2. + > Gradle
# 3. Task: fabric:runClient
# 4. Debug実行でブレークポイント設定可能
```

### ゲーム内デバッグコマンド

```
/locate biome worldgentest:crystal_plains
/execute in worldgentest:crystal_dimension run tp @s 0 100 0
/data get entity @s Dimension
```

---

## テスト戦略

### 単体テスト（限定的）

```java
// tests/unit/PortalFrameDetectorTest.java
@Test
public void testFrameDetection_MinimumSize() {
    PortalFrame frame = new PortalFrame(new BlockPos(0, 0, 0), 2, 3);
    assertTrue(frame.isValid());
}

@Test
public void testCoordinateScale() {
    BlockPos overworldPos = new BlockPos(100, 64, 200);
    BlockPos dimensionPos = scaleCoordinate(overworldPos, 0.25);
    assertEquals(new BlockPos(25, 64, 50), dimensionPos);
}
```

### 実行時テスト（主要）

**テストシナリオ**:
1. ポータルフレーム構築（最小2×3、最大21×21）
2. クリスタルの欠片で起動
3. ポータル通過（4秒待機）
4. カスタムディメンション到着確認
5. バイオーム探索（4種確認）
6. 帰還テスト

**チェックポイント**:
- [ ] ポータル起動時に紫色パーティクル表示
- [ ] ポータル通過時にサウンド再生
- [ ] ディメンション内が常に昼間
- [ ] 空が紫がかった色
- [ ] カスタムブロックが正しく生成
- [ ] 座標スケール1:4が正しく動作

---

## トラブルシューティング

### よくある問題

#### 1. ディメンションが見つからない

**症状**: `/locate biome worldgentest:crystal_plains`で"Unknown biome"

**原因**:
- JSONファイルのパスが間違っている
- JSONシンタックスエラー

**解決策**:
```bash
# JSON構造確認
jq . common/src/main/resources/data/worldgentest/dimension_type/crystal_dimension.json

# ビルド出力確認
ls -la fabric/build/resources/main/data/worldgentest/dimension_type/
```

#### 2. ポータルが起動しない

**症状**: クリスタルの欠片を使ってもポータルブロックが生成されない

**原因**:
- フレーム構造が不正
- イベントハンドラー未登録

**解決策**:
```java
// ログ追加でフレーム検出状況確認
LOGGER.info("Frame detection at {}: width={}, height={}, valid={}",
            pos, frame.width, frame.height, frame.isValid());
```

#### 3. テレポート時にクラッシュ

**症状**: ポータル通過時にゲームクラッシュ

**原因**:
- スポーン位置が不正（空中、溶岩等）
- ディメンション未読み込み

**解決策**:
```java
// スポーン位置の安全性確認
private BlockPos findSafeSpawnPos(ServerLevel level, BlockPos basePos) {
    // y=64基準で安全位置探索（FR-016要件）
}
```

---

## リソース

### プロジェクトドキュメント
- [plan.md](./plan.md) - 実装計画
- [research.md](./research.md) - 技術調査結果
- [data-model.md](./data-model.md) - データモデル定義
- [CLAUDE.md](../../CLAUDE.md) - 開発ガイドライン

### 外部リソース
- [Architectury Documentation](https://docs.architectury.dev/)
- [Minecraft Wiki - Dimension](https://minecraft.wiki/w/Dimension_type)
- [Misode's Generators](https://misode.github.io/)
- [Terrablender Wiki](https://github.com/Glitchfiend/TerraBlender/wiki)

---

## 次のステップ

1. `/speckit.tasks`コマンド実行
2. `tasks.md`でタスク優先順位確認
3. T001タスクから順次実装開始

**推定期間**: 2-3週間（並行開発なし、順次実装）
