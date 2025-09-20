package com.example.worldgentest;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.block.AbstractBlock;

public class FabricModBlocks {
    public static final String MOD_ID = "worldgentest";

    public static final Block CRYSTAL_BLOCK = Registry.register(
        Registries.BLOCK,
        Identifier.of(MOD_ID, "crystal_block"),
        new Block(AbstractBlock.Settings.create()
            .strength(3.0F, 6.0F)
            .sounds(BlockSoundGroup.GLASS)
            .luminance(state -> 10)
            .requiresTool())
    );

    public static final Item CRYSTAL_BLOCK_ITEM = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "crystal_block"),
        new BlockItem(CRYSTAL_BLOCK, new Item.Settings())
    );

    public static final Item CRYSTAL_SHARD = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "crystal_shard"),
        new Item(new Item.Settings())
    );

    public static void register() {
        // 登録は上記で完了
    }
}