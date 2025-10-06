# Quickstart: クリスタル石ブロックシステム

**Date**: 2025-09-22
**Estimated Time**: 15分
**Prerequisites**: Minecraft 1.21.1, WorldGenTest mod installed

## Overview
このガイドでは、新しく追加されたクリスタル石ブロックシステムの基本的な使用方法を説明します。採掘からクラフトまでの完全なワークフローを体験できます。

## Quick Test Scenarios

### Scenario 1: クリスタル石の発見と採掘 (5分)

**Goal**: クリスタルの洞窟バイオームでクリスタル石を見つけて採掘する

**Steps**:
1. **ワールド生成**
   ```
   /locate biome worldgentest:crystalline_caves
   ```
   コマンドでクリスタルの洞窟バイオームの座標を取得

2. **バイオームへ移動**
   ```
   /tp @s <x> <y> <z>
   ```
   座標にテレポートし、洞窟内を探索

3. **クリスタル石の特定**
   - 白っぽい石ブロックを探す
   - バニラの石より明るい色調
   - 洞窟壁や天井に自然生成

4. **採掘テスト**
   - **ツルハシで採掘**: クリスタル丸石をドロップ
   - **シルクタッチツルハシで採掘**: クリスタル石ブロックをドロップ
   - **他ツールで採掘**: ブロック破壊、アイテムドロップなし

**Expected Results**:
- ✅ クリスタル石が洞窟内に見つかる
- ✅ ツルハシ採掘でクリスタル丸石を入手
- ✅ シルクタッチ採掘でクリスタル石を入手
- ✅ 他ツールでは何もドロップしない

### Scenario 2: 精錬ワークフロー (3分)

**Goal**: クリスタル丸石をクリスタル石に精錬する

**Prerequisites**: クリスタル丸石 × 1, かまど, 燃料

**Steps**:
1. **かまどの準備**
   - かまどまたは溶鉱炉を設置
   - 燃料（石炭、木炭等）を用意

2. **精錬実行**
   ```
   Input: クリスタル丸石 × 1
   Fuel: 石炭 × 1
   Time: 10秒 (標準精錬時間)
   ```

3. **結果確認**
   - かまどからクリスタル石 × 1を取得
   - 経験値 0.1を獲得

**Expected Results**:
- ✅ クリスタル丸石がクリスタル石に変換される
- ✅ 精錬時間は10秒
- ✅ 溶鉱炉でも同様に動作する
- ✅ レシピブックに精錬レシピが表示される

### Scenario 3: クラフトワークフロー (2分)

**Goal**: クリスタル石からクリスタルレンガを作成する

**Prerequisites**: クリスタル石 × 4, 作業台

**Steps**:
1. **作業台でのクラフト**
   ```
   Pattern (2x2):
   [Crystal Stone] [Crystal Stone]
   [Crystal Stone] [Crystal Stone]

   Result: Crystal Bricks × 4
   ```

2. **レシピブック確認**
   - クリスタル石を所持すると自動でレシピ解除
   - 「装飾ブロック」カテゴリに表示

3. **クラフト実行**
   - 2x2パターンでクリスタル石 × 4を配置
   - クリスタルレンガ × 4を取得

**Expected Results**:
- ✅ 4個のクリスタル石から4個のクリスタルレンガ作成
- ✅ レシピブックに表示される
- ✅ アドバンスメント「Crystal Architecture」解除

### Scenario 4: アドバンスメント確認 (2分)

**Goal**: 関連するアドバンスメントが正しく動作することを確認

**Steps**:
1. **精錬アドバンスメント**
   - クリスタル丸石を所持した時点で精錬レシピ解除
   - アドバンスメント「Crystal Purification」解除

2. **クラフトアドバンスメント**
   - クリスタル石を所持した時点でクラフトレシピ解除
   - アドバンスメント「Crystal Architecture」解除

**Expected Results**:
- ✅ 材料入手時にレシピブックに自動表示
- ✅ アドバンスメント通知が表示される
- ✅ アドバンスメント画面で確認可能

### Scenario 5: 建築での使用 (3分)

**Goal**: クリスタルレンガを使って実際に建築する

**Steps**:
1. **クリスタルレンガの配置**
   - 壁、床、天井に配置テスト
   - バニラ石系ブロックとの組み合わせテスト

