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
                // ブロック類
                output.accept(ModBlocks.CRYSTAL_BLOCK_ITEM.get());
                output.accept(ModBlocks.CRYSTAL_STONE_ITEM.get());
                output.accept(ModBlocks.CRYSTAL_COBBLESTONE_ITEM.get());
                output.accept(ModBlocks.CRYSTAL_BRICKS_ITEM.get());
                output.accept(ModBlocks.CRYSTAL_ORE_ITEM.get());
                output.accept(ModBlocks.DEEPSLATE_CRYSTAL_ORE_ITEM.get());
                output.accept(ModBlocks.CRYSTAL_STALACTITE_ITEM.get());
                output.accept(ModBlocks.GLOWING_MOSS_ITEM.get());

                // アイテム類
                output.accept(ModBlocks.CRYSTAL_SHARD.get());

                // ツール類
                output.accept(ModBlocks.CRYSTAL_SWORD.get());
                output.accept(ModBlocks.CRYSTAL_PICKAXE.get());
                output.accept(ModBlocks.CRYSTAL_AXE.get());
                output.accept(ModBlocks.CRYSTAL_SHOVEL.get());
                output.accept(ModBlocks.CRYSTAL_HOE.get());

                // 防具類
                output.accept(ModBlocks.CRYSTAL_HELMET.get());
                output.accept(ModBlocks.CRYSTAL_CHESTPLATE.get());
                output.accept(ModBlocks.CRYSTAL_LEGGINGS.get());
                output.accept(ModBlocks.CRYSTAL_BOOTS.get());
            })
            .build()
    );

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}