package com.example.worldgentest;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.IEventBus;
import net.minecraft.core.registries.Registries;
import com.example.worldgentest.ModToolTiers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, WorldGenTestNeoForge.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, WorldGenTestNeoForge.MOD_ID);

    public static final Supplier<Block> CRYSTAL_BLOCK = BLOCKS.register("crystal_block", () ->
        new Block(BlockBehaviour.Properties.of()
            .strength(3.0F, 6.0F)
            .sound(SoundType.GLASS)
            .lightLevel(state -> 10))
    );

    public static final Supplier<Item> CRYSTAL_BLOCK_ITEM = ITEMS.register("crystal_block", () ->
        new BlockItem(CRYSTAL_BLOCK.get(), new Item.Properties())
    );

    public static final Supplier<Item> CRYSTAL_SHARD = ITEMS.register("crystal_shard", () ->
        new Item(new Item.Properties())
    );

    // クリスタル鉱石（通常・深層岩）
    public static final Supplier<Block> CRYSTAL_ORE = BLOCKS.register("crystal_ore", () ->
        new Block(BlockBehaviour.Properties.of()
            .strength(3.0F, 3.0F)
            .sound(SoundType.STONE))
    );

    public static final Supplier<Item> CRYSTAL_ORE_ITEM = ITEMS.register("crystal_ore", () ->
        new BlockItem(CRYSTAL_ORE.get(), new Item.Properties())
    );

    public static final Supplier<Block> DEEPSLATE_CRYSTAL_ORE = BLOCKS.register("deepslate_crystal_ore", () ->
        new Block(BlockBehaviour.Properties.of()
            .strength(4.5F, 3.0F)
            .sound(SoundType.DEEPSLATE))
    );

    public static final Supplier<Item> DEEPSLATE_CRYSTAL_ORE_ITEM = ITEMS.register("deepslate_crystal_ore", () ->
        new BlockItem(DEEPSLATE_CRYSTAL_ORE.get(), new Item.Properties())
    );

    // 洞窟装飾ブロック
    public static final Supplier<Block> CRYSTAL_STALACTITE = BLOCKS.register("crystal_stalactite", () ->
        new Block(BlockBehaviour.Properties.of()
            .strength(0.5F, 1.0F)
            .sound(SoundType.GLASS)
            .lightLevel(state -> 5)
            .noOcclusion()
            .noCollission())
    );

    public static final Supplier<Item> CRYSTAL_STALACTITE_ITEM = ITEMS.register("crystal_stalactite", () ->
        new BlockItem(CRYSTAL_STALACTITE.get(), new Item.Properties())
    );

    public static final Supplier<Block> GLOWING_MOSS = BLOCKS.register("glowing_moss", () ->
        new Block(BlockBehaviour.Properties.of()
            .strength(0.2F, 0.2F)
            .sound(SoundType.MOSS)
            .lightLevel(state -> 3)
            .noOcclusion()
            .noCollission())
    );

    public static final Supplier<Item> GLOWING_MOSS_ITEM = ITEMS.register("glowing_moss", () ->
        new BlockItem(GLOWING_MOSS.get(), new Item.Properties())
    );


    // クリスタルツール（NeoForgeのコンストラクタ仕様に合わせる）
    public static final Supplier<Item> CRYSTAL_SWORD = ITEMS.register("crystal_sword", () ->
        new SwordItem(ModToolTiers.CRYSTAL, new Item.Properties()
            .component(DataComponents.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.builder()
                    .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                            SwordItem.BASE_ATTACK_DAMAGE_ID,
                            6.5, // クリスタル剣（7.5攻撃力）ダイヤモンドより高く、ネザライト未満
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                            SwordItem.BASE_ATTACK_SPEED_ID,
                            -2.4, // ダイヤモンド剣と同等（1.6攻撃速度）
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND)
                    .build())
        )
    );

    public static final Supplier<Item> CRYSTAL_PICKAXE = ITEMS.register("crystal_pickaxe", () ->
        new PickaxeItem(ModToolTiers.CRYSTAL, new Item.Properties()
            .component(DataComponents.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.builder()
                    .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                            PickaxeItem.BASE_ATTACK_DAMAGE_ID,
                            4.5, // クリスタルピッケル（5.5攻撃力）ダイヤモンドより高く、ネザライト未満
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                            PickaxeItem.BASE_ATTACK_SPEED_ID,
                            -2.8, // ダイヤピッケルと同等（1.2攻撃速度）
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND)
                    .build())
        )
    );

    public static final Supplier<Item> CRYSTAL_AXE = ITEMS.register("crystal_axe", () ->
        new AxeItem(ModToolTiers.CRYSTAL, new Item.Properties()
            .component(DataComponents.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.builder()
                    .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                            AxeItem.BASE_ATTACK_DAMAGE_ID,
                            8.5, // クリスタル斧（9.5攻撃力）ダイヤモンドより高く、ネザライト未満
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                            AxeItem.BASE_ATTACK_SPEED_ID,
                            -3.0, // ダイヤ斧と同等（1.0攻撃速度）
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND)
                    .build())
        )
    );

    public static final Supplier<Item> CRYSTAL_SHOVEL = ITEMS.register("crystal_shovel", () ->
        new ShovelItem(ModToolTiers.CRYSTAL, new Item.Properties()
            .component(DataComponents.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.builder()
                    .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                            ShovelItem.BASE_ATTACK_DAMAGE_ID,
                            5.0, // クリスタルシャベル（6.0攻撃力）ダイヤモンドより高く、ネザライト未満
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                            ShovelItem.BASE_ATTACK_SPEED_ID,
                            -3.0, // ダイヤシャベルと同等（1.0攻撃速度）
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND)
                    .build())
        )
    );

    public static final Supplier<Item> CRYSTAL_HOE = ITEMS.register("crystal_hoe", () ->
        new HoeItem(ModToolTiers.CRYSTAL, new Item.Properties()
            .component(DataComponents.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.builder()
                    .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                            HoeItem.BASE_ATTACK_DAMAGE_ID,
                            1.5, // クリスタルクワ（2.5攻撃力）ダイヤモンドより高く、ネザライト未満
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                            HoeItem.BASE_ATTACK_SPEED_ID,
                            0.0, // ダイヤクワと同等（4.0攻撃速度）
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND)
                    .build())
        )
    );

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}