---
description: "Implementation tasks for custom dimension feature"
---

# Tasks: カスタムディメンション追加

**Input**: Design documents from `/specs/003-mod/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/, quickstart.md

**Tests**: Tests are not explicitly requested in the specification, so test tasks are NOT included. Implementation will rely on runtime testing (`./gradlew runClient`).

**Organization**: Tasks are organized by acceptance scenarios (mapped to implementation phases) to enable incremental delivery and independent testing.

## Format: `[ID] [P?] [Story] Description`
- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which acceptance scenario/story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions
- **Common module**: `common/src/main/java/com/example/worldgentest/`
- **Common resources**: `common/src/main/resources/`
- **Fabric module**: `fabric/src/main/java/com/example/worldgentest/`
- **NeoForge module**: `neoforge/src/main/java/com/example/worldgentest/`

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and resource preparation

- [X] T001 Verify existing Architectury project structure and dependencies (ModDevGradle 2.0, Terrablender 4.1.0.3)
- [X] T002 [P] Create base directory structure for dimension feature: `common/src/main/java/com/example/worldgentest/dimension/`, `common/src/main/java/com/example/worldgentest/portal/`
- [X] T003 [P] Create JSON directory structure: `common/src/main/resources/data/worldgentest/dimension_type/`, `common/src/main/resources/data/worldgentest/dimension/`, `common/src/main/resources/data/worldgentest/worldgen/biome/`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core dimension infrastructure that MUST be complete before user stories

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

### Custom Dimension Definition (JSON-based)

- [X] T004 [P] Create dimension type JSON in `common/src/main/resources/data/worldgentest/dimension_type/crystal_dimension.json` (fixed_time=6000, coordinate_scale=1.0, bed_works=true, effects="minecraft:overworld")
- [X] T005 [P] Create level stem JSON in `common/src/main/resources/data/worldgentest/dimension/crystal_dimension.json` (noise generator with multi_noise biome source)
- [X] T006 Register dimension key constant in `common/src/main/java/com/example/worldgentest/dimension/ModDimensions.java` (ResourceKey<Level> CRYSTAL_DIMENSION)

### Custom Blocks Foundation

- [X] T007 [P] Create CrystalGrassBlock class in `common/src/main/java/com/example/worldgentest/block/CrystalGrassBlock.java` (hardness=0.6, tool=SHOVEL, drop=self)
- [X] T008 [P] Create CrystalDirtBlock class in `common/src/main/java/com/example/worldgentest/block/CrystalDirtBlock.java` (hardness=0.5, tool=SHOVEL, drop=self)
- [X] T009 [P] Create CrystalLogBlock class in `common/src/main/java/com/example/worldgentest/block/CrystalLogBlock.java` (hardness=2.0, tool=AXE, drop=self)
- [X] T010 [P] Create CrystalSandBlock class in `common/src/main/java/com/example/worldgentest/block/CrystalSandBlock.java` (hardness=0.5, tool=SHOVEL, drop=self)
- [X] T011 Register custom blocks in `common/src/main/java/com/example/worldgentest/ModBlocks.java` (CRYSTAL_GRASS_BLOCK, CRYSTAL_DIRT, CRYSTAL_LOG, CRYSTAL_SAND)
- [X] T012 Register custom block items in Fabric: `fabric/src/main/java/com/example/worldgentest/FabricModBlocks.java`
- [X] T013 Register custom block items in NeoForge: `neoforge/src/main/java/com/example/worldgentest/ModBlocks.java`

### Block Resources (Textures, Models, Loot Tables)

- [ ] T014 [P] Create crystal grass block textures in `common/src/main/resources/assets/worldgentest/textures/block/` (crystal_grass_block_top.png, crystal_grass_block_side.png, based on vanilla with color adjustment)
- [ ] T015 [P] Create crystal dirt texture in `common/src/main/resources/assets/worldgentest/textures/block/crystal_dirt.png`
- [ ] T016 [P] Create crystal log textures in `common/src/main/resources/assets/worldgentest/textures/block/` (crystal_log.png, crystal_log_top.png)
- [ ] T017 [P] Create crystal sand texture in `common/src/main/resources/assets/worldgentest/textures/block/crystal_sand.png`
- [ ] T018 [P] Create blockstate JSON for crystal grass block in `common/src/main/resources/assets/worldgentest/blockstates/crystal_grass_block.json`
- [ ] T019 [P] Create blockstate JSON for crystal dirt in `common/src/main/resources/assets/worldgentest/blockstates/crystal_dirt.json`
- [ ] T020 [P] Create blockstate JSON for crystal log in `common/src/main/resources/assets/worldgentest/blockstates/crystal_log.json`
- [ ] T021 [P] Create blockstate JSON for crystal sand in `common/src/main/resources/assets/worldgentest/blockstates/crystal_sand.json`
- [ ] T022 [P] Create block model JSON for crystal grass block in `common/src/main/resources/assets/worldgentest/models/block/crystal_grass_block.json`
- [ ] T023 [P] Create block model JSON for crystal dirt in `common/src/main/resources/assets/worldgentest/models/block/crystal_dirt.json`
- [ ] T024 [P] Create block model JSON for crystal log in `common/src/main/resources/assets/worldgentest/models/block/crystal_log.json`
- [ ] T025 [P] Create block model JSON for crystal sand in `common/src/main/resources/assets/worldgentest/models/block/crystal_sand.json`
- [ ] T026 [P] Create item model JSON for crystal grass block in `common/src/main/resources/assets/worldgentest/models/item/crystal_grass_block.json`
- [ ] T027 [P] Create item model JSON for crystal dirt in `common/src/main/resources/assets/worldgentest/models/item/crystal_dirt.json`
- [ ] T028 [P] Create item model JSON for crystal log in `common/src/main/resources/assets/worldgentest/models/item/crystal_log.json`
- [ ] T029 [P] Create item model JSON for crystal sand in `common/src/main/resources/assets/worldgentest/models/item/crystal_sand.json`
- [ ] T030 [P] Create loot table for crystal grass block in `common/src/main/resources/data/worldgentest/loot_table/block/crystal_grass_block.json` (drops self)
- [ ] T031 [P] Create loot table for crystal dirt in `common/src/main/resources/data/worldgentest/loot_table/block/crystal_dirt.json`
- [ ] T032 [P] Create loot table for crystal log in `common/src/main/resources/data/worldgentest/loot_table/block/crystal_log.json`
- [ ] T033 [P] Create loot table for crystal sand in `common/src/main/resources/data/worldgentest/loot_table/block/crystal_sand.json`
- [ ] T034 [P] Add custom blocks to block tags in `common/src/main/resources/data/minecraft/tags/block/mineable/shovel.json` (grass, dirt, sand)
- [ ] T035 [P] Add crystal log to block tags in `common/src/main/resources/data/minecraft/tags/block/mineable/axe.json`

### Custom Biomes (Terrablender Integration)

- [ ] T036 [P] Create Crystal Plains biome JSON in `common/src/main/resources/data/worldgentest/worldgen/biome/crystal_plains.json` (temperature=0.0, downfall=0.4, surface=CRYSTAL_GRASS_BLOCK, sky_color=0x8E44AD)
- [ ] T037 [P] Create Crystal Forest biome JSON in `common/src/main/resources/data/worldgentest/worldgen/biome/crystal_forest.json` (temperature=0.0, downfall=0.8, surface=CRYSTAL_GRASS_BLOCK, sky_color=0x8E44AD)
- [ ] T038 [P] Create Crystal Desert biome JSON in `common/src/main/resources/data/worldgentest/worldgen/biome/crystal_desert.json` (temperature=2.0, downfall=0.0, surface=CRYSTAL_SAND, sky_color=0x8E44AD)
- [ ] T039 [P] Create Crystal River biome JSON in `common/src/main/resources/data/worldgentest/worldgen/biome/crystal_river.json` (temperature=0.0, downfall=0.5, water_color=0x3FA3D3, sky_color=0x8E44AD)
- [ ] T040 Create CrystalDimensionRegion class (NeoForge, Mojang mapping) in `common/src/main/java/com/example/worldgentest/biome/CrystalDimensionRegion.java` (addBiomes with weight: Plains=40, Forest=30, Desert=20, River=10)
- [ ] T041 Create FabricCrystalDimensionRegion class (Fabric, Yarn mapping) in `fabric/src/main/java/com/example/worldgentest/biome/FabricCrystalDimensionRegion.java` (MultiNoiseUtil API)
- [ ] T042 Create NeoForge Terrablender initialization in `neoforge/src/main/java/com/example/worldgentest/biome/NeoForgeCrystalDimensionRegion.java` (FMLCommonSetupEvent registration)
- [ ] T043 Add Terrablender entrypoint in `fabric/src/main/resources/fabric.mod.json` ("terrablender": ["com.example.worldgentest.FabricTerraBlenderInit"])

### Localization

- [ ] T044 [P] Add English translations for custom blocks in `common/src/main/resources/assets/worldgentest/lang/en_us.json`
- [ ] T045 [P] Add Japanese translations for custom blocks in `common/src/main/resources/assets/worldgentest/lang/ja_jp.json`

**Checkpoint**: Foundation ready - dimension exists, custom blocks available, biomes integrated. Portal system can now be implemented.

---

## Phase 3: User Story 1 - Portal Frame Construction (Acceptance Scenarios 1) 🎯 MVP

**Goal**: Player can build portal frame with crystal blocks, system recognizes completed frame

**Independent Test**:
1. Launch `./gradlew fabric:runClient`
2. Create new world (Default world type)
3. Get crystal blocks from creative inventory
4. Build rectangular frame (minimum 2x3 inner space)
5. Verify frame structure is valid (next phase will activate it)

### Implementation for User Story 1

- [ ] T046 [P] [US1] Create CrystalPortalBlock class in `common/src/main/java/com/example/worldgentest/portal/CrystalPortalBlock.java` (AXIS property, transparent render, no collision)
- [ ] T047 [P] [US1] Create PortalFrame data class in `common/src/main/java/com/example/worldgentest/portal/PortalFrame.java` (bottomLeft, width, height, axis, isActive)
- [ ] T048 [US1] Create PortalFrameDetector class in `common/src/main/java/com/example/worldgentest/portal/PortalFrameDetector.java` (detectFrame method, validateFrame method, size constraints 2x3 to 21x21)
- [ ] T049 [US1] Implement frame edge validation in PortalFrameDetector (check all edges are crystal blocks, corners optional)
- [ ] T050 [US1] Implement interior space validation in PortalFrameDetector (check inner space is air or portal blocks)
- [ ] T051 [US1] Register portal block in `common/src/main/java/com/example/worldgentest/ModBlocks.java` (CRYSTAL_PORTAL)
- [ ] T052 [US1] Register portal block in Fabric: `fabric/src/main/java/com/example/worldgentest/FabricModBlocks.java`
- [ ] T053 [US1] Register portal block in NeoForge: `neoforge/src/main/java/com/example/worldgentest/ModBlocks.java`

### Portal Block Resources

- [ ] T054 [P] [US1] Create portal block texture in `common/src/main/resources/assets/worldgentest/textures/block/crystal_portal.png` (animated, purple-tinted)
- [ ] T055 [P] [US1] Create portal blockstate JSON in `common/src/main/resources/assets/worldgentest/blockstates/crystal_portal.json` (variants: axis=x, axis=z)
- [ ] T056 [P] [US1] Create portal block model JSON in `common/src/main/resources/assets/worldgentest/models/block/crystal_portal_ns.json`
- [ ] T057 [P] [US1] Create portal block model JSON in `common/src/main/resources/assets/worldgentest/models/block/crystal_portal_ew.json`
- [ ] T058 [P] [US1] Add portal block to block tags in `common/src/main/resources/data/minecraft/tags/block/mineable/pickaxe.json`

**Checkpoint**: Portal frame detection works, portal blocks are registered and ready for activation

---

## Phase 4: User Story 2 - Portal Activation (Acceptance Scenarios 2-3)

**Goal**: Player can activate portal with crystal shard, portal blocks appear, player can teleport to custom dimension

**Independent Test**:
1. Build portal frame (from US1)
2. Right-click frame with crystal shard
3. Verify purple particles appear
4. Verify portal blocks fill inner space
5. Stand in portal for 4 seconds
6. Verify teleport to custom dimension

### Implementation for User Story 2

- [ ] T059 [P] [US2] Create PortalActivationHandler class in `common/src/main/java/com/example/worldgentest/portal/PortalActivationHandler.java` (handles UseOnContext event)
- [ ] T060 [US2] Implement portal ignition logic in PortalActivationHandler (detect frame, check crystal shard, fill portal blocks)
- [ ] T061 [US2] Add purple particle effects on portal activation (FR-008 requirement)
- [ ] T062 [US2] Add sound effects on portal activation (FR-008 requirement, similar to nether portal)
- [ ] T063 [US2] Register interaction event handler in Fabric: `fabric/src/main/java/com/example/worldgentest/FabricModInitializer.java` (UseBlockCallback.EVENT)
- [ ] T064 [US2] Register interaction event handler in NeoForge: `neoforge/src/main/java/com/example/worldgentest/ModEvents.java` (PlayerInteractEvent.RightClickBlock)
- [ ] T065 [P] [US2] Create CrystalDimensionTeleporter class in `common/src/main/java/com/example/worldgentest/dimension/CrystalDimensionTeleporter.java` (coordinate scaling 1:4, spawn position calculation)
- [ ] T066 [US2] Implement coordinate scaling logic in CrystalDimensionTeleporter (Overworld→Dimension: divide by 4, Dimension→Overworld: multiply by 4)
- [ ] T067 [US2] Implement safe spawn position finder in CrystalDimensionTeleporter (y=64 base, search for solid blocks, FR-016 requirement)
- [ ] T068 [US2] Implement entity dwell time tracking in CrystalPortalBlock (80 ticks = 4 seconds, FR-004 requirement)
- [ ] T069 [US2] Create platform-specific teleport method interface using @ExpectPlatform in `common/src/main/java/com/example/worldgentest/dimension/PlatformTeleporter.java`
- [ ] T070 [US2] Implement Fabric teleport method in `fabric/src/main/java/com/example/worldgentest/dimension/PlatformTeleporterImpl.java` (FabricDimensions.teleport)
- [ ] T071 [US2] Implement NeoForge teleport method in `neoforge/src/main/java/com/example/worldgentest/dimension/PlatformTeleporterImpl.java` (player.teleportTo)
- [ ] T072 [US2] Integrate teleporter with portal block entity collision detection in CrystalPortalBlock

**Checkpoint**: Portal activation works, player can teleport to custom dimension with correct coordinate scaling

---

## Phase 5: User Story 3 - Portal Return and Linking (Acceptance Scenarios 4)

**Goal**: Player can return from custom dimension to overworld using same portal

**Independent Test**:
1. Teleport to custom dimension (from US2)
2. Enter portal again from inside dimension
3. Verify return to overworld at linked position (1:4 scaling)

### Implementation for User Story 3

- [ ] T073 [P] [US3] Create PortalForcer class in `common/src/main/java/com/example/worldgentest/dimension/PortalForcer.java` (portal linking, auto-generation if needed)
- [ ] T074 [US3] Implement portal search logic in PortalForcer (search for existing portal within 128 block radius in target dimension)
- [ ] T075 [US3] Implement portal auto-generation in PortalForcer (create new portal at calculated position if none found)
- [ ] T076 [US3] Add portal direction preservation in PortalForcer (maintain axis orientation between linked portals)
- [ ] T077 [US3] Integrate PortalForcer with CrystalDimensionTeleporter (use findOrCreatePortal before teleporting)

**Checkpoint**: Two-way portal travel works with correct coordinate linking

---

## Phase 6: User Story 4 - Portal Destruction (Acceptance Scenario 6)

**Goal**: Breaking portal frame deactivates portal blocks

**Independent Test**:
1. Activate portal (from US2)
2. Break one crystal block from frame
3. Verify all portal blocks disappear

### Implementation for User Story 4

- [ ] T078 [US4] Implement neighborChanged handler in CrystalPortalBlock (detect frame integrity on block updates)
- [ ] T079 [US4] Implement BFS portal destruction in CrystalPortalBlock (destroyConnectedPortal method using Queue<BlockPos>)
- [ ] T080 [US4] Add validation check in destroyConnectedPortal (ensure surrounding frame blocks still exist before keeping portal active)

**Checkpoint**: Portal destruction works correctly when frame is broken

---

## Phase 7: User Story 5 - Custom Biome Exploration (Acceptance Scenarios 5, 10)

**Goal**: Player can explore 4 different biomes in custom dimension (Plains, Forest, Desert, River)

**Independent Test**:
1. Enter custom dimension
2. Use `/locate biome worldgentest:crystal_plains` (and other 3 biomes)
3. Travel to each biome location
4. Verify biome-specific blocks (grass, trees, sand, water)
5. Verify biome distribution roughly matches 40%/30%/20%/10%

### Implementation for User Story 5

- [ ] T081 [P] [US5] Create configured feature for crystal trees in `common/src/main/resources/data/worldgentest/worldgen/configured_feature/crystal_tree.json` (uses CRYSTAL_LOG)
- [ ] T082 [P] [US5] Create placed feature for crystal trees in `common/src/main/resources/data/worldgentest/worldgen/placed_feature/crystal_tree_placed.json` (placement in Forest biome)
- [ ] T083 [US5] Add tree feature reference to Crystal Forest biome JSON (`crystal_forest.json` features section)
- [ ] T084 [US5] Verify biome parameter ranges in level stem JSON match target distribution (Plains 40%, Forest 30%, Desert 20%, River 10%)

**Checkpoint**: All 4 biomes are explorable and visually distinct with correct block types

---

## Phase 8: User Story 6 - Respawn System (Acceptance Scenarios 7-8)

**Goal**: Player can set respawn point in custom dimension using bed, respawns correctly on death

**Independent Test**:
1. Enter custom dimension
2. Place bed in custom dimension
3. Sleep in bed (sets respawn point)
4. Die in custom dimension (e.g., jump off cliff)
5. Verify respawn at bed location in custom dimension

### Implementation for User Story 6

- [ ] T085 [US6] Verify bed_works=true in dimension type JSON (`crystal_dimension.json` in dimension_type/)
- [ ] T086 [US6] Test respawn behavior with bed placement in custom dimension (runtime test)
- [ ] T087 [US6] Test respawn behavior without bed (should respawn at world spawn in overworld, FR-012 requirement)

**Checkpoint**: Respawn system works correctly with beds in custom dimension

---

## Phase 9: User Story 7 - Day/Night Cycle (Acceptance Scenario 9)

**Goal**: Custom dimension always shows daytime with purple-tinted sky

**Independent Test**:
1. Enter custom dimension
2. Wait for extended period (or use /time add)
3. Verify light level stays at maximum (day brightness)
4. Verify sky color appears purple-tinted

### Implementation for User Story 7

- [ ] T088 [US7] Verify fixed_time=6000 in dimension type JSON (`crystal_dimension.json`, ensures constant noon)
- [ ] T089 [US7] Verify sky_color=0x8E44AD in all biome JSONs (purple tint)
- [ ] T090 [US7] Test time behavior in dimension (runtime test, verify no night transition)

**Checkpoint**: Day/night cycle is fixed at daytime with purple sky

---

## Phase 10: User Story 8 - Water Color in River Biome (Acceptance Scenario 11)

**Goal**: Water in Crystal River biome appears light cyan color

**Independent Test**:
1. Enter custom dimension
2. Locate Crystal River biome (`/locate biome worldgentest:crystal_river`)
3. Observe water blocks
4. Verify light cyan color (0x3FA3D3)

### Implementation for User Story 8

- [ ] T091 [US8] Verify water_color=0x3FA3D3 in Crystal River biome JSON (`crystal_river.json`)
- [ ] T092 [US8] Test water rendering in river biome (runtime test, verify color appears correctly)

**Checkpoint**: Water color in river biome is visually distinct (light cyan)

---

## Phase 11: Polish & Cross-Cutting Concerns

**Purpose**: Final improvements and validation

- [ ] T093 [P] Add comprehensive logging for error cases in portal system (NFR-005: errors only, with coordinates and dimension info)
- [ ] T094 [P] Add translations for custom dimension name in `common/src/main/resources/assets/worldgentest/lang/en_us.json` ("dimension.worldgentest.crystal_dimension": "Crystal Dimension")
- [ ] T095 [P] Add translations for custom dimension name in `common/src/main/resources/assets/worldgentest/lang/ja_jp.json` ("dimension.worldgentest.crystal_dimension": "クリスタルディメンション")
- [ ] T096 [P] Add translations for portal block in language files
- [ ] T097 Validate all JSON files using Misode's generators (dimension_type, dimension, biomes)
- [ ] T098 Validate quickstart.md instructions by following them step-by-step
- [ ] T099 Test multiplayer behavior (NFR-003: multiple players using portals simultaneously)
- [ ] T100 Performance test large portal (21x21 inner space, verify activation <50ms, NFR-001: teleport <200ms)
- [ ] T101 Test edge case: incomplete frame with ignition item (should do nothing)
- [ ] T102 Test edge case: multiple entities entering portal simultaneously (should all teleport)
- [ ] T103 Test edge case: portal linking with multiple nearby portals (document expected behavior)
- [ ] T104 Test edge case: command-based dimension entry without portal (`/execute in worldgentest:crystal_dimension`)
- [ ] T105 Test emergency spawn platform generation (FR-016: when no solid block found within 64 blocks of y=64)

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3-10)**: All depend on Foundational phase completion
  - US1 (Portal Frame): Independent after Foundation
  - US2 (Activation): Depends on US1
  - US3 (Return): Depends on US2
  - US4 (Destruction): Depends on US1, US2
  - US5 (Biomes): Independent after Foundation (parallel with US1-4)
  - US6 (Respawn): Depends on US2 (need dimension access)
  - US7 (Day/Night): Independent after Foundation (validation only)
  - US8 (Water): Depends on US5 (biome exploration)
- **Polish (Phase 11)**: Depends on all user stories being complete

### User Story Dependencies

- **US1 (Portal Frame)**: Foundation → US1 (Independent)
- **US2 (Activation)**: Foundation → US1 → US2
- **US3 (Return)**: Foundation → US1 → US2 → US3
- **US4 (Destruction)**: Foundation → US1 → US2 → US4
- **US5 (Biomes)**: Foundation → US5 (Independent, can parallel with US1-4)
- **US6 (Respawn)**: Foundation → US2 → US6
- **US7 (Day/Night)**: Foundation → US7 (Validation only)
- **US8 (Water)**: Foundation → US5 → US8

### Critical Path

1. Setup (Phase 1) → Foundation (Phase 2)
2. Foundation → US1 → US2 → US3 (MVP: Portal system working)
3. Parallel: US4, US5, US6, US7, US8 can proceed after their dependencies
4. Polish (Phase 11) after all stories complete

### Parallel Opportunities

- **Setup Phase**: T002, T003 can run in parallel
- **Foundation Phase**:
  - T004, T005 (dimension JSONs) can run in parallel
  - T007-T010 (custom block classes) can run in parallel
  - T014-T017 (textures) can run in parallel
  - T018-T021 (blockstates) can run in parallel
  - T022-T025 (block models) can run in parallel
  - T026-T029 (item models) can run in parallel
  - T030-T033 (loot tables) can run in parallel
  - T034, T035 (block tags) can run in parallel
  - T036-T039 (biome JSONs) can run in parallel
  - T044, T045 (translations) can run in parallel
- **User Story 1**: T046, T047 can run in parallel; T054-T058 (resources) can run in parallel
- **User Story 2**: T059, T065 can run in parallel
- **User Story 5**: T081, T082 can run in parallel
- **Polish Phase**: T093-T096 (logging and translations) can run in parallel

---

## Parallel Example: Foundation Phase

```bash
# Launch all custom block class implementations together:
Task: "Create CrystalGrassBlock class in common/src/main/java/com/example/worldgentest/block/CrystalGrassBlock.java"
Task: "Create CrystalDirtBlock class in common/src/main/java/com/example/worldgentest/block/CrystalDirtBlock.java"
Task: "Create CrystalLogBlock class in common/src/main/java/com/example/worldgentest/block/CrystalLogBlock.java"
Task: "Create CrystalSandBlock class in common/src/main/java/com/example/worldgentest/block/CrystalSandBlock.java"

