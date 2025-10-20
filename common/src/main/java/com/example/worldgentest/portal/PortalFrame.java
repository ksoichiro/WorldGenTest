package com.example.worldgentest.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

/**
 * Data class representing a portal frame structure.
 *
 * A portal frame is a rectangle of crystal blocks with:
 * - Minimum size: 2x3 (width x height of inner space)
 * - Maximum size: 21x21
 * - Corners are optional (similar to Nether portal)
 */
public class PortalFrame {
    private final BlockPos bottomLeft;
    private final int width;
    private final int height;
    private final Direction.Axis axis;
    private boolean isActive;

    public PortalFrame(BlockPos bottomLeft, int width, int height, Direction.Axis axis) {
        this.bottomLeft = bottomLeft;
        this.width = width;
        this.height = height;
        this.axis = axis;
        this.isActive = false;
    }

    public BlockPos getBottomLeft() {
        return bottomLeft;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Direction.Axis getAxis() {
        return axis;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Validates frame size constraints.
     */
    public boolean isValid() {
        return width >= 2 && width <= 21 && height >= 3 && height <= 21;
    }

    @Override
    public String toString() {
        return "PortalFrame{" +
                "bottomLeft=" + bottomLeft +
                ", width=" + width +
                ", height=" + height +
                ", axis=" + axis +
                ", isActive=" + isActive +
                '}';
    }
}
