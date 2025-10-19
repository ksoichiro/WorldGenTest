package com.example.worldgentest.block;

import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Crystal Log Block - Tree trunk for Crystal Forest biome
 * Similar to vanilla log blocks but with custom texture
 * Uses RotatedPillarBlock for directional placement
 */
public class CrystalLogBlock extends RotatedPillarBlock {
    public CrystalLogBlock() {
        super(BlockBehaviour.Properties.of()
            .strength(2.0F)  // Same as vanilla logs
            .sound(SoundType.WOOD)
        );
    }
}
