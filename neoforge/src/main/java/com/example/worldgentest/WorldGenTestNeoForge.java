package com.example.worldgentest;

import com.example.worldgentest.entity.CrystalGolemEntity;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@Mod("worldgentest")
public class WorldGenTestNeoForge {
    public static final String MOD_ID = "worldgentest";

    public WorldGenTestNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        // Common初期化
        WorldGenTest.init();

        // NeoForge固有の登録処理
        ModBlocks.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        // エンティティ登録
        NeoForgeModEntities.register(modEventBus);

        // バイオーム関連の登録
        NeoForgeFeatures.register(modEventBus);
        NeoForgeBiomes.register(modEventBus);

        // エンティティ属性登録イベント
        modEventBus.addListener(this::entityAttributes);
    }

    @SubscribeEvent
    public void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(NeoForgeModEntities.CRYSTAL_GOLEM.get(), CrystalGolemEntity.createAttributes().build());
    }
}