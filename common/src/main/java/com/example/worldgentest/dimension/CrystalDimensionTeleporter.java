package com.example.worldgentest.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles teleportation logic between Overworld and Crystal Dimension.
 *
 * Features:
 * - Coordinate scaling: Overworld→Dimension = divide by 4, Dimension→Overworld = multiply by 4
 * - Safe spawn position finding: searches for solid ground near target coordinates
 * - Emergency spawn platform generation: creates obsidian platform if no safe ground found
 */
public class CrystalDimensionTeleporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrystalDimensionTeleporter.class);

    // Coordinate scaling factor (1:4 ratio, similar to Nether)
    private static final double COORDINATE_SCALE = 4.0;

    // Default Y level for spawn position search
    private static final int DEFAULT_SPAWN_Y = 64;

    // Maximum vertical search distance for solid ground
    private static final int MAX_VERTICAL_SEARCH = 64;

    /**
     * Calculates the target position in the destination dimension with coordinate scaling.
     *
     * @param entity The entity being teleported
     * @param sourceDimension The dimension the entity is coming from
     * @param targetDimension The dimension the entity is going to
     * @return Target position with appropriate coordinate scaling applied
     */
    public static BlockPos calculateTargetPosition(Entity entity, ServerLevel sourceDimension, ServerLevel targetDimension) {
        Vec3 sourcePos = entity.position();

        double targetX;
        double targetZ;

        // Check if going to or from Crystal Dimension
        boolean goingToCrystalDimension = isCrystalDimension(targetDimension);
        boolean comingFromCrystalDimension = isCrystalDimension(sourceDimension);

        if (goingToCrystalDimension && !comingFromCrystalDimension) {
            // Overworld → Crystal Dimension: divide by 4
            targetX = sourcePos.x / COORDINATE_SCALE;
            targetZ = sourcePos.z / COORDINATE_SCALE;
        } else if (!goingToCrystalDimension && comingFromCrystalDimension) {
            // Crystal Dimension → Overworld: multiply by 4
            targetX = sourcePos.x * COORDINATE_SCALE;
            targetZ = sourcePos.z * COORDINATE_SCALE;
        } else {
            // Same dimension or other dimension pair: 1:1 scaling
            targetX = sourcePos.x;
            targetZ = sourcePos.z;
        }

        return BlockPos.containing(targetX, DEFAULT_SPAWN_Y, targetZ);
    }

    /**
     * Finds a safe spawn position near the target coordinates.
     * Searches vertically for solid ground within MAX_VERTICAL_SEARCH blocks.
     *
     * @param level The target dimension
     * @param targetPos The desired target position
     * @return Safe spawn position, or null if none found
     */
    public static BlockPos findSafeSpawnPosition(ServerLevel level, BlockPos targetPos) {
        // Search upward from target position
        for (int yOffset = 0; yOffset <= MAX_VERTICAL_SEARCH; yOffset++) {
            BlockPos checkPos = targetPos.offset(0, yOffset, 0);
            if (isSafeSpawnLocation(level, checkPos)) {
                return checkPos;
            }
        }

        // Search downward from target position
        for (int yOffset = -1; yOffset >= -MAX_VERTICAL_SEARCH; yOffset--) {
            BlockPos checkPos = targetPos.offset(0, yOffset, 0);
            if (isSafeSpawnLocation(level, checkPos)) {
                return checkPos;
            }
        }

        return null;
    }

    /**
     * Checks if a position is safe for entity spawning.
     * Requires: solid block below, 2 air blocks above for standing space.
     */
    private static boolean isSafeSpawnLocation(ServerLevel level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockPos above = pos.above();

        BlockState blockBelow = level.getBlockState(below);
        BlockState blockAt = level.getBlockState(pos);
        BlockState blockAbove = level.getBlockState(above);

        // Must have solid ground below
        if (!blockBelow.isSolid() || !blockBelow.blocksMotion()) {
            return false;
        }

        // Must have air for standing and head space
        if (!blockAt.isAir() || !blockAbove.isAir()) {
            return false;
        }

        // Must not spawn above dangerous blocks
        if (blockBelow.is(Blocks.LAVA) || blockBelow.is(Blocks.FIRE)) {
            return false;
        }

        return true;
    }

    /**
     * Creates an emergency spawn platform at the target position.
     * Used when no safe ground is found within search range.
     *
     * Platform structure: 3x3 obsidian platform with 2 air blocks above for standing.
     *
     * @param level The target dimension
     * @param centerPos The center of the platform
     * @return The safe spawn position on top of the platform
     */
    public static BlockPos createEmergencySpawnPlatform(ServerLevel level, BlockPos centerPos) {
        LOGGER.warn("Creating emergency spawn platform at {} in dimension {}",
            centerPos, level.dimension().location());

        // Create 3x3 obsidian platform
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos platformPos = centerPos.offset(x, -1, z);
                level.setBlock(platformPos, Blocks.OBSIDIAN.defaultBlockState(), 3);

                // Clear 2 blocks above for standing space
                level.setBlock(platformPos.above(), Blocks.AIR.defaultBlockState(), 3);
                level.setBlock(platformPos.above(2), Blocks.AIR.defaultBlockState(), 3);
            }
        }

        return centerPos;
    }

    /**
     * Checks if a level is the Crystal Dimension.
     */
    private static boolean isCrystalDimension(ServerLevel level) {
        String dimensionKey = level.dimension().location().toString();
        return dimensionKey.contains("crystal_dimension");
    }

    /**
     * Gets the final teleport position with all safety checks.
     * Will create emergency platform if necessary.
     *
     * @param targetLevel The destination dimension
     * @param targetPos The desired target position
     * @return Safe teleport position (guaranteed to be safe)
     */
    public static BlockPos getFinalTeleportPosition(ServerLevel targetLevel, BlockPos targetPos) {
        // Try to find safe spawn position
        BlockPos safePos = findSafeSpawnPosition(targetLevel, targetPos);

        if (safePos != null) {
            return safePos;
        }

        // No safe position found - create emergency platform
        LOGGER.warn("No safe spawn position found near {}, creating emergency platform", targetPos);
        return createEmergencySpawnPlatform(targetLevel, targetPos);
    }
}
