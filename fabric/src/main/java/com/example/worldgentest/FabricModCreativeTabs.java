package com.example.worldgentest;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class FabricModCreativeTabs {
    public static final ItemGroup WORLDGEN_TEST_TAB = Registry.register(
        Registries.ITEM_GROUP,
        Identifier.of(FabricModBlocks.MOD_ID, "worldgentest"),
        FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup." + FabricModBlocks.MOD_ID + ".worldgentest"))
            .icon(() -> new ItemStack(FabricModBlocks.CRYSTAL_BLOCK_ITEM))
            .entries((context, entries) -> {
                entries.add(FabricModBlocks.CRYSTAL_BLOCK_ITEM);
                entries.add(FabricModBlocks.CRYSTAL_SHARD);
            })
            .build()
    );

    public static void register() {
        // 登録は上記で完了
    }
}