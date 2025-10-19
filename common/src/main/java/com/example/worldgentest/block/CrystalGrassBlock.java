package com.example.worldgentest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Crystal Grass Block - Surface block for Crystal Dimension biomes
 * Similar to vanilla grass block but with custom texture
 */
public class CrystalGrassBlock extends Block {
    public CrystalGrassBlock() {
        super(BlockBehaviour.Properties.of()
            .strength(0.6F)  // Same as vanilla grass block
            .sound(SoundType.GRASS)
        );
    }
}
