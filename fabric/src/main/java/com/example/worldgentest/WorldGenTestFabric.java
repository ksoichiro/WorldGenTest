package com.example.worldgentest;

import net.fabricmc.api.ModInitializer;

public class WorldGenTestFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Fabric固有の登録処理
        FabricModBlocks.register();
        FabricModCreativeTabs.register();

        // バイオーム関連の登録
        FabricFeatures.register();
        FabricBiomes.register();
        FabricBiomes.modifyBiomes();

        // カスタムコマンドの登録
        FabricCommands.register();
    }
}