# Data Model: クリスタル石ブロックシステム

**Date**: 2025-09-22
**Feature**: Crystal Stone Block System

## Entities Overview

### 1. Crystal Stone Block (クリスタル石ブロック)
**Purpose**: 自然生成される基本ブロック、バニラ石の白っぽいバリアント

**Properties**:
- **Name**: `crystal_stone`
- **Type**: 固体ブロック（Solid Block）
- **Hardness**: 1.5（バニラ石と同等）
- **Resistance**: 6.0（バニラ石と同等）
- **Tool Requirement**: ツルハシ（Pickaxe）必須
- **Sound Type**: STONE
- **Material**: STONE
- **Light Level**: 0（発光なし）

**Behavior**:
- 通常採掘時: Crystal Cobblestoneをドロップ
- シルクタッチ採掘時: Crystal Stone自体をドロップ
- ツルハシ以外での採掘: アイテムドロップなし

**World Generation**:
- **Biome**: Crystalline Caves専用
- **Generation Type**: Ore-like patches
- **Size**: 8-12ブロックのクラスター
- **Frequency**: 中程度（バニラ石より少ない）
- **Y-Level**: 0-64（洞窟内）

### 2. Crystal Cobblestone (クリスタル丸石)
**Purpose**: Crystal Stoneの採掘で得られる中間素材

**Properties**:
- **Name**: `crystal_cobblestone`
- **Type**: 固体ブロック（Solid Block）
- **Hardness**: 2.0（バニラ丸石と同等）
- **Resistance**: 6.0
- **Tool Requirement**: ツルハシ（Pickaxe）推奨
- **Sound Type**: STONE
- **Material**: STONE
- **Light Level**: 0

**Behavior**:
- 通常採掘時: Crystal Cobblestone自体をドロップ
- シルクタッチ採掘時: 同様（違いなし）
- 精錬可能: かまど・溶鉱炉でCrystal Stoneに変換

**Recipe Integration**:
- **Input for**: Crystal Stone精錬レシピ
- **Obtained from**: Crystal Stone採掘

### 3. Crystal Bricks (クリスタルレンガ)
**Purpose**: 装飾用建築ブロック、Crystal Stoneから作成

**Properties**:
- **Name**: `crystal_bricks`
- **Type**: 装飾ブロック（Decorative Block）
- **Hardness**: 2.0
- **Resistance**: 6.0
- **Tool Requirement**: ツルハシ（Pickaxe）推奨
- **Sound Type**: STONE
- **Material**: STONE
- **Light Level**: 0

**Behavior**:
- 通常採掘時: Crystal Bricks自体をドロップ
- シルクタッチ採掘時: 同様（違いなし）
- クラフト専用: 自然生成されない

**Recipe Integration**:
- **Created from**: Crystal Stone 4個（2x2パターン）
- **Output**: Crystal Bricks 4個

## Relationships

### Block Evolution Chain
```
Crystal Stone (自然生成)
    ↓ (採掘)
Crystal Cobblestone (中間素材)
    ↓ (精錬)
Crystal Stone (復元)
    ↓ (クラフト 4個)
Crystal Bricks (装飾ブロック 4個)
```

### Recipe Dependencies
```
1. Smelting Recipe
   Input: Crystal Cobblestone × 1
   Output: Crystal Stone × 1
   Method: Furnace, Blast Furnace

2. Crafting Recipe
   Input: Crystal Stone × 4 (2x2 pattern)
   Output: Crystal Bricks × 4
   Method: Crafting Table
```

## Validation Rules

### World Generation Constraints
- **Biome Restriction**: Crystalline Cavesバイオーム内のみ生成
- **Y-Level Range**: 0-64（地下洞窟環境）
- **Cluster Size**: 8-12ブロック（バニラ石の小型版）
- **Replacement Target**: 石系ブロック（stone_ore_replaceables tag）

### Mining Rules
- **Tool Requirement**:
  - Crystal Stone: ツルハシ必須（他ツールではドロップなし）
  - Crystal Cobblestone: ツルハシ推奨（他ツールでも低速採掘可能）
  - Crystal Bricks: ツルハシ推奨
- **Drop Behavior**:
  - 通常採掘: 指定されたドロップアイテム
  - シルクタッチ: ブロック自体（Crystal Stoneのみ特別動作）

### Recipe Constraints
- **Smelting Requirements**:
  - 燃料: バニラかまど・溶鉱炉の標準燃料
  - 時間: 標準精錬時間（10秒）
  - 経験値: 0.1（石系標準）
- **Crafting Requirements**:
  - パターン: 2x2のfilled pattern
  - 代替材料: なし（Crystal Stoneのみ）

## State Transitions

### Crystal Stone States
```
Natural Generation State:
- World Gen → Placed in Crystalline Caves
- Player Mining → Crystal Cobblestone (normal) | Crystal Stone (silk touch)

Crafted/Smelted State:
- Smelting Result → Available for crafting
- Crafting Usage → Consumed to create Crystal Bricks
```

### Crystal Cobblestone States
```
Mined State:
- From Crystal Stone → Ready for smelting
- Inventory Storage → Stackable (×64)
- Smelting Input → Consumed to create Crystal Stone
```

### Crystal Bricks States
```
Crafted State:
- From Crafting → Final decorative product
- Building Material → Permanent placement
- No Further Transformation → Terminal state
```

## Integration Points

### With Existing Systems
- **Creative Tab**: 既存のworldgentestクリエイティブタブに配置
- **Biome System**: Crystalline Cavesバイオームの生成要素として統合
- **Recipe Book**: カテゴリ別（misc, decorations）でのレシピブック表示
- **Advancement System**: レシピ解除アドバンスメントとの連携

### Localization Integration
```json
{
  "block.worldgentest.crystal_stone": {
    "en_us": "Crystal Stone",
    "ja_jp": "クリスタル石"
  },
  "block.worldgentest.crystal_cobblestone": {
    "en_us": "Crystal Cobblestone",
    "ja_jp": "クリスタル丸石"
  },
  "block.worldgentest.crystal_bricks": {
    "en_us": "Crystal Bricks",
    "ja_jp": "クリスタルレンガ"
  }
}
```

## Performance Considerations

### World Generation Impact
- **Generation Frequency**: 控えめな生成量でパフォーマンス維持
- **Chunk Loading**: 標準的なore generation負荷
- **Memory Usage**: 3ブロック追加による最小限の影響

### Runtime Performance
- **Rendering**: バニラ石系ブロックと同等の軽量レンダリング
- **Physics**: 標準的な固体ブロック物理演算
- **Storage**: 標準的なMinecraft chunk storage効率