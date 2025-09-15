package com.example.worldgentest;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FabricModBlocks {
    public static final String MOD_ID = "worldgentest";

    public static final Block CRYSTAL_BLOCK = Registry.register(
        BuiltInRegistries.BLOCK,
        ResourceLocation.fromNamespaceAndPath(MOD_ID, "crystal_block"),
        new Block(BlockBehaviour.Properties.of()
            .strength(3.0F, 6.0F)
            .sound(SoundType.GLASS)
            .lightLevel(state -> 10)
            .requiresCorrectToolForDrops())
    );

    public static final Item CRYSTAL_BLOCK_ITEM = Registry.register(
        BuiltInRegistries.ITEM,
        ResourceLocation.fromNamespaceAndPath(MOD_ID, "crystal_block"),
        new BlockItem(CRYSTAL_BLOCK, new Item.Properties())
    );

    public static void register() {
        // 登録は上記で完了
    }
}