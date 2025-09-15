package com.example.worldgentest;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class FabricCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("checkbiome")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    ServerLevel level = player.serverLevel();
                    BlockPos pos = player.blockPosition();

                    Holder<Biome> biomeHolder = level.getBiome(pos);
                    String biomeName = biomeHolder.unwrapKey()
                        .map(key -> key.location().toString())
                        .orElse("Unknown");

                    player.sendSystemMessage(Component.literal("現在のバイオーム: " + biomeName));

                    // バイオームの詳細情報も表示
                    Biome biome = biomeHolder.value();
                    float temperature = biome.getBaseTemperature();

                    player.sendSystemMessage(Component.literal(
                        String.format("温度: %.2f", temperature)
                    ));

                    // カスタムバイオーム確認
                    if (biomeName.contains("worldgentest") || biomeName.contains("crystalline")) {
                        player.sendSystemMessage(Component.literal("✅ カスタムバイオーム検出！"));
                    } else {
                        player.sendSystemMessage(Component.literal("❌ 通常バイオーム"));
                    }

                    return 1;
                })
            );

            // カスタムバイオーム作成テスト用コマンド
            dispatcher.register(Commands.literal("testbiome")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();

                    try {
                        // カスタムバイオーム作成テスト
                        Biome testBiome = ModBiomes.createCrystallineCaves();

                        player.sendSystemMessage(Component.literal("✅ Crystalline Cavesバイオーム作成成功"));
                        player.sendSystemMessage(Component.literal(
                            String.format("温度: %.2f, 降水: %s",
                                testBiome.getBaseTemperature(),
                                testBiome.hasPrecipitation() ? "あり" : "なし"
                            )
                        ));
                    } catch (Exception e) {
                        player.sendSystemMessage(Component.literal("❌ バイオーム作成エラー: " + e.getMessage()));
                    }

                    return 1;
                })
            );
        });
    }
}