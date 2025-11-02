package com.example.worldgentest.portal;

import com.example.worldgentest.dimension.CrystalDimensionTeleporter;
import com.example.worldgentest.dimension.PlatformTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Crystal Portal Block - the portal blocks that appear inside an activated portal frame.
 *
 * Features:
 * - Transparent rendering (no collision)
 * - AXIS property to match frame orientation
 * - Entity dwell time tracking (4 seconds = 80 ticks)
 * - Dimension teleportation after dwell time
 */
public class CrystalPortalBlock extends Block {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrystalPortalBlock.class);

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    // Dwell time required before teleportation (80 ticks = 4 seconds)
    private static final int PORTAL_DWELL_TIME = 80;

    // Map to track entity dwell times
    private static final Map<UUID, PortalDwellTracker> ENTITY_PORTAL_TIMES = new HashMap<>();

    public CrystalPortalBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // No collision - entities can pass through
        return Shapes.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // No collision
        return Shapes.empty();
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        // Server-side only
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        // Track entity dwell time
        UUID entityId = entity.getUUID();
        PortalDwellTracker tracker = ENTITY_PORTAL_TIMES.computeIfAbsent(entityId,
            id -> new PortalDwellTracker());

        tracker.incrementTime();

        // Teleport if dwell time reached
        if (tracker.getTime() >= PORTAL_DWELL_TIME) {
            tryTeleportEntity(entity, serverLevel);
            // Reset timer after teleport attempt
            tracker.reset();
        }
    }

    /**
     * Attempts to teleport an entity to the target dimension.
     */
    private void tryTeleportEntity(Entity entity, ServerLevel sourceLevel) {
        try {
            // Determine target dimension
            ServerLevel targetLevel = getTargetDimension(sourceLevel);

            if (targetLevel == null) {
                LOGGER.error("Failed to get target dimension for entity {} at {}",
                    entity.getName().getString(), entity.blockPosition());
                return;
            }

            // Calculate target position with coordinate scaling
            BlockPos targetPos = CrystalDimensionTeleporter.calculateTargetPosition(
                entity, sourceLevel, targetLevel);

            // Find safe spawn position (or create emergency platform)
            BlockPos safePos = CrystalDimensionTeleporter.getFinalTeleportPosition(
                targetLevel, targetPos);

            // Perform platform-specific teleportation
            PlatformTeleporter.teleportEntity(entity, targetLevel, safePos);

            LOGGER.info("Teleported entity {} from {} to {} at {}",
                entity.getName().getString(),
                sourceLevel.dimension().location(),
                targetLevel.dimension().location(),
                safePos);

        } catch (Exception e) {
            LOGGER.error("Portal teleportation failed for entity {} at {}",
                entity.getName().getString(), entity.blockPosition(), e);
        }
    }

    /**
     * Gets the target dimension for teleportation.
     * Overworld ↔ Crystal Dimension
     */
    private ServerLevel getTargetDimension(ServerLevel sourceLevel) {
        // Check if coming from Crystal Dimension
        String sourceDimKey = sourceLevel.dimension().location().toString();

        if (sourceDimKey.contains("crystal_dimension")) {
            // Return to Overworld
            return sourceLevel.getServer().getLevel(Level.OVERWORLD);
        } else {
            // Go to Crystal Dimension
            // TODO: Use proper ResourceKey when dimension is registered
            return sourceLevel.getServer().getLevel(Level.OVERWORLD); // Placeholder
        }
    }

    /**
     * Tracks how long an entity has been inside a portal.
     */
    private static class PortalDwellTracker {
        private int time = 0;

        public void incrementTime() {
            time++;
        }

        public int getTime() {
            return time;
        }

        public void reset() {
            time = 0;
        }
    }

    /**
     * Cleans up tracking data for entities that are no longer in portals.
     * Should be called periodically to prevent memory leaks.
     */
    public static void cleanupInactiveTrackers() {
        // Remove trackers that have been inactive for too long
        ENTITY_PORTAL_TIMES.entrySet().removeIf(entry -> {
            PortalDwellTracker tracker = entry.getValue();
            // Remove if time is 0 (entity left portal and timer was reset)
            return tracker.getTime() == 0;
        });
    }
}
