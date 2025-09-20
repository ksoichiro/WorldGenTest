package com.example.worldgentest;

import com.example.worldgentest.entity.FabricCrystalGolemEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class FabricModEntities {
    public static final String MOD_ID = "worldgentest";

    // Fabric用のエンティティタイプ
    public static EntityType<FabricCrystalGolemEntity> CRYSTAL_GOLEM;

    public static void register() {
        // エンティティタイプを作成・登録
        CRYSTAL_GOLEM = Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(MOD_ID, "crystal_golem"),
                EntityType.Builder.create(FabricCrystalGolemEntity::new, SpawnGroup.CREATURE)
                        .dimensions(0.9F, 1.3F)
                        .build("crystal_golem")
        );

        // エンティティ属性を登録
        FabricDefaultAttributeRegistry.register(CRYSTAL_GOLEM, FabricCrystalGolemEntity.createGolemAttributes());

        System.out.println("[WorldGenTest] Crystal Golem entity registered successfully!");
    }
}