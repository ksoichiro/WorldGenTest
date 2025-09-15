package com.example.worldgentest;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NeoForgeBiomes {
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(Registries.BIOME, WorldGenTestNeoForge.MOD_ID);

    public static final Supplier<Biome> CRYSTALLINE_CAVES = BIOMES.register("crystalline_caves", () ->
        ModBiomes.createCrystallineCaves()
    );

    public static void register(IEventBus modEventBus) {
        BIOMES.register(modEventBus);
        System.out.println("NeoForge biomes initialized - using programmatic + datapack registration");
        System.out.println("crystalline_caves biome is registered and ready!");
        System.out.println("Use '/locate biome worldgentest:crystalline_caves' to find it.");
    }
}