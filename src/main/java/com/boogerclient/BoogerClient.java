package com.boogerclient;

import com.boogerclient.config.BoogerConfig;
import com.boogerclient.hud.HudManager;
import com.boogerclient.pvp.PvpManager;
import com.boogerclient.cosmetics.CosmeticsManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoogerClient implements ClientModInitializer {

    public static final String MOD_ID = "boogerclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static BoogerConfig config;
    public static HudManager hudManager;
    public static PvpManager pvpManager;
    public static CosmeticsManager cosmeticsManager;

    public static KeyBinding openMenuKey;
    public static KeyBinding toggleHudEditKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Booger Client initializing...");

        config = BoogerConfig.load();
        hudManager = new HudManager();
        pvpManager = new PvpManager();
        cosmeticsManager = new CosmeticsManager();

        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.boogerclient.menu",
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "key.categories.misc"
        ));

        toggleHudEditKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.boogerclient.hud_edit",
            GLFW.GLFW_KEY_F6,
            "key.categories.misc"
        ));

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            hudManager.render(drawContext, tickCounter.getDynamicDeltaTicks());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            pvpManager.tick(client);
            if (openMenuKey.wasPressed()) {
                client.setScreen(new com.boogerclient.gui.BoogerMenuScreen());
            }
            if (toggleHudEditKey.wasPressed()) {
                hudManager.toggleEditMode();
            }
        });

        LOGGER.info("Booger Client initialized.");
    }
}
