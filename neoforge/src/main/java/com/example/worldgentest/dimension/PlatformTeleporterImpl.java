package com.example.worldgentest.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

/**
 * NeoForge-specific teleportation implementation.
 */
public class PlatformTeleporterImpl {

    /**
     * Teleports an entity using NeoForge's built-in teleportation methods.
     *
     * Note: Parameters come as typed objects since NeoForge uses Mojang mapping
     * (same as common module), so no mapping conversion is needed.
     */
    public static void teleportEntity(Object entityObj, Object levelObj, Object posObj) {
        // Cast to Mojang-mapped types (direct cast since both are Mojang mapping)
        Entity entity = (Entity) entityObj;
        ServerLevel targetLevel = (ServerLevel) levelObj;
        BlockPos targetPos = (BlockPos) posObj;

        Vec3 targetVec = Vec3.atBottomCenterOf(targetPos);

        if (entity instanceof ServerPlayer player) {
            // Special handling for players
            player.teleportTo(
                targetLevel,
                targetVec.x,
                targetVec.y,
                targetVec.z,
                player.getYRot(),
                player.getXRot()
            );
        } else {
            // Generic entity teleportation using DimensionTransition
            DimensionTransition transition = new DimensionTransition(
                targetLevel,
                targetVec,
                entity.getDeltaMovement(),
                entity.getYRot(),
                entity.getXRot(),
                DimensionTransition.DO_NOTHING
            );
            Entity teleportedEntity = entity.changeDimension(transition);
            // Entity is automatically moved to target position by changeDimension
        }
    }
}
