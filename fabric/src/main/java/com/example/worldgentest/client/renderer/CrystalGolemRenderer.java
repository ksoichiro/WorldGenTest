package com.example.worldgentest.client.renderer;

import com.example.worldgentest.WorldGenTest;
import com.example.worldgentest.entity.FabricCrystalGolemEntity;
import com.example.worldgentest.client.model.CrystalGolemModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class CrystalGolemRenderer extends MobEntityRenderer<FabricCrystalGolemEntity, CrystalGolemModel<FabricCrystalGolemEntity>> {
    private static final Identifier TEXTURE = Identifier.of(WorldGenTest.MOD_ID, "textures/entity/crystal_golem.png");

    public CrystalGolemRenderer(EntityRendererFactory.Context context) {
        super(context, new CrystalGolemModel<>(context.getPart(CrystalGolemModel.LAYER_LOCATION)), 0.7F);
    }

    @Override
    public Identifier getTexture(FabricCrystalGolemEntity entity) {
        return TEXTURE;
    }
}