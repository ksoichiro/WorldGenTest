package com.example.worldgentest.dimension;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

/**
 * Platform-specific teleportation implementation.
 *
 * Uses Architectury's @ExpectPlatform pattern to delegate to
 * platform-specific implementations (Fabric vs NeoForge).
 *
 * Note: Platform implementations receive these as Objects to handle
 * Yarn/Mojang mapping differences at runtime.
 */
public class PlatformTeleporter {

    /**
     * Teleports an entity to the target dimension at the specified position.
     *
     * This method delegates to platform-specific implementations:
     * - Fabric: Uses FabricDimensions.teleport (Yarn mapping)
     * - NeoForge: Uses Entity.teleportTo with ServerLevel parameter (Mojang mapping)
     *
     * @param entity The entity to teleport
     * @param targetLevel The destination dimension
     * @param targetPos The destination position
     */
    @ExpectPlatform
    public static void teleportEntity(Entity entity, ServerLevel targetLevel, BlockPos targetPos) {
        throw new AssertionError("@ExpectPlatform method not implemented");
    }
}
