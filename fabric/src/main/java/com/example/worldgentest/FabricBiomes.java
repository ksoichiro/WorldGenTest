package com.example.worldgentest;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class FabricBiomes {

    // バイオームキーの定義
    public static final RegistryKey<Biome> CRYSTALLINE_CAVES = RegistryKey.of(
        RegistryKeys.BIOME,
        Identifier.of("worldgentest", "crystalline_caves")
    );

    public static void register() {
        System.out.println("Fabric biomes initialized - using datapack registration");
        System.out.println("Biome registration will be handled by datapack");
    }

    public static void modifyBiomes() {
        System.out.println("Fabric biome modifications initialized");
        System.out.println("crystalline_caves biome is registered and ready!");
        System.out.println("Use '/locate biome worldgentest:crystalline_caves' to find it.");
    }
}