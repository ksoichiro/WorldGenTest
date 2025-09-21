package com.example.worldgentest;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public enum ModToolTiers implements Tier {
    CRYSTAL(
        3,      // レベル（鉄=2、ダイヤ=3、ネザライト=4）
        1561,   // 耐久性（ダイヤ=1561、鉄=250）
        8.0F,   // 採掘速度（ダイヤ=8.0F、鉄=6.0F）
        3.0F,   // 攻撃ダメージボーナス（ダイヤ=3.0F、鉄=2.0F）
        10      // エンチャント適性（ダイヤ=10、鉄=14）
    );

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;

    ModToolTiers(int level, int uses, float speed, float damage, int enchantmentValue) {
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentValue;
    }

    @Override
    public int getUses() {
        return this.uses;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.damage;
    }


    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        // クリスタルの欠片で修理可能にする
        // プラットフォーム固有の実装が必要な場合は、各プラットフォームで上書き
        return Ingredient.EMPTY;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        // ダイヤモンドレベルのツールと同等として扱う
        return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
    }
}