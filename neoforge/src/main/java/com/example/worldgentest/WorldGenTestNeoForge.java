package com.example.worldgentest;

import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

@Mod("worldgentest")
public class WorldGenTestNeoForge {
    public static final String MOD_ID = "worldgentest";

    public WorldGenTestNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        // NeoForge固有の登録処理
        ModBlocks.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        // バイオーム関連の登録
        NeoForgeFeatures.register(modEventBus);
        NeoForgeBiomes.register(modEventBus);
    }
}