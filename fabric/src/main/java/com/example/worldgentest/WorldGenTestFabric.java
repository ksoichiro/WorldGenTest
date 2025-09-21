package com.example.worldgentest;

import net.fabricmc.api.ModInitializer;

public class WorldGenTestFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // エンティティの登録（最初に行う）
        FabricModEntities.register(); // yarn mappingで復活

        // Fabric固有の登録処理
        FabricModItems.register();
        FabricModCreativeTabs.register();

        // バイオーム関連の登録
        FabricFeatures.register();
        FabricBiomes.register();
        FabricBiomes.modifyBiomes();

        // カスタムコマンドの登録
        // FabricCommands.register(); // yarn mapping移行中のため一時的に無効化
    }
}