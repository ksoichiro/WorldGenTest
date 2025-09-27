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
import net.minecraft.item.SwordItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.block.Block;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import com.example.worldgentest.ModArmorMaterials;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.registry.entry.RegistryEntry;
import java.util.List;
import java.util.Map;

public class FabricModItems {
    public static final String MOD_ID = "worldgentest";

    public static final Block CRYSTAL_BLOCK = Registry.register(
        Registries.BLOCK,
        Identifier.of(MOD_ID, "crystal_block"),
        new Block(AbstractBlock.Settings.create()
            .strength(3.0F, 6.0F)
            .sounds(BlockSoundGroup.GLASS)
            .luminance(state -> 10))
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

    // クリスタル鉱石（通常・深層岩）
    public static final Block CRYSTAL_ORE = Registry.register(
        Registries.BLOCK,
        Identifier.of(MOD_ID, "crystal_ore"),
        new Block(AbstractBlock.Settings.create()
            .strength(3.0F, 3.0F)
            .sounds(BlockSoundGroup.STONE)
            .requiresTool())
    );

    public static final Item CRYSTAL_ORE_ITEM = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "crystal_ore"),
        new BlockItem(CRYSTAL_ORE, new Item.Settings())
    );

    public static final Block DEEPSLATE_CRYSTAL_ORE = Registry.register(
        Registries.BLOCK,
        Identifier.of(MOD_ID, "deepslate_crystal_ore"),
        new Block(AbstractBlock.Settings.create()
            .strength(4.5F, 3.0F)
            .sounds(BlockSoundGroup.DEEPSLATE)
            .requiresTool())
    );

    public static final Item DEEPSLATE_CRYSTAL_ORE_ITEM = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "deepslate_crystal_ore"),
        new BlockItem(DEEPSLATE_CRYSTAL_ORE, new Item.Settings())
    );

    // 洞窟装飾ブロック
    public static final Block CRYSTAL_STALACTITE = Registry.register(
        Registries.BLOCK,
        Identifier.of(MOD_ID, "crystal_stalactite"),
        new Block(AbstractBlock.Settings.create()
            .strength(0.5F, 1.0F)
            .sounds(BlockSoundGroup.GLASS)
            .luminance(state -> 5)
            .nonOpaque()
            .noCollision())
    );

    public static final Item CRYSTAL_STALACTITE_ITEM = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "crystal_stalactite"),
        new BlockItem(CRYSTAL_STALACTITE, new Item.Settings())
    );

    public static final Block GLOWING_MOSS = Registry.register(
        Registries.BLOCK,
        Identifier.of(MOD_ID, "glowing_moss"),
        new Block(AbstractBlock.Settings.create()
            .strength(0.2F, 0.2F)
            .sounds(BlockSoundGroup.MOSS_BLOCK)
            .luminance(state -> 3)
            .nonOpaque()
            .noCollision())
    );

    public static final Item GLOWING_MOSS_ITEM = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "glowing_moss"),
        new BlockItem(GLOWING_MOSS, new Item.Settings())
    );

    // Crystal Stone Block System - 3 new blocks for crystal stone workflow

    // Crystal Stone - naturally generated, drops cobblestone when mined normally
    public static final Block CRYSTAL_STONE = Registry.register(
        Registries.BLOCK,
        Identifier.of(MOD_ID, "crystal_stone"),
        new Block(AbstractBlock.Settings.create()
            .strength(1.5F, 6.0F)  // Same as vanilla stone
            .sounds(BlockSoundGroup.STONE)
            .requiresTool())  // Requires pickaxe
    );

    public static final Item CRYSTAL_STONE_ITEM = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "crystal_stone"),
        new BlockItem(CRYSTAL_STONE, new Item.Settings())
    );

    // Crystal Cobblestone - result of mining crystal stone, can be smelted back to crystal stone
    public static final Block CRYSTAL_COBBLESTONE = Registry.register(
        Registries.BLOCK,
        Identifier.of(MOD_ID, "crystal_cobblestone"),
        new Block(AbstractBlock.Settings.create()
            .strength(2.0F, 6.0F)  // Same as vanilla cobblestone
            .sounds(BlockSoundGroup.STONE))
    );

    public static final Item CRYSTAL_COBBLESTONE_ITEM = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "crystal_cobblestone"),
        new BlockItem(CRYSTAL_COBBLESTONE, new Item.Settings())
    );

    // Crystal Bricks - decorative block crafted from 4 crystal stones
    public static final Block CRYSTAL_BRICKS = Registry.register(
        Registries.BLOCK,
        Identifier.of(MOD_ID, "crystal_bricks"),
        new Block(AbstractBlock.Settings.create()
            .strength(2.0F, 6.0F)  // Same as vanilla stone bricks
            .sounds(BlockSoundGroup.STONE))
    );

    public static final Item CRYSTAL_BRICKS_ITEM = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "crystal_bricks"),
        new BlockItem(CRYSTAL_BRICKS, new Item.Settings())
    );


    // クリスタルツール材料のToolMaterial実装（ダイヤモンドレベル）
    public static final ToolMaterial CRYSTAL_TOOL_MATERIAL = new ToolMaterial() {
        @Override
        public int getDurability() {
            return 1561; // ダイヤモンドレベル
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return 8.0F; // ダイヤモンドレベル
        }

        @Override
        public float getAttackDamage() {
            return 3.0F; // ダイヤモンドレベルの攻撃ダメージ（SwordItemのコンストラクタで+3される）
        }

        @Override
        public int getEnchantability() {
            return 10; // ダイヤモンドレベル
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(CRYSTAL_SHARD);
        }

        @Override
        public TagKey<Block> getInverseTag() {
            // ダイヤモンドレベルのツールと同等として扱う
            return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
        }
    };

    // クリスタル防具材料（Fabric用）
    public static final RegistryEntry<ArmorMaterial> CRYSTAL_ARMOR_MATERIAL = RegistryEntry.of(new ArmorMaterial(
        Map.of(
            ArmorItem.Type.BOOTS, 4,        // 防御力（ダイヤ+0.5）
            ArmorItem.Type.LEGGINGS, 6,     // 防御力（ダイヤ+0.5）
            ArmorItem.Type.CHESTPLATE, 8,   // 防御力（ダイヤ+0.5）
            ArmorItem.Type.HELMET, 4        // 防御力（ダイヤ+0.5）
        ),
        10, // エンチャント適性
        SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, // 装備音
        () -> Ingredient.ofItems(CRYSTAL_SHARD), // 修理成分
        List.of(
            new ArmorMaterial.Layer(
                Identifier.of(MOD_ID, "crystal")
            )
        ),
        2.0F, // タフネス
        0.0F  // ノックバック耐性
    ));

    // クリスタル防具
    public static final Item CRYSTAL_HELMET = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "crystal_helmet"),
        new ArmorItem(CRYSTAL_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings())
    );

    public static final Item CRYSTAL_CHESTPLATE = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "crystal_chestplate"),
        new ArmorItem(CRYSTAL_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings())
    );

    public static final Item CRYSTAL_LEGGINGS = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "crystal_leggings"),
        new ArmorItem(CRYSTAL_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings())
    );

    public static final Item CRYSTAL_BOOTS = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "crystal_boots"),
        new ArmorItem(CRYSTAL_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings())
    );


    // クリスタルツール（ダイヤモンドレベルの攻撃力を実現）
    public static final Item CRYSTAL_SWORD = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "crystal_sword"),
        new SwordItem(CRYSTAL_TOOL_MATERIAL, new Item.Settings()
            .component(DataComponentTypes.ATTRIBUTE_MODIFIERS,
                AttributeModifiersComponent.builder()
                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(
                            SwordItem.BASE_ATTACK_DAMAGE_MODIFIER_ID,
                            6.5, // クリスタル剣7.5攻撃力（ダイヤ7より0.5高く、ネザライト8未満）
                            EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND)
                    .add(EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(
                            SwordItem.BASE_ATTACK_SPEED_MODIFIER_ID,
                            -2.4, // ダイヤモンド剣と同等（基本4から-2.4で1.6）
                            EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND)
                    .build())
        )
    );

    public static final Item CRYSTAL_PICKAXE = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "crystal_pickaxe"),
        new PickaxeItem(CRYSTAL_TOOL_MATERIAL, new Item.Settings()
            .component(DataComponentTypes.ATTRIBUTE_MODIFIERS,
                AttributeModifiersComponent.builder()
                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(
                            PickaxeItem.BASE_ATTACK_DAMAGE_MODIFIER_ID,
                            4.5, // クリスタルピッケル5.5攻撃力（ダイヤ5より0.5高く、ネザライト6未満）
                            EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND)
                    .add(EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(
                            PickaxeItem.BASE_ATTACK_SPEED_MODIFIER_ID,
                            -2.8, // ダイヤピッケルと同等（基本4から-2.8で1.2）
                            EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND)
                    .build())
        )
    );

    public static final Item CRYSTAL_AXE = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "crystal_axe"),
        new AxeItem(CRYSTAL_TOOL_MATERIAL, new Item.Settings()
            .component(DataComponentTypes.ATTRIBUTE_MODIFIERS,
                AttributeModifiersComponent.builder()
                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(
                            AxeItem.BASE_ATTACK_DAMAGE_MODIFIER_ID,
                            8.5, // クリスタル斧9.5攻撃力（ダイヤ9より0.5高く、ネザライト10未満）
                            EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND)
                    .add(EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(
                            AxeItem.BASE_ATTACK_SPEED_MODIFIER_ID,
                            -3.0, // ダイヤ斧と同等（基本4から-3.0で1.0）
                            EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND)
                    .build())
        )
    );

    public static final Item CRYSTAL_SHOVEL = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "crystal_shovel"),
        new ShovelItem(CRYSTAL_TOOL_MATERIAL, new Item.Settings()
            .component(DataComponentTypes.ATTRIBUTE_MODIFIERS,
                AttributeModifiersComponent.builder()
                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(
                            ShovelItem.BASE_ATTACK_DAMAGE_MODIFIER_ID,
                            5.0, // クリスタルシャベル6.0攻撃力（ダイヤ5.5より0.5高く、ネザライト6.5未満）
                            EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND)
                    .add(EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(
                            ShovelItem.BASE_ATTACK_SPEED_MODIFIER_ID,
                            -3.0, // ダイヤシャベルと同等（基本4から-3.0で1.0）
                            EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND)
                    .build())
        )
    );

    public static final Item CRYSTAL_HOE = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "crystal_hoe"),
        new HoeItem(CRYSTAL_TOOL_MATERIAL, new Item.Settings()
            .component(DataComponentTypes.ATTRIBUTE_MODIFIERS,
                AttributeModifiersComponent.builder()
                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(
                            HoeItem.BASE_ATTACK_DAMAGE_MODIFIER_ID,
                            1.5, // クリスタルクワ2.5攻撃力（ダイヤ2より0.5高く、ネザライト3未満）
                            EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND)
                    .add(EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(
                            HoeItem.BASE_ATTACK_SPEED_MODIFIER_ID,
                            0.0, // ダイヤクワと同等（基本4から変更なしで4.0）
                            EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND)
                    .build())
        )
    );

    public static void register() {
        // 登録は上記で完了
    }
}