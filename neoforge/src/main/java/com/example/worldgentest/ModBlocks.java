package com.example.worldgentest;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.IEventBus;
import net.minecraft.core.registries.Registries;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, WorldGenTestNeoForge.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, WorldGenTestNeoForge.MOD_ID);

    public static final Supplier<Block> CRYSTAL_BLOCK = BLOCKS.register("crystal_block", () ->
        new Block(BlockBehaviour.Properties.of()
            .strength(3.0F, 6.0F)
            .sound(SoundType.GLASS)
            .lightLevel(state -> 10)
            .requiresCorrectToolForDrops())
    );

    public static final Supplier<Item> CRYSTAL_BLOCK_ITEM = ITEMS.register("crystal_block", () ->
        new BlockItem(CRYSTAL_BLOCK.get(), new Item.Properties())
    );

    public static final Supplier<Item> CRYSTAL_SHARD = ITEMS.register("crystal_shard", () ->
        new Item(new Item.Properties())
    );

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}