2. **採掘テスト**
   - 配置したクリスタルレンガをツルハシで採掘
   - 正しくアイテム化されることを確認

3. **視覚確認**
   - テクスチャが正しく表示される
   - 他ブロックとの境界が自然

**Expected Results**:
- ✅ 正常に配置・採掘できる
- ✅ テクスチャが正しく表示される
- ✅ 建築材料として使用可能

## Integration Test Workflow

### Complete End-to-End Test (15分)

**Goal**: 発見から建築まで全体のワークフローを検証

**Complete Workflow**:
```
1. World Generation → Find Crystalline Caves biome
2. Exploration → Locate Crystal Stone in caves
3. Mining → Extract Crystal Cobblestone (normal pickaxe)
4. Alternative Mining → Extract Crystal Stone (silk touch)
5. Smelting → Convert Cobblestone to Stone
6. Crafting → Convert Stone to Bricks
7. Building → Use Bricks for construction
8. Verification → Confirm all recipes and advancements
```

**Success Criteria**:
- [ ] All blocks generate correctly in the biome
- [ ] Mining behavior matches specifications
- [ ] Recipes work as designed
- [ ] Advancements unlock properly
- [ ] Assets (textures, models) display correctly
- [ ] Localization works for both languages
- [ ] No errors in server/client logs
- [ ] Performance remains stable

## Troubleshooting

### Common Issues

#### Issue: クリスタル石が見つからない
**Symptoms**: クリスタルの洞窟バイオームにクリスタル石が生成されない
**Solutions**:
1. バイオームの確認: `/locate biome worldgentest:crystalline_caves`
2. 洞窟内部の探索: 地表ではなく地下の洞窟部分
3. ワールド再生成: 新しいチャンクでの確認

#### Issue: レシピが表示されない
**Symptoms**: レシピブックにクリスタル石レシピが表示されない
**Solutions**:
1. 材料の確認: 正しいアイテム（クリスタル丸石/クリスタル石）を所持
2. アドバンスメント確認: レシピ解除アドバンスメントの状態
3. サーバー再起動: サーバー環境での場合

#### Issue: ドロップアイテムが正しくない
**Symptoms**: 採掘時に期待したアイテムがドロップしない
**Solutions**:
1. ツール確認: ツルハシを使用しているか
2. エンチャント確認: シルクタッチの有無
3. ブロック確認: 正しいクリスタル石ブロックか

### Performance Verification

#### FPS Monitoring
- **Baseline**: バニラバイオームでのFPS計測
- **With Feature**: クリスタルの洞窟でのFPS計測
- **Threshold**: 60 FPS維持（significant drop <5 FPS）

#### Memory Usage
- **Baseline**: MOD無しでのメモリ使用量
- **With Feature**: 機能追加後のメモリ使用量
- **Threshold**: +50MB以下の増加

#### Generation Performance
- **Chunk Loading**: 新しいチャンク生成時間の測定
- **Biome Generation**: クリスタルの洞窟バイオーム生成負荷
- **Threshold**: 標準バイオームと同等の生成時間

## Documentation Integration

### Update Required Documents

1. **CLAUDE.md**: 実装完了セクションの更新
2. **README.md**: 新機能の説明追加
3. **CHANGELOG.md**: バージョン履歴への記録

### User Guide Sections

1. **Block Guide**: 新ブロックの説明
2. **Recipe Guide**: レシピの詳細説明
3. **Building Guide**: 建築での使用例
4. **Technical Guide**: MOD開発者向け情報

## Validation Checklist

### Functional Validation
- [ ] All blocks generate in correct biome
- [ ] Mining behavior matches specification
- [ ] Recipes produce correct outputs
- [ ] Advancements unlock as expected
- [ ] Assets display correctly
- [ ] Localization works for all languages

### Platform Validation
- [ ] Fabric platform works correctly
- [ ] NeoForge platform works correctly
- [ ] Cross-platform consistency verified

### Performance Validation
- [ ] No significant FPS impact
- [ ] Memory usage within acceptable range
- [ ] Generation performance adequate

### Integration Validation
- [ ] Compatible with existing features
- [ ] No conflicts with other mods
- [ ] Server/client synchronization correct