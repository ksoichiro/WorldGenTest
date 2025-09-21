package com.example.worldgentest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import com.example.worldgentest.client.renderer.CrystalGolemRenderer;
import com.example.worldgentest.client.model.CrystalGolemModel;

public class WorldGenTestFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // エンティティレンダラー登録
        EntityModelLayerRegistry.registerModelLayer(CrystalGolemModel.LAYER_LOCATION, CrystalGolemModel::createBodyLayer);
        EntityRendererRegistry.register(FabricModEntities.CRYSTAL_GOLEM, CrystalGolemRenderer::new);
    }
}