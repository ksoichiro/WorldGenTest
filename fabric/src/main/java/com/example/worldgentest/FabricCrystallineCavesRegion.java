package com.example.worldgentest;

import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

/**
 * Fabric用のCrystalline Caves Region (Yarn mapping)
 */
public class FabricCrystallineCavesRegion extends Region {

    private static final RegistryKey<Biome> CRYSTALLINE_CAVES = RegistryKey.of(
        RegistryKeys.BIOME,
        Identifier.of("worldgentest", "crystalline_caves")
    );

    @SuppressWarnings({"unchecked", "rawtypes", "cast"})
    public FabricCrystallineCavesRegion(Identifier name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes", "cast"})
    public void addBiomes(Registry registry, Consumer mapper) {
        // Crystalline Cavesバイオームを地下洞窟として追加
        // TerraBlenderのaddBiomeメソッドを使用
        // MultiNoiseUtil.NoiseHypercube (Yarn) = Climate.ParameterPoint (Mojang)
        this.addBiome(mapper,
            new MultiNoiseUtil.NoiseHypercube(
                MultiNoiseUtil.ParameterRange.of(-1.0f, 1.0f),  // temperature: 全範囲
                MultiNoiseUtil.ParameterRange.of(-1.0f, 1.0f),  // humidity: 全範囲
                MultiNoiseUtil.ParameterRange.of(-1.0f, 1.0f),  // continentalness: 全範囲
                MultiNoiseUtil.ParameterRange.of(-1.0f, 1.0f),  // erosion: 全範囲
                MultiNoiseUtil.ParameterRange.of(0.2f, 1.0f),   // depth: 地下のみ (0.2以上)
                MultiNoiseUtil.ParameterRange.of(-1.0f, 1.0f),  // weirdness: 全範囲
                0L                                               // offset (long型)
            ),
            CRYSTALLINE_CAVES
        );
    }
}
