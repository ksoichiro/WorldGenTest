# Recipe Functionality Test

**Test ID**: T005
**Purpose**: Validate crystal stone smelting and crafting recipes
**Expected Status**: MUST FAIL until recipes are implemented

## Test Cases

### TC-001: Crystal Cobblestone Smelting (Furnace)

**Prerequisites**:
- Crystal cobblestone item available (not yet implemented)
- Furnace and coal available

**Test Steps**:
1. Place crystal cobblestone in furnace input
   - **Expected**: Furnace accepts the item
   - **Actual**: [FAILS - Item not implemented]
2. Add coal as fuel
3. Wait for smelting (10 seconds)
   - **Expected**: Produces crystal stone item
   - **Actual**: [FAILS - Recipe not implemented]
4. Check experience gain
   - **Expected**: 0.1 experience points
   - **Actual**: [FAILS - Recipe not implemented]

### TC-002: Crystal Cobblestone Smelting (Blast Furnace)

**Test Steps**:
1. Place crystal cobblestone in blast furnace input
   - **Expected**: Blast furnace accepts the item
   - **Actual**: [FAILS - Item not implemented]
2. Add coal as fuel
3. Wait for smelting (5 seconds, faster than furnace)
   - **Expected**: Produces crystal stone item
   - **Actual**: [FAILS - Recipe not implemented]
4. Verify same output as furnace
   - **Expected**: Identical crystal stone item
   - **Actual**: [FAILS - Recipe not implemented]

### TC-003: Crystal Bricks Crafting

**Prerequisites**:
- Crystal stone items available (not yet implemented)
- Crafting table available

**Test Steps**:
1. Open crafting table interface
2. Place crystal stone items in 2x2 pattern:
   ```
   [Crystal Stone] [Crystal Stone]
   [Crystal Stone] [Crystal Stone]
   ```
   - **Expected**: Crafting preview shows 4 crystal bricks
   - **Actual**: [FAILS - Items/Recipe not implemented]
3. Take result from crafting output
   - **Expected**: Receive 4 crystal bricks, consume 4 crystal stones
   - **Actual**: [FAILS - Recipe not implemented]

### TC-004: Recipe Book Integration

**Test Steps**:
1. Obtain crystal cobblestone item
   - **Expected**: Smelting recipe appears in recipe book under "Misc" category
   - **Actual**: [FAILS - Items not implemented]
2. Obtain crystal stone item
   - **Expected**: Crafting recipe appears in recipe book under "Decorations" category
   - **Actual**: [FAILS - Items not implemented]
3. Verify recipe book search functionality
   - **Expected**: Searching "crystal" shows both recipes
   - **Actual**: [FAILS - Recipes not implemented]

### TC-005: Advanced Recipe Validation

**Test Steps**:
1. Test incorrect crafting patterns
   - Place crystal stones in 1x4, 3x3, or other patterns
   - **Expected**: No crafting output
   - **Actual**: [FAILS - Recipe not implemented]
2. Test partial patterns
   - Place only 1-3 crystal stones in 2x2 area
   - **Expected**: No crafting output
   - **Actual**: [FAILS - Recipe not implemented]
3. Test with similar but wrong items
   - Try crafting with regular stone instead of crystal stone
   - **Expected**: No crafting output
   - **Actual**: [FAILS - Recipe not implemented]

## Advancement Integration Tests

### TC-006: Recipe Unlock Advancements

**Test Steps**:
1. Obtain crystal cobblestone for first time
   - **Expected**: "Crystal Purification" advancement unlocks
   - **Expected**: Smelting recipes appear in recipe book
   - **Actual**: [FAILS - Advancement not implemented]
2. Obtain crystal stone for first time
   - **Expected**: "Crystal Architecture" advancement unlocks
   - **Expected**: Crafting recipe appears in recipe book
   - **Actual**: [FAILS - Advancement not implemented]

## Validation Criteria

- [ ] Furnace smelting: cobblestone → stone (10s, 0.1 XP)
- [ ] Blast furnace smelting: cobblestone → stone (5s, 0.1 XP)
- [ ] Crafting: 4 crystal stones → 4 crystal bricks (2x2 pattern)
- [ ] Recipe book shows recipes in correct categories
- [ ] Advancements unlock properly
- [ ] Recipe search functionality works
- [ ] Invalid patterns don't produce output

## Current Status

**ALL TESTS FAIL** - Items and recipes are not yet implemented. This is expected behavior for TDD approach.

Tests will be re-run after:
1. Block implementation (Phase 3.3)
2. Recipe implementation (Phase 3.5)
3. Advancement implementation (Phase 3.5)