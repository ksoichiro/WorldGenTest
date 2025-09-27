# Block System Contracts

**Date**: 2025-09-22
**System**: Crystal Stone Block System

## Block Registration Contract

### Interface: Block Definition
```java
// Common Block Interface
public interface CrystalBlockContract {
    // Block Properties Contract
    Block getCrystalStone();
    Block getCrystalCobblestone();
    Block getCrystalBricks();

    // Block Behavior Contract
    boolean requiresCorrectTool(Block block);
    ItemStack getDroppedItem(Block block, boolean hasSilkTouch);
    SoundType getSoundType(Block block);
}
```

### Platform-Specific Registration Contract
```java
// Fabric Implementation Contract
public interface FabricBlockContract extends CrystalBlockContract {
    void registerBlocks();
    void registerBlockItems();
}

// NeoForge Implementation Contract
public interface NeoForgeBlockContract extends CrystalBlockContract {
    DeferredRegister<Block> getBlockRegistry();
    DeferredRegister<Item> getItemRegistry();
    void setupRegistration();
}
```

## Block Properties Contract

### Crystal Stone Properties
```yaml
contract:
  name: "crystal_stone"
  properties:
    hardness: 1.5
    resistance: 6.0
    tool_type: "pickaxe"
    tool_required: true
    sound_type: "STONE"
    material: "STONE"
    light_level: 0
  drops:
    normal: "crystal_cobblestone"
    silk_touch: "crystal_stone"
```

### Crystal Cobblestone Properties
```yaml
contract:
  name: "crystal_cobblestone"
  properties:
    hardness: 2.0
    resistance: 6.0
    tool_type: "pickaxe"
    tool_required: false
    sound_type: "STONE"
    material: "STONE"
    light_level: 0
  drops:
    normal: "crystal_cobblestone"
    silk_touch: "crystal_cobblestone"
```

### Crystal Bricks Properties
```yaml
contract:
  name: "crystal_bricks"
  properties:
    hardness: 2.0
    resistance: 6.0
    tool_type: "pickaxe"
    tool_required: false
    sound_type: "STONE"
    material: "STONE"
    light_level: 0
  drops:
    normal: "crystal_bricks"
    silk_touch: "crystal_bricks"
```

## World Generation Contract

### Generation Interface
```java
public interface WorldGenContract {
    // Feature Definition Contract
    ConfiguredFeature<?, ?> getCrystalStoneFeature();
    PlacedFeature getCrystalStonePlacedFeature();

    // Biome Integration Contract
    void addToBiome(BiomeGenerationSettings.Builder builder);

    // Generation Parameters Contract
    GenerationStep.Decoration getGenerationStep();
    int getGenerationPriority();
}
```

### Generation Configuration Contract
```yaml
world_generation:
  crystal_stone_ore:
    type: "minecraft:ore"
    size: 12
    targets:
      - target: "minecraft:stone_ore_replaceables"
        state: "worldgentest:crystal_stone"
    placement:
      biome: "worldgentest:crystalline_caves"
      y_range: [0, 64]
      frequency: 8
      rarity: 16
```

## Recipe System Contract

### Recipe Interface
```java
public interface RecipeContract {
    // Smelting Contract
    RecipeHolder<SmeltingRecipe> getCrystalStoneSmeltingRecipe();
    RecipeHolder<BlastingRecipe> getCrystalStoneBlastingRecipe();

    // Crafting Contract
    RecipeHolder<CraftingRecipe> getCrystalBricksRecipe();

    // Recipe Validation Contract
    boolean validateRecipeInputs(ItemStack... inputs);
    ItemStack getRecipeOutput(RecipeType<?> type, ItemStack input);
}
```

### Smelting Recipe Contract
```json
{
  "contract": "smelting_crystal_stone",
  "type": "minecraft:smelting",
  "ingredient": {
    "item": "worldgentest:crystal_cobblestone"
  },
  "result": {
    "id": "worldgentest:crystal_stone",
    "count": 1
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

### Crafting Recipe Contract
```json
{
  "contract": "crystal_bricks_crafting",
  "type": "minecraft:crafting_shaped",
  "category": "decorations",
  "pattern": [
    "CC",
    "CC"
  ],
  "key": {
    "C": {
      "item": "worldgentest:crystal_stone"
    }
  },
  "result": {
    "id": "worldgentest:crystal_bricks",
    "count": 4
  }
}
```

## Advancement System Contract

### Advancement Interface
```java
public interface AdvancementContract {
    // Recipe Unlock Contract
    AdvancementHolder getSmeltingAdvancement();
    AdvancementHolder getCraftingAdvancement();

