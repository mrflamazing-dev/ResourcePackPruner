package com.mrflamazing.rpr;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

@Mod(value = ResourcePackPruner.MODID, dist = Dist.CLIENT)
// UPDATED: Removed "bus = EventBusSubscriber.Bus.MOD" as it is deprecated.
// NeoForge now automatically detects that FMLClientSetupEvent belongs to the Mod Bus.
@EventBusSubscriber(modid = ResourcePackPruner.MODID, value = Dist.CLIENT)
public class ResourcePackPrunerClient {
    private static final Logger LOGGER = LogUtils.getLogger();

    public ResourcePackPrunerClient(ModContainer container) {
        // Register the config screen factory
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("Client Setup Complete.");

        // Safe check for user instance
        if (Minecraft.getInstance().getUser() != null) {
            LOGGER.info("Active User: {}", Minecraft.getInstance().getUser().getName());
        }
    }
}