package com.example.worldgentest;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import com.example.worldgentest.ModArmorMaterials;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.List;
import java.util.Map;
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
            .lightLevel(state -> 10)
            .requiresCorrectToolForDrops())
    );

    public static final Supplier<Item> CRYSTAL_BLOCK_ITEM = ITEMS.register("crystal_block", () ->
        new BlockItem(CRYSTAL_BLOCK.get(), new Item.Properties())
    );

    public static final Supplier<Item> CRYSTAL_SHARD = ITEMS.register("crystal_shard", () ->
        new Item(new Item.Properties())
    );

    // クリスタル防具材料（NeoForge用）
    public static final Holder<ArmorMaterial> CRYSTAL_ARMOR_MATERIAL = Holder.direct(new ArmorMaterial(
        Map.of(
            ArmorItem.Type.BOOTS, 4,        // 防御力（ダイヤ+0.5）
            ArmorItem.Type.LEGGINGS, 6,     // 防御力（ダイヤ+0.5）
            ArmorItem.Type.CHESTPLATE, 8,   // 防御力（ダイヤ+0.5）
            ArmorItem.Type.HELMET, 4        // 防御力（ダイヤ+0.5）
        ),
        10, // エンチャント適性
SoundEvents.ARMOR_EQUIP_DIAMOND, // 装備音
        () -> Ingredient.of(CRYSTAL_SHARD.get()), // 修理成分
        List.of(
            new ArmorMaterial.Layer(
                ResourceLocation.parse("worldgentest:crystal")
            )
        ),
        2.0F, // タフネス
        0.0F  // ノックバック耐性
    ));

    // クリスタル防具
    public static final Supplier<Item> CRYSTAL_HELMET = ITEMS.register("crystal_helmet", () ->
        new ArmorItem(CRYSTAL_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties())
    );

    public static final Supplier<Item> CRYSTAL_CHESTPLATE = ITEMS.register("crystal_chestplate", () ->
        new ArmorItem(CRYSTAL_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties())
    );

    public static final Supplier<Item> CRYSTAL_LEGGINGS = ITEMS.register("crystal_leggings", () ->
        new ArmorItem(CRYSTAL_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties())
    );

    public static final Supplier<Item> CRYSTAL_BOOTS = ITEMS.register("crystal_boots", () ->
        new ArmorItem(CRYSTAL_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties())
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