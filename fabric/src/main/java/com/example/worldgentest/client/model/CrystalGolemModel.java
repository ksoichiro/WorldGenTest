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

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public CrystalGolemModel(ModelPart root) {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.leftArm = root.getChild("left_arm");
        this.rightArm = root.getChild("right_arm");
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg = root.getChild("right_leg");
    }

    public static TexturedModelData createBodyLayer() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create()
                .uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                ModelTransform.pivot(0.0F, 4.0F, 0.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create()
                .uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F),
                ModelTransform.pivot(0.0F, 4.0F, 0.0F));

        ModelPartData leftArm = modelPartData.addChild("left_arm", ModelPartBuilder.create()
                .uv(40, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.pivot(5.0F, 6.0F, 0.0F));

        ModelPartData rightArm = modelPartData.addChild("right_arm", ModelPartBuilder.create()
                .uv(40, 16).mirrored().cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.pivot(-5.0F, 6.0F, 0.0F));

        ModelPartData leftLeg = modelPartData.addChild("left_leg", ModelPartBuilder.create()
                .uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F),
                ModelTransform.pivot(2.0F, 16.0F, 0.0F));

        ModelPartData rightLeg = modelPartData.addChild("right_leg", ModelPartBuilder.create()
                .uv(0, 16).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F),
                ModelTransform.pivot(-2.0F, 16.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yaw = netHeadYaw * ((float)Math.PI / 180F);
        this.head.pitch = headPitch * ((float)Math.PI / 180F);

        this.rightArm.pitch = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.leftArm.pitch = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;

        this.rightLeg.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.pitch = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        head.render(matrixStack, vertexConsumer, light, overlay, color);
        body.render(matrixStack, vertexConsumer, light, overlay, color);
        leftArm.render(matrixStack, vertexConsumer, light, overlay, color);
        rightArm.render(matrixStack, vertexConsumer, light, overlay, color);
        leftLeg.render(matrixStack, vertexConsumer, light, overlay, color);
        rightLeg.render(matrixStack, vertexConsumer, light, overlay, color);
    }
}