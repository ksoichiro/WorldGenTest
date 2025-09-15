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

        // バイオームを直接登録（実行時登録）
        try {
            // CrystallineCavesBiomeクラスからバイオーム作成
            Biome crystallineCaves = CrystallineCavesBiome.create();

            System.out.println("Crystalline Caves biome created successfully");
            System.out.println("Biome temperature: " + crystallineCaves.getBaseTemperature());
            System.out.println("Biome precipitation: " + crystallineCaves.hasPrecipitation());

            // 既存のバイオーム一覧を表示
            System.out.println("Currently registered biomes:");
            for (var biomeKey : biomeRegistry.keySet()) {
                if (biomeKey.toString().contains("worldgentest") ||
                    biomeKey.toString().contains("crystalline")) {
                    System.out.println("  - " + biomeKey);
                }
            }

        } catch (Exception e) {
            System.out.println("Error creating Crystalline Caves biome: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void modifyBiomes() {
        // BiomeModifications APIは使用しない
        // Minecraft 1.21.1ではdatapackによる定義で十分
        System.out.println("Fabric biome modifications initialized");
    }
}