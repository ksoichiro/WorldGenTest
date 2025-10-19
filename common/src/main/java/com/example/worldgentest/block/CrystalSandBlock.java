package com.example.worldgentest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Crystal Sand Block - Surface block for Crystal Desert biome
 * Similar to vanilla sand block but with custom texture
 * Uses FallingBlock for gravity physics
 */
public class CrystalSandBlock extends FallingBlock {
    public static final MapCodec<CrystalSandBlock> CODEC = simpleCodec(CrystalSandBlock::new);

    public CrystalSandBlock() {
        super(BlockBehaviour.Properties.of()
            .strength(0.5F)  // Same as vanilla sand
            .sound(SoundType.SAND)
        );
    }

    public CrystalSandBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }
}
