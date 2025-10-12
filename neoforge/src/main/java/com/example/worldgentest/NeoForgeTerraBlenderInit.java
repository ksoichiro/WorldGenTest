package com.example.worldgentest;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import terrablender.api.Regions;

/**
 * NeoForge用のTerraBlender初期化クラス
 */
public class NeoForgeTerraBlenderInit {

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(NeoForgeTerraBlenderInit::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Crystalline Cavesバイオームを持つRegionを登録
            // weight=2: 低めの重みで設定（バニラバイオームを圧迫しない）
            Regions.register(new CrystallineCavesRegion(
                ResourceLocation.parse("worldgentest:overworld_region"),
                2
            ));
        });
    }
}
