package com.example.worldgentest;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.core.Holder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class NeoForgeFeatures {
    // ConfiguredFeatures
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
        DeferredRegister.create(Registries.CONFIGURED_FEATURE, WorldGenTestNeoForge.MOD_ID);

    // PlacedFeatures
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
        DeferredRegister.create(Registries.PLACED_FEATURE, WorldGenTestNeoForge.MOD_ID);

    // Crystal block ore configured feature
    public static final Supplier<ConfiguredFeature<?, ?>> CRYSTAL_BLOCK_ORE = CONFIGURED_FEATURES.register("crystal_block_ore", () ->
        new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
            new BlockMatchTest(net.minecraft.world.level.block.Blocks.STONE),
            ModBlocks.CRYSTAL_BLOCK.get().defaultBlockState(),
            9 // vein size
        ))
    );

    // Crystal block ore placed feature
    public static final Supplier<PlacedFeature> CRYSTAL_BLOCK_PLACED = PLACED_FEATURES.register("crystal_block_placed", () ->
        new PlacedFeature(Holder.direct(CRYSTAL_BLOCK_ORE.get()), List.of(
            CountPlacement.of(20), // number of veins per chunk
            InSquarePlacement.spread(),
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64)),
            BiomeFilter.biome()
        ))
    );

    public static void register(IEventBus modEventBus) {
        CONFIGURED_FEATURES.register(modEventBus);
        PLACED_FEATURES.register(modEventBus);
    }
}