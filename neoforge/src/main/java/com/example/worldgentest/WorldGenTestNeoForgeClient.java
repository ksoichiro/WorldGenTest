package com.example.worldgentest;

import com.example.worldgentest.client.model.CrystalGolemModel;
import com.example.worldgentest.client.renderer.CrystalGolemRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = WorldGenTest.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class WorldGenTestNeoForgeClient {

    public static final ModelLayerLocation CRYSTAL_GOLEM_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(WorldGenTest.MOD_ID, "crystal_golem"), "main");

    @SubscribeEvent
    public static void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CRYSTAL_GOLEM_LAYER, CrystalGolemModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NeoForgeModEntities.CRYSTAL_GOLEM.get(), CrystalGolemRenderer::new);
    }
}