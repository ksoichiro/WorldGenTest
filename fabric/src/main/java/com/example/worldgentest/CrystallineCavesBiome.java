package com.example.worldgentest;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.Climate;

public class CrystallineCavesBiome {

    public static Biome create() {
        // バイオームの特殊エフェクト設定
        BiomeSpecialEffects.Builder effectsBuilder = new BiomeSpecialEffects.Builder()
            .waterColor(0x3F76E4)         // 水の色
            .waterFogColor(0x050533)      // 水中の霧の色
            .skyColor(0x78A7FF)           // 空の色
            .grassColorOverride(0x9FC93C) // 草の色を明るい緑に
            .foliageColorOverride(0x77AB2F) // 葉っぱの色
            .fogColor(0xC0D8FF);          // 霧の色

        // Mob生成設定（洞窟らしくコウモリを追加）
        MobSpawnSettings.Builder mobSpawnBuilder = new MobSpawnSettings.Builder();

        // バイオーム生成設定
        BiomeGenerationSettings.Builder generationBuilder = new BiomeGenerationSettings.Builder();

        // バイオーム作成
        return new Biome.BiomeBuilder()
            .hasPrecipitation(false)  // 降水なし
            .temperature(0.8f)        // 温度
            .downfall(0.6f)          // 湿度
            .specialEffects(effectsBuilder.build())
            .mobSpawnSettings(mobSpawnBuilder.build())
            .generationSettings(generationBuilder.build())
            .temperatureAdjustment(Biome.TemperatureModifier.NONE)
            .build();
    }
}