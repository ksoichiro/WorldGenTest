package com.example.worldgentest.client.model;

import com.example.worldgentest.WorldGenTest;
import com.example.worldgentest.entity.FabricCrystalGolemEntity;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class CrystalGolemModel<T extends FabricCrystalGolemEntity> extends EntityModel<T> {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(
            Identifier.of(WorldGenTest.MOD_ID, "crystal_golem"), "main");

    private final ModelPart Body;
    private final ModelPart Head;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;

    public CrystalGolemModel(ModelPart root) {
        this.Body = root.getChild("Body");
        this.Head = root.getChild("Head");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
    }

    public static TexturedModelData createBodyLayer() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData Body = modelPartData.addChild("Body", ModelPartBuilder.create()
                .uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData Head = modelPartData.addChild("Head", ModelPartBuilder.create()
                .uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                // .uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), // hat layer
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0524F, -0.1745F, 0.0F));

        ModelPartData RightArm = modelPartData.addChild("RightArm", ModelPartBuilder.create()
                .uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(-5.0F, 2.0F, 0.0F, -1.3963F, -0.0873F, 0.0F));

        ModelPartData LeftArm = modelPartData.addChild("LeftArm", ModelPartBuilder.create()
                .uv(40, 16).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(5.0F, 2.0F, 0.0F, -1.309F, 0.0873F, 0.0F));

        ModelPartData RightLeg = modelPartData.addChild("RightLeg", ModelPartBuilder.create()
                .uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(-1.9F, 12.0F, 0.0F, -0.4363F, 0.0F, 0.0873F));

        ModelPartData LeftLeg = modelPartData.addChild("LeftLeg", ModelPartBuilder.create()
                .uv(0, 16).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(1.9F, 12.0F, 0.0F, 0.3491F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.Head.yaw = netHeadYaw * ((float)Math.PI / 180F);
        this.Head.pitch = headPitch * ((float)Math.PI / 180F);

        this.RightArm.pitch = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.LeftArm.pitch = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;

        this.RightLeg.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.LeftLeg.pitch = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        Body.render(matrixStack, vertexConsumer, light, overlay, color);
        Head.render(matrixStack, vertexConsumer, light, overlay, color);
        RightArm.render(matrixStack, vertexConsumer, light, overlay, color);
        LeftArm.render(matrixStack, vertexConsumer, light, overlay, color);
        RightLeg.render(matrixStack, vertexConsumer, light, overlay, color);
        LeftLeg.render(matrixStack, vertexConsumer, light, overlay, color);
    }
}