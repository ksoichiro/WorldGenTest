# Tasks: クリスタル石ブロックシステム

**Input**: Design documents from `/specs/001-mod/`
**Prerequisites**: plan.md (required), research.md, data-model.md, contracts/

## Execution Flow (main)
```
1. Load plan.md from feature directory
   → Tech stack: Java 21, Minecraft 1.21.1, Architectury API 13.0.8
   → Structure: common + platform-specific (Fabric/NeoForge)
2. Load design documents:
   → data-model.md: 3 entities (Crystal Stone, Cobblestone, Bricks)
   → contracts/: Block system, recipes, world generation, assets
   → quickstart.md: 5 test scenarios for validation
3. Generate tasks by category:
   → Setup: texture generation, project verification
   → Tests: block behavior, recipe functionality, world generation
   → Core: block registration, recipe implementation, world generation
   → Integration: biome integration, advancement system
   → Polish: localization, documentation, performance testing
4. Applied task rules:
   → Different platforms/files = [P] for parallel
   → Same platform files = sequential (no [P])
   → Tests before implementation (TDD approach)
5. Generated 24 tasks across 5 phases
6. SUCCESS (tasks ready for execution)
```

## Format: `[ID] [P?] Description`
- **[P]**: Can run in parallel (different files, no dependencies)
- Include exact file paths in descriptions

## Path Conventions
- **Minecraft Mod structure**: `common/src/main/`, `fabric/src/main/`, `neoforge/src/main/`
- **Resources**: `common/src/main/resources/`
- **Testing**: Manual testing via `runClient`

## Phase 3.1: Setup and Asset Generation
- [x] T001 [P] Create Python texture generation script in `/scripts/generate_crystal_textures.py`
- [x] T002 [P] Generate crystal stone textures from vanilla stone in `common/src/main/resources/assets/worldgentest/textures/block/`
- [x] T003 Verify existing project structure and dependencies for crystal stone implementation

## Phase 3.2: Tests First (TDD) ⚠️ MUST COMPLETE BEFORE 3.3
**CRITICAL: These validation scenarios MUST be documented and MUST FAIL before ANY implementation**
- [x] T004 [P] Document block behavior test in `tests/manual/block_behavior_test.md`
- [x] T005 [P] Document recipe functionality test in `tests/manual/recipe_test.md`
- [x] T006 [P] Document world generation test in `tests/manual/world_generation_test.md`
- [x] T007 [P] Document integration workflow test in `tests/manual/integration_test.md`

## Phase 3.3: Core Block Implementation (ONLY after tests are documented)
- [x] T008 [P] Crystal Stone block implementation in `neoforge/src/main/java/com/example/worldgentest/ModBlocks.java`
- [x] T009 [P] Crystal Cobblestone block implementation in `neoforge/src/main/java/com/example/worldgentest/ModBlocks.java`
- [x] T010 [P] Crystal Bricks block implementation in `neoforge/src/main/java/com/example/worldgentest/ModBlocks.java`
- [x] T011 [P] Fabric block registration in `fabric/src/main/java/com/example/worldgentest/FabricModItems.java`
- [x] T012 [P] NeoForge block registration in `neoforge/src/main/java/com/example/worldgentest/ModBlocks.java`

## Phase 3.4: Asset Integration
- [x] T013 [P] Block models for crystal stone in `common/src/main/resources/assets/worldgentest/models/block/`
- [x] T014 [P] Item models for crystal blocks in `common/src/main/resources/assets/worldgentest/models/item/`
- [x] T015 [P] Blockstate definitions in `common/src/main/resources/assets/worldgentest/blockstates/`
- [x] T016 [P] Block loot tables in `common/src/main/resources/data/worldgentest/loot_table/blocks/`

## Phase 3.5: Recipe System Implementation
- [x] T017 [P] Crystal stone smelting recipes in `common/src/main/resources/data/worldgentest/recipe/`
- [x] T018 [P] Crystal bricks crafting recipe in `common/src/main/resources/data/worldgentest/recipe/`
- [x] T019 [P] Smelting advancement in `common/src/main/resources/data/worldgentest/advancement/recipe/misc/`
- [x] T020 [P] Crafting advancement in `common/src/main/resources/data/worldgentest/advancement/recipe/decorations/`

## Phase 3.6: World Generation Integration
- [x] T021 Crystal stone world generation feature in `common/src/main/resources/data/worldgentest/worldgen/configured_feature/`
- [x] T022 Crystal stone placed feature in `common/src/main/resources/data/worldgentest/worldgen/placed_feature/`
- [x] T023 Biome integration in existing Crystalline Caves biome configuration

## Phase 3.7: Localization and Polish
- [x] T024 [P] Localization files update in `common/src/main/resources/assets/worldgentest/lang/`

