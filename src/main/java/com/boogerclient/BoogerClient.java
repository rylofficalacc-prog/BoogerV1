package com.boogerclient;

import com.boogerclient.config.BoogerConfig;
import com.boogerclient.hud.HudManager;
import com.boogerclient.pvp.PvpManager;
import com.boogerclient.cosmetics.CosmeticsManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoogerClient implements ClientModInitializer {

    public static final String MOD_ID = "boogerclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static BoogerConfig config;
    public static HudManager hudManager;
    public static PvpManager pvpManager;
    public static CosmeticsManager cosmeticsManager;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Booger Client initializing...");

        config = BoogerConfig.load();
        hudManager = new HudManager();
        pvpManager = new PvpManager();
        cosmeticsManager = new CosmeticsManager();

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            hudManager.render(drawContext, tickCounter.getDynamicDeltaTicks());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            pvpManager.tick(client);
        });

        LOGGER.info("Booger Client initialized.");
    }
}
