package com.example.worldgentest.client.renderer;

import com.example.worldgentest.WorldGenTest;
import com.example.worldgentest.WorldGenTestNeoForgeClient;
import com.example.worldgentest.client.model.CrystalGolemModel;
import com.example.worldgentest.entity.CrystalGolemEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CrystalGolemRenderer extends MobRenderer<CrystalGolemEntity, CrystalGolemModel<CrystalGolemEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(WorldGenTest.MOD_ID, "textures/entity/crystal_golem.png");

    public CrystalGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new CrystalGolemModel<>(context.bakeLayer(WorldGenTestNeoForgeClient.CRYSTAL_GOLEM_LAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(CrystalGolemEntity entity) {
        return TEXTURE;
    }
}