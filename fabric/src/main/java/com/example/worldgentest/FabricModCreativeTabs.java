package com.example.worldgentest;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class FabricModCreativeTabs {
    public static final CreativeModeTab WORLDGEN_TEST_TAB = Registry.register(
        BuiltInRegistries.CREATIVE_MODE_TAB,
        ResourceLocation.fromNamespaceAndPath(FabricModBlocks.MOD_ID, "worldgentest"),
        FabricItemGroup.builder()
            .title(Component.translatable("itemGroup." + FabricModBlocks.MOD_ID + ".worldgentest"))
            .icon(() -> new ItemStack(FabricModBlocks.CRYSTAL_BLOCK_ITEM))
            .displayItems((parameters, output) -> {
                output.accept(FabricModBlocks.CRYSTAL_BLOCK_ITEM);
                output.accept(FabricModBlocks.CRYSTAL_SHARD);
            })
            .build()
    );

    public static void register() {
        // 登録は上記で完了
    }
}