# Launch all biome JSON creations together:
Task: "Create Crystal Plains biome JSON in common/src/main/resources/data/worldgentest/worldgen/biome/crystal_plains.json"
Task: "Create Crystal Forest biome JSON in common/src/main/resources/data/worldgentest/worldgen/biome/crystal_forest.json"
Task: "Create Crystal Desert biome JSON in common/src/main/resources/data/worldgentest/worldgen/biome/crystal_desert.json"
Task: "Create Crystal River biome JSON in common/src/main/resources/data/worldgentest/worldgen/biome/crystal_river.json"
```

---

## Implementation Strategy

### MVP First (Portal System Core)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
3. Complete Phase 3: User Story 1 (Portal Frame)
4. Complete Phase 4: User Story 2 (Portal Activation & Teleport)
5. Complete Phase 5: User Story 3 (Portal Return)
6. **STOP and VALIDATE**: Test portal system end-to-end
7. Deploy/demo if ready (Core feature working)

### Incremental Delivery

1. Complete Setup + Foundational → Custom dimension exists, biomes work
2. Add US1 + US2 + US3 → Portal system working (MVP!)
3. Add US4 → Portal destruction working
4. Add US5 → Full biome exploration
5. Add US6-US8 → Polish features (respawn, aesthetics)
6. Phase 11 → Final validation and edge cases

### Parallel Team Strategy

With multiple developers after Foundation completes:

1. **Developer A**: US1 → US2 → US3 (Portal system)
2. **Developer B**: US5 (Biome features - independent)
3. **Developer C**: US7 → US8 (Aesthetic validation - independent)
4. After portal system (A) completes: Developer A can do US4, Developer B can do US6

---

## Notes

- **[P] tasks**: Different files, no dependencies, can run in parallel
- **[Story] label**: Maps task to acceptance scenario for traceability
- **Runtime testing**: Most validation happens via `./gradlew runClient` since Minecraft APIs are hard to unit test
- **Terrablender pattern**: Reuse existing Crystalline Caves implementation pattern for mapping differences
- **Coordinate scaling**: Overworld→Dimension = divide by 4, Dimension→Overworld = multiply by 4
- **Commit frequently**: After each task or logical group (e.g., all textures for one block)
- **JSON validation**: Use Misode's generators before committing JSON files
- **Stop at checkpoints**: Validate each user story independently before moving to next
- **Performance targets**: Portal activation <50ms, teleport <200ms, chunk generation = vanilla speed

---

## Estimated Implementation Time

- **Phase 1 (Setup)**: 1 hour
- **Phase 2 (Foundation)**: 12-16 hours (custom blocks + biomes + resources)
- **Phase 3 (US1)**: 4-6 hours (portal frame detection)
- **Phase 4 (US2)**: 8-10 hours (portal activation + teleport)
- **Phase 5 (US3)**: 4-6 hours (portal linking)
- **Phase 6 (US4)**: 2-3 hours (portal destruction)
- **Phase 7 (US5)**: 3-4 hours (biome features)
- **Phase 8 (US6)**: 2-3 hours (respawn testing)
- **Phase 9 (US7)**: 1-2 hours (time validation)
- **Phase 10 (US8)**: 1-2 hours (water color validation)
- **Phase 11 (Polish)**: 6-8 hours (edge cases + validation)

**Total Estimated Time**: 44-61 hours

**MVP Time (US1+US2+US3)**: ~20-25 hours (Setup + Foundation + Portal System)
