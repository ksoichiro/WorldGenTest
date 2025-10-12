package com.example.worldgentest;

import net.minecraft.util.Identifier;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

/**
 * Fabric用のTerraBlender初期化クラス
 */
public class FabricTerraBlenderInit implements TerraBlenderApi {

    @Override
    public void onTerraBlenderInitialized() {
        // Crystalline Cavesバイオームを持つRegionを登録
        // weight=2: 低めの重みで設定（バニラバイオームを圧迫しない）
        Regions.register(new FabricCrystallineCavesRegion(
            Identifier.of("worldgentest", "overworld_region"),
            2
        ));
    }
}
