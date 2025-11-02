package com.example.worldgentest.dimension;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.world.TeleportTarget;

/**
 * Fabric-specific teleportation implementation.
 * Uses Yarn mapping.
 */
public class PlatformTeleporterImpl {

    /**
     * Teleports an entity using Entity.teleportTo (Minecraft 1.21.1+).
     *
     * Note: Parameters come from Mojang-mapped common module but are received as Objects
     * to avoid compile-time mapping conflicts. We cast them to Yarn types at runtime.
     */
    public static void teleportEntity(Object entityObj, Object levelObj, Object posObj) {
        // Cast to Yarn-mapped types
        Entity entity = (Entity) entityObj;
        ServerWorld targetWorld = (ServerWorld) levelObj;

        // Extract position coordinates (works across mappings)
        int x, y, z;
        try {
            // Use reflection-free approach: get coordinates via methods that exist in both mappings
            x = (int) posObj.getClass().getMethod("getX").invoke(posObj);
            y = (int) posObj.getClass().getMethod("getY").invoke(posObj);
            z = (int) posObj.getClass().getMethod("getZ").invoke(posObj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract position coordinates", e);
        }

        BlockPos targetPos = new BlockPos(x, y, z);
        Vec3d targetVec = Vec3d.ofBottomCenter(targetPos);

        if (entity instanceof ServerPlayerEntity player) {
            // Special handling for players
            player.teleport(
                targetWorld,
                targetVec.x,
                targetVec.y,
                targetVec.z,
                player.getYaw(),
                player.getPitch()
            );
        } else {
            // Generic entity teleportation using TeleportTarget
            TeleportTarget target = new TeleportTarget(
                targetWorld,
                targetVec,
                entity.getVelocity(),
                entity.getYaw(),
                entity.getPitch(),
                TeleportTarget.NO_OP
            );
            entity.teleportTo(target);
        }
    }
}
