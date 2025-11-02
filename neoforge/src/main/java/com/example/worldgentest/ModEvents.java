package com.example.worldgentest;

import com.example.worldgentest.portal.PortalActivationHandler;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * NeoForge event handlers for mod functionality.
 */
@EventBusSubscriber(modid = WorldGenTestNeoForge.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    private static PortalActivationHandler portalActivationHandler;

    /**
     * Initializes the portal activation handler.
     * Must be called after blocks are registered.
     */
    public static void initPortalHandler() {
        Block frameBlock = ModBlocks.CRYSTAL_BLOCK.get();
        Block portalBlock = ModBlocks.CRYSTAL_PORTAL.get();
        portalActivationHandler = new PortalActivationHandler(frameBlock, portalBlock);
    }

    /**
     * Handles right-click on blocks for portal activation.
     */
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        if (portalActivationHandler == null) {
            return; // Handler not initialized yet
        }

        InteractionResult result = portalActivationHandler.tryActivatePortal(
            event.getLevel(),
            event.getPos(),
            event.getItemStack()
        );

        if (result.consumesAction()) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }
}
