package com.example.worldgentest.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles portal activation when a player uses a crystal shard on a portal frame.
 *
 * Activation process:
 * 1. Detect valid portal frame at clicked position
 * 2. Verify player is holding crystal shard
 * 3. Fill portal interior with portal blocks
 * 4. Play activation effects (particles, sound)
 */
public class PortalActivationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PortalActivationHandler.class);

    private final Block frameBlock;
    private final Block portalBlock;
    private final PortalFrameDetector frameDetector;

    public PortalActivationHandler(Block frameBlock, Block portalBlock) {
        this.frameBlock = frameBlock;
        this.portalBlock = portalBlock;
        this.frameDetector = new PortalFrameDetector(frameBlock);
    }

    /**
     * Attempts to activate a portal - Object overload for cross-platform mapping support.
     *
     * @param levelObj The world/level (as Object for mapping compatibility)
     * @param posObj The position (as Object for mapping compatibility)
     * @param heldItemObj The item (as Object for mapping compatibility)
     * @return InteractionResult indicating success or failure
     */
    public InteractionResult tryActivatePortal(Object levelObj, Object posObj, Object heldItemObj) {
        // Cast to Mojang-mapped types (safe at runtime)
        Level level = (Level) levelObj;
        BlockPos pos = (BlockPos) posObj;
        ItemStack heldItem = (ItemStack) heldItemObj;

        return tryActivatePortal(level, pos, heldItem);
    }

    /**
     * Attempts to activate a portal at the given position with the given item.
     *
     * @param level The world/level
     * @param pos The position where the player clicked (frame block)
     * @param heldItem The item the player is holding
     * @return InteractionResult indicating success or failure
     */
    public InteractionResult tryActivatePortal(Level level, BlockPos pos, ItemStack heldItem) {
        // Server-side only
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        try {
            // Verify crystal shard is being used
            if (!isCrystalShard(heldItem)) {
                return InteractionResult.PASS;
            }

            // Verify clicked block is a frame block
            BlockState clickedState = level.getBlockState(pos);
            if (!clickedState.is(frameBlock)) {
                return InteractionResult.PASS;
            }

            // Detect portal frame
            PortalFrame frame = frameDetector.detectFrame(level, pos);
            if (frame == null || !frame.isValid()) {
                return InteractionResult.FAIL;
            }

            // Check if portal is already active
            if (isPortalActive(level, frame)) {
                return InteractionResult.PASS;
            }

            // Activate the portal
            activatePortal(level, frame);

            return InteractionResult.CONSUME;
        } catch (Exception e) {
            LOGGER.error("Portal activation failed at {}", pos, e);
            return InteractionResult.FAIL;
        }
    }

    /**
     * Checks if the given item is a crystal shard.
     */
    private boolean isCrystalShard(ItemStack item) {
        // TODO: Update this when we have a proper registry reference
        // For now, check by item ID string
        String itemId = item.getItem().toString();
        return itemId.contains("crystal_shard");
    }

    /**
     * Checks if a portal is already active (has portal blocks in interior).
     */
    private boolean isPortalActive(Level level, PortalFrame frame) {
        BlockPos bottomLeft = frame.getBottomLeft();
        int width = frame.getWidth();
        int height = frame.getHeight();
        Direction.Axis axis = frame.getAxis();

        Direction rightDir = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;

        // Check if any portal blocks exist in interior
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                BlockPos pos = bottomLeft.relative(rightDir, x).above(y);
                BlockState state = level.getBlockState(pos);

                if (state.getBlock() instanceof CrystalPortalBlock) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Activates the portal by filling it with portal blocks and playing effects.
     */
    private void activatePortal(Level level, PortalFrame frame) {
        BlockPos bottomLeft = frame.getBottomLeft();
        int width = frame.getWidth();
        int height = frame.getHeight();
        Direction.Axis axis = frame.getAxis();

        Direction rightDir = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;

        // Fill interior with portal blocks
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                BlockPos pos = bottomLeft.relative(rightDir, x).above(y);
                BlockState portalState = portalBlock.defaultBlockState()
                    .setValue(BlockStateProperties.HORIZONTAL_AXIS, axis);
                level.setBlock(pos, portalState, 3); // Flag 3: notify neighbors and clients
            }
        }

        // Play activation effects
        playActivationEffects((ServerLevel) level, frame);
    }

    /**
     * Plays purple particles and portal sound on activation.
     */
    private void playActivationEffects(ServerLevel level, PortalFrame frame) {
        BlockPos bottomLeft = frame.getBottomLeft();
        int width = frame.getWidth();
        int height = frame.getHeight();
        Direction.Axis axis = frame.getAxis();

        Direction rightDir = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;

        // Calculate center position for sound
        BlockPos centerPos = bottomLeft.relative(rightDir, width / 2).above(height / 2);

        // Play portal sound
        level.playSound(
            null, // null = play for all players
            centerPos,
            SoundEvents.PORTAL_TRIGGER,
            SoundSource.BLOCKS,
            1.0F, // volume
            1.0F  // pitch
        );

        // Spawn purple particles across the portal surface
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                BlockPos pos = bottomLeft.relative(rightDir, x).above(y);

                // Spawn multiple particles at each position
                for (int i = 0; i < 5; i++) {
                    double particleX = pos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 0.5;
                    double particleY = pos.getY() + 0.5 + (level.random.nextDouble() - 0.5) * 0.5;
                    double particleZ = pos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 0.5;

                    // Use PORTAL particle (purple spiral effect)
                    level.sendParticles(
                        ParticleTypes.PORTAL,
                        particleX, particleY, particleZ,
                        1, // particle count
                        0.0, 0.0, 0.0, // velocity
                        0.5 // speed
                    );
                }
            }
        }
    }
}