## Dependencies
- Asset generation (T001-T002) before asset integration (T013-T016)
- Tests documentation (T004-T007) before block implementation (T008-T012)
- Block implementation (T008-T012) before recipe system (T017-T020)
- Block implementation (T008-T012) before world generation (T021-T023)
- Recipe implementation (T017-T018) before advancement system (T019-T020)
- World generation features (T021-T022) before biome integration (T023)

## Parallel Example
```
# Launch T008-T010 together (different entities in same file):
# NOTE: These cannot be truly parallel as they modify the same ModBlocks.java file
# They should be executed sequentially but can be planned together

# Launch T011-T012 together (different platforms):
Task: "Fabric block registration in fabric/src/main/java/com/example/worldgentest/FabricModBlocks.java"
Task: "NeoForge block registration in neoforge/src/main/java/com/example/worldgentest/ModBlocks.java"

# Launch T013-T016 together (different asset types):
Task: "Block models in common/src/main/resources/assets/worldgentest/models/block/"
Task: "Item models in common/src/main/resources/assets/worldgentest/models/item/"
Task: "Blockstate definitions in common/src/main/resources/assets/worldgentest/blockstates/"
Task: "Block loot tables in common/src/main/resources/data/worldgentest/loot_table/blocks/"
```

## Detailed Task Specifications

### T001: Python Texture Generation Script
**File**: `/scripts/generate_crystal_textures.py`
**Purpose**: Create automated texture generation from vanilla stone assets
**Requirements**:
- Extract vanilla stone.png, cobblestone.png, stone_bricks.png
- Apply white/crystal color transformation (brightness +30%, saturation -40%)
- Output crystal_stone.png, crystal_cobblestone.png, crystal_bricks.png
- Handle PNG format and Minecraft texture resolution (16x16)

### T002: Generate Crystal Textures
**Files**: `common/src/main/resources/assets/worldgentest/textures/block/`
**Purpose**: Execute texture generation script and verify output
**Requirements**:
- Run T001 script to generate all 3 block textures
- Verify texture quality and color consistency
- Ensure textures match Minecraft format standards

### T008-T010: Block Implementation
**File**: `common/src/main/java/com/example/worldgentest/ModBlocks.java`
**Purpose**: Define block properties and behavior
**Requirements**:
- Crystal Stone: hardness 1.5, requires pickaxe, drops cobblestone normally
- Crystal Cobblestone: hardness 2.0, standard drops
- Crystal Bricks: hardness 2.0, decorative block properties
- All blocks: STONE material, STONE sound, resistance 6.0

### T017-T018: Recipe Implementation
**Files**: `common/src/main/resources/data/worldgentest/recipe/`
**Purpose**: JSON recipe definitions for Minecraft 1.21.1 format
**Requirements**:
- Smelting: crystal_cobblestone → crystal_stone (furnace + blast_furnace)
- Crafting: 2x2 crystal_stone → 4x crystal_bricks
- Category: misc (smelting), decorations (crafting)
- Experience: 0.1 for smelting, none for crafting

### T021-T023: World Generation
**Files**: `common/src/main/resources/data/worldgentest/worldgen/`
**Purpose**: Integration with existing Crystalline Caves biome
**Requirements**:
- Ore-like generation pattern, size 8-12 blocks
- Y-level range 0-64, moderate frequency
- Target: stone_ore_replaceables tag
- Biome: worldgentest:crystalline_caves only

## Testing Integration

### Manual Testing Workflow (from quickstart.md)
1. **Scenario 1**: Find and mine crystal stone in Crystalline Caves biome
2. **Scenario 2**: Smelt cobblestone to stone (furnace + blast furnace)
3. **Scenario 3**: Craft stone to bricks (2x2 pattern)
4. **Scenario 4**: Verify advancement unlocking
5. **Scenario 5**: Use bricks for building and verify behavior

### Validation Checklist
- [ ] All block contracts implemented (T008-T012)
- [ ] All recipe contracts implemented (T017-T020)
- [ ] All world generation contracts implemented (T021-T023)
- [ ] All quickstart scenarios pass
- [ ] Both Fabric and NeoForge platforms work
- [ ] Performance targets met (60 FPS, minimal generation overhead)

## Notes
- [P] tasks = different files/platforms, no dependencies
- Sequential tasks within same files (ModBlocks.java modifications)
- Test documentation before implementation (TDD approach)
- Commit after each logical group completion
- Avoid: modifying same files in parallel, skipping test scenarios

## Task Generation Rules Applied
1. **From Contracts**: Block, Recipe, World Generation, Asset contracts → implementation tasks
2. **From Data Model**: 3 entities → 3 core block tasks + platform registrations
3. **From Quickstart**: 5 scenarios → comprehensive test documentation
4. **Ordering**: Setup → Tests → Models → Recipes → Integration → Polish
5. **Platform Separation**: Fabric/NeoForge tasks can run in parallel