package com.example.worldgentest;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

/**
 * TerraBlenderを使用してCrystalline Cavesバイオームをオーバーワールドに追加するRegionクラス
 */
public class CrystallineCavesRegion extends Region {

    public static final ResourceKey<Biome> CRYSTALLINE_CAVES = ResourceKey.create(
        Registries.BIOME,
        ResourceLocation.parse("worldgentest:crystalline_caves")
    );

    public CrystallineCavesRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // Crystalline Cavesバイオームを地下洞窟として追加
        // パラメータ: temperature, humidity, continentalness, erosion, depth, weirdness, offset
        this.addBiome(mapper,
            Climate.parameters(
                Climate.Parameter.span(-1.0f, 1.0f),  // temperature: 全範囲
                Climate.Parameter.span(-1.0f, 1.0f),  // humidity: 全範囲
                Climate.Parameter.span(-1.0f, 1.0f),  // continentalness: 全範囲
                Climate.Parameter.span(-1.0f, 1.0f),  // erosion: 全範囲
                Climate.Parameter.span(0.2f, 1.0f),   // depth: 地下のみ (0.2以上)
                Climate.Parameter.span(-1.0f, 1.0f),  // weirdness: 全範囲
                0.0f                                   // offset
            ),
            CRYSTALLINE_CAVES
        );
    }
}