    // Trigger Contract
    void triggerRecipeUnlock(Player player, String recipeName);
    void triggerInventoryChanged(Player player, ItemStack... items);
}
```

### Advancement Definition Contract
```json
{
  "contract": "crystal_stone_smelting",
  "parent": "minecraft:recipes/misc/cobblestone",
  "rewards": {
    "recipes": ["worldgentest:smelting_crystal_stone"]
  },
  "criteria": {
    "has_crystal_cobblestone": {
      "trigger": "minecraft:inventory_changed",
      "conditions": {
        "items": [{"item": "worldgentest:crystal_cobblestone"}]
      }
    }
  }
}
```

## Asset System Contract

### Texture Interface
```java
public interface TextureContract {
    // Resource Location Contract
    ResourceLocation getCrystalStoneTexture();
    ResourceLocation getCrystalCobblestoneTexture();
    ResourceLocation getCrystalBricksTexture();

    // Generation Contract
    void generateTextures(Path inputPath, Path outputPath);
    BufferedImage processTexture(BufferedImage input, TextureProcessingMode mode);
}
```

### Model Definition Contract
```json
{
  "contract": "crystal_stone_blockstate",
  "variants": {
    "": {
      "model": "worldgentest:block/crystal_stone"
    }
  }
}
```

```json
{
  "contract": "crystal_stone_block_model",
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "worldgentest:block/crystal_stone"
  }
}
```

```json
{
  "contract": "crystal_stone_item_model",
  "parent": "worldgentest:block/crystal_stone"
}
```

## Localization Contract

### Language Interface
```java
public interface LocalizationContract {
    // Translation Key Contract
    String getBlockTranslationKey(String blockName);
    String getAdvancementTranslationKey(String advancementName);

    // Supported Languages Contract
    Set<String> getSupportedLanguages();
    Map<String, String> getTranslations(String language);
}
```

### Language Definition Contract
```json
{
  "contract": "localization_en_us",
  "translations": {
    "block.worldgentest.crystal_stone": "Crystal Stone",
    "block.worldgentest.crystal_cobblestone": "Crystal Cobblestone",
    "block.worldgentest.crystal_bricks": "Crystal Bricks",
    "advancement.worldgentest.crystal_stone_smelting": "Crystal Purification",
    "advancement.worldgentest.crystal_bricks_crafting": "Crystal Architecture"
  }
}
```

## Testing Contract

### Block Testing Interface
```java
public interface BlockTestContract {
    // Block Behavior Testing
    void testBlockHardness(Block block, float expectedHardness);
    void testBlockDrops(Block block, ItemStack expectedDrop, boolean silkTouch);
    void testToolRequirement(Block block, Item tool, boolean shouldWork);

    // Recipe Testing
    void testSmeltingRecipe(ItemStack input, ItemStack expectedOutput);
    void testCraftingRecipe(ItemStack[] inputs, ItemStack expectedOutput);

    // World Generation Testing
    void testBiomeGeneration(Biome biome, Block expectedBlock);
    void testGenerationFrequency(Biome biome, Block block, int expectedCount);
}
```

### Integration Testing Contract
```java
public interface IntegrationTestContract {
    // End-to-End Workflow Testing
    void testMiningWorkflow();      // Mine crystal stone → get cobblestone
    void testSmeltingWorkflow();    // Smelt cobblestone → get stone
    void testCraftingWorkflow();    // Craft stone → get bricks

    // Platform Compatibility Testing
    void testFabricCompatibility();
    void testNeoForgeCompatibility();
    void testCrossCompatibility();
}
```

## Error Handling Contract

### Error Interface
```java
public interface ErrorHandlingContract {
    // Registration Errors
    void handleBlockRegistrationFailure(String blockName, Exception cause);
    void handleRecipeRegistrationFailure(String recipeName, Exception cause);

    // Runtime Errors
    void handleInvalidDropBehavior(Block block, ItemStack actualDrop);
    void handleGenerationFailure(Biome biome, Exception cause);

    // Validation Errors
    void handleTextureLoadFailure(ResourceLocation texture, Exception cause);
    void handleModelLoadFailure(ResourceLocation model, Exception cause);
}
```

### Error Response Contract
```yaml
error_handling:
  registration_failure:
    action: "log_error_and_continue"
    fallback: "disable_block_registration"
    user_notification: true

  runtime_failure:
    action: "log_warning_and_fallback"
    fallback: "vanilla_behavior"
    user_notification: false

  asset_failure:
    action: "log_error_and_fallback"
    fallback: "missing_texture_placeholder"
    user_notification: true
```