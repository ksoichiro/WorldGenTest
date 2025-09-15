package com.example.worldgentest;

import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.core.Holder;

public class ModBiomes {

    public static Biome createCrystallineCaves() {
        return createCrystallineCaves(null);
    }

    public static Biome createCrystallineCaves(Holder<PlacedFeature> crystalFeature) {
        BiomeGenerationSettings.Builder generationBuilder = new BiomeGenerationSettings.Builder(null, null);

        // プラットフォーム固有のfeatureが提供されている場合は追加
        if (crystalFeature != null) {
            generationBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, crystalFeature);
        }

        return new Biome.BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(0.5f)
            .downfall(0.0f)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .waterColor(0x3f76e4)
                .waterFogColor(0x050533)
                .skyColor(0x77adff)
                .grassColorOverride(0x79c05a)
                .foliageColorOverride(0x59ae30)
                .fogColor(0xc0d8ff)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DRIPSTONE_CAVES))
                .build())
            .mobSpawnSettings(new MobSpawnSettings.Builder()
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 100, 4, 4))
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 95, 4, 4))
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 100, 4, 4))
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 100, 4, 4))
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 100, 4, 4))
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 10, 1, 4))
                .addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.BAT, 10, 8, 8))
                .build())
            .generationSettings(generationBuilder.build())
            .build();
    }
}