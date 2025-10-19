package com.example.worldgentest.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class ModDimensions {
    public static final String MOD_ID = "worldgentest";

    // Dimension Type Key
    public static final ResourceKey<DimensionType> CRYSTAL_DIMENSION_TYPE = ResourceKey.create(
        Registries.DIMENSION_TYPE,
        ResourceLocation.fromNamespaceAndPath(MOD_ID, "crystal_dimension")
    );

    // Level (Dimension) Key
    public static final ResourceKey<Level> CRYSTAL_DIMENSION = ResourceKey.create(
        Registries.DIMENSION,
        ResourceLocation.fromNamespaceAndPath(MOD_ID, "crystal_dimension")
    );

    public static void register() {
        // Dimension registration is handled by JSON files in data/worldgentest/dimension_type/ and data/worldgentest/dimension/
        // This class only provides ResourceKey constants for code reference
    }
}
