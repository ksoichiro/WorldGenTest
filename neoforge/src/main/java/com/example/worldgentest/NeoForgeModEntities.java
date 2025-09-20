package com.example.worldgentest;

import com.example.worldgentest.entity.CrystalGolemEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NeoForgeModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, WorldGenTest.MOD_ID);

    public static final Supplier<EntityType<CrystalGolemEntity>> CRYSTAL_GOLEM = ENTITY_TYPES.register("crystal_golem",
            () -> EntityType.Builder.of(CrystalGolemEntity::new, MobCategory.CREATURE)
                    .sized(0.9f, 1.4f)
                    .build("crystal_golem"));

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }
}