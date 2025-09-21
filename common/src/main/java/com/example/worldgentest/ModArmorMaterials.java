package com.example.worldgentest;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModArmorMaterials {

    // 耐久値のマップ（ダイヤモンドレベル）
    private static final Map<ArmorItem.Type, Integer> DURABILITY_MAP = Map.of(
        ArmorItem.Type.BOOTS, 14 * 37,      // 518
        ArmorItem.Type.LEGGINGS, 17 * 37,   // 629
        ArmorItem.Type.CHESTPLATE, 17 * 37, // 629
        ArmorItem.Type.HELMET, 12 * 37      // 444
    );

    // 防御力のマップ（ダイヤモンド+0.5レベル）
    private static final Map<ArmorItem.Type, Integer> DEFENSE_MAP = Map.of(
        ArmorItem.Type.BOOTS, 4,        // ダイヤ3 + 0.5 → 4（四捨五入）
        ArmorItem.Type.LEGGINGS, 6,     // ダイヤ6 + 0.5 → 6（四捨五入）
        ArmorItem.Type.CHESTPLATE, 8,   // ダイヤ8 + 0.5 → 8（四捨五入）
        ArmorItem.Type.HELMET, 4        // ダイヤ3 + 0.5 → 4（四捨五入）
    );

    public static final ArmorMaterial CRYSTAL = new ArmorMaterial(
        DEFENSE_MAP, // 防御力マップ（正しい値）
        10, // エンチャント適性
SoundEvents.ARMOR_EQUIP_DIAMOND, // 装備音
        () -> Ingredient.EMPTY, // 修理成分（プラットフォーム固有で上書き）
        List.of(
            new ArmorMaterial.Layer(
                ResourceLocation.parse("worldgentest:crystal")
            )
        ),
        2.0F, // タフネス
        0.0F  // ノックバック耐性
    );

    // 防御力を取得するヘルパーメソッド
    public static int getDefenseForType(ArmorItem.Type type) {
        return DEFENSE_MAP.get(type);
    }

    // 耐久値を取得するヘルパーメソッド
    public static int getDurabilityForType(ArmorItem.Type type) {
        return DURABILITY_MAP.get(type);
    }
}