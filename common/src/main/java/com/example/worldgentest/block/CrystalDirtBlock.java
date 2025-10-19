package com.example.worldgentest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Crystal Dirt Block - Underground block for Crystal Dimension biomes
 * Similar to vanilla dirt block but with custom texture
 */
public class CrystalDirtBlock extends Block {
    public CrystalDirtBlock() {
        super(BlockBehaviour.Properties.of()
            .strength(0.5F)  // Same as vanilla dirt
            .sound(SoundType.GRAVEL)
        );
    }
}
