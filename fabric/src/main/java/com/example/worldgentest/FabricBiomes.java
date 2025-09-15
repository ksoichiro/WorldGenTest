package com.example.worldgentest;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class FabricBiomes {

    // バイオームキーの定義（データパックからの参照用）
    public static final ResourceKey<Biome> CRYSTALLINE_CAVES = ResourceKey.create(
        Registries.BIOME,
        ResourceLocation.fromNamespaceAndPath("worldgentest", "crystalline_caves")
    );

    public static void register() {
        // Fabricでは、バイオームの実際の登録はdatapackファイルで行われる
        // プログラム的な登録はMinecraft 1.21.1では推奨されない
        System.out.println("Fabric biomes initialized - datapack biomes will be loaded automatically");

        // サーバー起動時にバイオーム登録を確認
        ServerLifecycleEvents.SERVER_STARTED.register(FabricBiomes::onServerStarted);
    }

    private static void onServerStarted(MinecraftServer server) {
        RegistryAccess registryAccess = server.registryAccess();
        Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registries.BIOME);

        // 登録されたバイオーム一覧を確認
        System.out.println("Checking for WorldGenTest biomes:");
        boolean foundCustomBiomes = false;
        for (var biomeKey : biomeRegistry.keySet()) {
            if (biomeKey.toString().contains("worldgentest") ||
                biomeKey.toString().contains("crystalline")) {
                System.out.println("  - " + biomeKey);
                foundCustomBiomes = true;
            }
        }

        if (!foundCustomBiomes) {
            System.out.println("No custom biomes found. They should be loaded via datapack.");
        } else {
            System.out.println("Custom biomes loaded successfully!");
        }
    }

    public static void modifyBiomes() {
        // BiomeModifications APIは使用しない
        // Minecraft 1.21.1ではdatapackによる定義で十分
        System.out.println("Fabric biome modifications initialized");
    }
}