package com.example.worldgentest;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WorldGenTestNeoForge.MOD_ID);

    public static final Supplier<CreativeModeTab> WORLDGEN_TEST_TAB = CREATIVE_MODE_TABS.register("worldgentest",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + WorldGenTestNeoForge.MOD_ID + ".worldgentest"))
            .icon(() -> new ItemStack(ModBlocks.CRYSTAL_BLOCK.get()))
            .displayItems((parameters, output) -> {
                output.accept(ModBlocks.CRYSTAL_BLOCK_ITEM.get());
            })
            .build()
    );

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}