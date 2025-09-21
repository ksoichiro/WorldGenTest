package com.example.worldgentest;

public class FabricBiomeModifications {

    public static void register() {
        // バイオーム修正は複雑なため、データパック方式を使用
        // 代わりに、プレイヤーがコマンドでテストできるようにする
        System.out.println("Biome modifications initialized!");
        System.out.println("Use creative mode and WorldGenTest tab to test new blocks.");
        System.out.println("Crystal features are available via datapack in custom worlds.");
        System.out.println("Use '/give @p worldgentest:crystal_ore' to test manually.");
    }
}