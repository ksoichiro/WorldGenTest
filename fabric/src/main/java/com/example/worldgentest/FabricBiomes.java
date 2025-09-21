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
        // Biome registration will be handled by datapack
    }

    public static void modifyBiomes() {
        // バイオーム生成はデータパックで処理される
    }
}