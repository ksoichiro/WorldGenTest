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
        Identifier.of(FabricModItems.MOD_ID, "worldgentest"),
        FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup." + FabricModItems.MOD_ID + ".worldgentest"))
            .icon(() -> new ItemStack(FabricModItems.CRYSTAL_BLOCK_ITEM))
            .entries((context, entries) -> {
                entries.add(FabricModItems.CRYSTAL_BLOCK_ITEM);
                entries.add(FabricModItems.CRYSTAL_SHARD);
                entries.add(FabricModItems.CRYSTAL_SWORD);
                entries.add(FabricModItems.CRYSTAL_PICKAXE);
                entries.add(FabricModItems.CRYSTAL_AXE);
                entries.add(FabricModItems.CRYSTAL_SHOVEL);
                entries.add(FabricModItems.CRYSTAL_HOE);
            })
            .build()
    );

    public static void register() {
        // 登録は上記で完了
    }
}