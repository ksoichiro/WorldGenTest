package com.example.worldgentest;

import com.example.worldgentest.entity.ModEntities;

public class WorldGenTest {
    public static final String MOD_ID = "worldgentest";

    public static void init() {
        ModEntities.register();
    }
}