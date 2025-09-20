package com.example.worldgentest.client.model;

import com.example.worldgentest.WorldGenTest;
import com.example.worldgentest.entity.CrystalGolemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CrystalGolemModel<T extends CrystalGolemEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(WorldGenTest.MOD_ID, "crystal_golem"), "main");

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

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create()
                .texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
                //.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), // hat layer
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0524F, -0.1745F, 0.0F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create()
                .texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, -1.3963F, -0.0873F, 0.0F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create()
                .texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, -1.309F, 0.0873F, 0.0F));

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.9F, 12.0F, 0.0F, -0.4363F, 0.0F, 0.0873F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create()
                .texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(1.9F, 12.0F, 0.0F, 0.3491F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.Head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.Head.xRot = headPitch * ((float)Math.PI / 180F);

        this.RightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.LeftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;

        this.RightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.LeftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}