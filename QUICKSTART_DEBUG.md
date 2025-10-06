# クリスタル石ブロックシステム デバッグ用クイックスタート

## 問題: Crystalline Cavesバイオームが自然生成されない

現在、Crystalline Cavesバイオームは定義されていますが、ワールド生成システムに統合されていないため、自然生成されません。

## デバッグ用の動作確認方法

### 方法1: クリエイティブモードでのブロック直接配置テスト

1. **クリエイティブモードで新規ワールド作成**

2. **クリエイティブタブからブロックを取得**
   - WorldGenTestタブを開く
   - Crystal Stone、Crystal Cobblestone、Crystal Bricksを取得

3. **ブロック動作テスト**
   - Crystal Stoneを配置し、ツルハシで採掘 → Crystal Cobblestoneがドロップすることを確認
   - シルクタッチツルハシで採掘 → Crystal Stone自体がドロップすることを確認

4. **レシピテスト**
   - Crystal Cobblestoneをかまどで精錬 → Crystal Stoneが生成されることを確認
   - Crystal Stone 4個を2x2でクラフト → Crystal Bricks 4個が生成されることを確認

### 方法2: fillコマンドでの大量配置テスト

```
/fill ~0 ~0 ~0 ~10 ~10 ~10 worldgentest:crystal_stone
```

これでCrystal Stoneを一気に配置し、ツルハシでの採掘動作を確認できます。

### 方法3: setblockコマンドでの個別テスト

```
/setblock ~ ~ ~ worldgentest:crystal_stone
/setblock ~ ~ ~1 worldgentest:crystal_cobblestone
/setblock ~ ~ ~2 worldgentest:crystal_bricks
```

## 確認項目チェックリスト

- [ ] クリエイティブタブにすべてのブロックが表示される
- [ ] Crystal Stoneをツルハシで採掘するとCrystal Cobblestoneがドロップ
- [ ] Crystal Stoneをシルクタッチツルハシで採掘するとCrystal Stoneがドロップ
- [ ] Crystal Cobblestoneを精錬するとCrystal Stoneになる（かまど/溶鉱炉）
- [ ] Crystal Stone 4個からCrystal Bricks 4個をクラフトできる
- [ ] レシピブックにレシピが表示される
- [ ] アドバンスメントが解除される

## バイオーム生成の修正について

Crystalline Cavesバイオームを自然生成させるには、以下のいずれかの対応が必要です：

### オプション1: TerraBlenderの導入（推奨）
TerraBlenderは、複雑なバイオーム生成設定を簡単に行えるライブラリです。

**メリット**:
- 簡単な設定でバイオームを追加可能
- バニラと互換性の高い生成
- 他MODとの競合が少ない

**デメリット**:
- 外部依存関係の追加が必要

### オプション2: データパック方式での完全設定
multi_noise設定とdensity functionを手動で作成します。

**メリット**:
- 外部依存なし
- 完全な制御が可能

**デメリット**:
- 設定が非常に複雑
- Minecraft 1.21.1の仕様変更に注意が必要

### オプション3: 既存バイオームの置き換え（テスト用）
既存の地下バイオーム（例：Lush Caves）をCrystalline Cavesに置き換えます。

**メリット**:
- 実装が簡単
- すぐにテスト可能

**デメリット**:
- 既存バイオームが消える
- 本番環境には不適切

## 次のステップ

1. **まず上記のデバッグ方法で機能確認を完了**
2. **バイオーム生成が必要な場合はユーザーに確認**
   - TerraBlenderの導入を検討するか
   - データパック方式で完全に設定するか
   - テスト目的ならデバッグ方法で十分か

## 現時点での動作確認結果

- ✅ ブロック定義: 完了
- ✅ レシピシステム: 完了
- ✅ アセット（テクスチャ・モデル）: 完了
- ✅ 多言語対応: 完了
- ❌ **バイオーム自然生成: 未統合**（データパックでの定義のみ）

バイオーム生成の統合は、現在のタスクスコープ外の追加作業となります。
