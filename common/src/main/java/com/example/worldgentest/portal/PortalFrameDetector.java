package com.example.worldgentest.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Detects and validates portal frame structures.
 *
 * Detection algorithm:
 * 1. Find bottom-left corner by scanning down and left from clicked position
 * 2. Scan upward and rightward to determine frame dimensions
 * 3. Validate frame edges (all edges must be frame blocks, corners optional)
 * 4. Validate interior space (must be air or portal blocks)
 *
 * Size constraints: 2x3 to 21x21 (inner space)
 */
public class PortalFrameDetector {
    private static final Logger LOGGER = LoggerFactory.getLogger(PortalFrameDetector.class);

    private static final int MIN_WIDTH = 2;
    private static final int MAX_WIDTH = 21;
    private static final int MIN_HEIGHT = 3;
    private static final int MAX_HEIGHT = 21;

    private final Block frameBlock;

    public PortalFrameDetector(Block frameBlock) {
        this.frameBlock = frameBlock;
    }

    /**
     * Attempts to detect a portal frame starting from the given position.
     *
     * @param level The world/level
     * @param startPos The position where the player clicked (frame block)
     * @return PortalFrame if valid frame detected, null otherwise
     */
    public PortalFrame detectFrame(Level level, BlockPos startPos) {
        try {
            // Try both X and Z axes
            PortalFrame frameX = detectFrameOnAxis(level, startPos, Direction.Axis.X);
            if (frameX != null && frameX.isValid()) {
                return frameX;
            }

            PortalFrame frameZ = detectFrameOnAxis(level, startPos, Direction.Axis.Z);
            if (frameZ != null && frameZ.isValid()) {
                return frameZ;
            }

            return null;
        } catch (Exception e) {
            LOGGER.error("Portal frame detection failed at {}", startPos, e);
            return null;
        }
    }

    private PortalFrame detectFrameOnAxis(Level level, BlockPos startPos, Direction.Axis axis) {
        // Step 1: Find bottom-left corner
        BlockPos bottomLeft = findBottomLeftCorner(level, startPos, axis);

        // Step 2: Determine frame dimensions
        int width = scanFrameWidth(level, bottomLeft, axis);
        int height = scanFrameHeight(level, bottomLeft, axis);

        if (width < MIN_WIDTH || width > MAX_WIDTH || height < MIN_HEIGHT || height > MAX_HEIGHT) {
            return null;
        }

        PortalFrame frame = new PortalFrame(bottomLeft, width, height, axis);

        // Step 3: Validate frame edges
        if (!validateFrameEdges(level, frame)) {
            return null;
        }

        // Step 4: Validate interior space
        if (!validateInteriorSpace(level, frame)) {
            return null;
        }

        return frame;
    }

    /**
     * Finds the bottom-left corner of the portal frame by scanning down and left.
     */
    private BlockPos findBottomLeftCorner(Level level, BlockPos startPos, Direction.Axis axis) {
        BlockPos current = startPos;

        // Scan downward
        while (level.getBlockState(current.below()).is(frameBlock)) {
            current = current.below();
        }

        // Scan left (depends on axis)
        Direction leftDir = axis == Direction.Axis.X ? Direction.WEST : Direction.NORTH;
        while (level.getBlockState(current.relative(leftDir)).is(frameBlock)) {
            current = current.relative(leftDir);
        }

        return current;
    }

    /**
     * Scans the width of the frame (number of blocks in horizontal direction).
     */
    private int scanFrameWidth(Level level, BlockPos bottomLeft, Direction.Axis axis) {
        Direction rightDir = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        int width = 0;

        BlockPos current = bottomLeft;
        while (width <= MAX_WIDTH && level.getBlockState(current).is(frameBlock)) {
            width++;
            current = current.relative(rightDir);
        }

        return width - 2; // Subtract 2 for the frame blocks on left and right edges
    }

    /**
     * Scans the height of the frame.
     */
    private int scanFrameHeight(Level level, BlockPos bottomLeft, Direction.Axis axis) {
        int height = 0;

        BlockPos current = bottomLeft;
        while (height <= MAX_HEIGHT && level.getBlockState(current).is(frameBlock)) {
            height++;
            current = current.above();
        }

        return height - 2; // Subtract 2 for the frame blocks on top and bottom edges
    }

    /**
     * Validates that all frame edges are made of frame blocks.
     * Corners are optional (can be any block).
     */
    public boolean validateFrameEdges(Level level, PortalFrame frame) {
        BlockPos bottomLeft = frame.getBottomLeft();
        int width = frame.getWidth();
        int height = frame.getHeight();
        Direction.Axis axis = frame.getAxis();

        Direction rightDir = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;

        // Bottom edge
        for (int i = 0; i < width + 2; i++) {
            BlockPos pos = bottomLeft.relative(rightDir, i);
            if (!level.getBlockState(pos).is(frameBlock)) {
                return false;
            }
        }

        // Top edge
        for (int i = 0; i < width + 2; i++) {
            BlockPos pos = bottomLeft.relative(rightDir, i).above(height + 1);
            if (!level.getBlockState(pos).is(frameBlock)) {
                return false;
            }
        }

        // Left edge (excluding corners already checked)
        for (int i = 1; i < height + 1; i++) {
            BlockPos pos = bottomLeft.above(i);
            if (!level.getBlockState(pos).is(frameBlock)) {
                return false;
            }
        }

        // Right edge (excluding corners already checked)
        for (int i = 1; i < height + 1; i++) {
            BlockPos pos = bottomLeft.relative(rightDir, width + 1).above(i);
            if (!level.getBlockState(pos).is(frameBlock)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Validates that the interior space is empty (air or portal blocks).
     */
    public boolean validateInteriorSpace(Level level, PortalFrame frame) {
        BlockPos bottomLeft = frame.getBottomLeft();
        int width = frame.getWidth();
        int height = frame.getHeight();
        Direction.Axis axis = frame.getAxis();

        Direction rightDir = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;

        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                BlockPos pos = bottomLeft.relative(rightDir, x).above(y);
                BlockState state = level.getBlockState(pos);

                // Must be air or portal block
                if (!state.isAir() && !(state.getBlock() instanceof CrystalPortalBlock)) {
                    return false;
                }
            }
        }

        return true;
    }
}
