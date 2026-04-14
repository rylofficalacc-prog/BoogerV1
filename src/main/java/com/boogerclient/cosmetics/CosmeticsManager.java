package com.boogerclient.cosmetics;

import com.boogerclient.BoogerClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages cosmetic rendering: capes, wings, and hats.
 * Capes are rendered client-side only and are visible only to the local player's perspective.
 * Server-side visibility requires a backend (not implemented here).
 */
public class CosmeticsManager {

    public enum CapeStyle {
        NONE, GRADIENT_RED, GRADIENT_BLUE, GRADIENT_GREEN, FLAME, GALAXY, RAINBOW
    }

    public enum HatStyle {
        NONE, CROWN, PARTY, SANTA, WITCH
    }

    // Map of cape style → texture resource path
    private static final Map<CapeStyle, String> CAPE_TEXTURES = new HashMap<>();

    static {
        CAPE_TEXTURES.put(CapeStyle.GRADIENT_RED,   "textures/cosmetics/cape_red.png");
        CAPE_TEXTURES.put(CapeStyle.GRADIENT_BLUE,  "textures/cosmetics/cape_blue.png");
        CAPE_TEXTURES.put(CapeStyle.GRADIENT_GREEN,  "textures/cosmetics/cape_green.png");
        CAPE_TEXTURES.put(CapeStyle.FLAME,          "textures/cosmetics/cape_flame.png");
        CAPE_TEXTURES.put(CapeStyle.GALAXY,         "textures/cosmetics/cape_galaxy.png");
        CAPE_TEXTURES.put(CapeStyle.RAINBOW,        "textures/cosmetics/cape_rainbow.png");
    }

    public CosmeticsManager() {
        BoogerClient.LOGGER.info("CosmeticsManager initialized.");
    }

    /**
     * Called from the LivingEntityRendererMixin to inject cape rendering.
     * Renders only for the local player when their cape setting is enabled.
     */
    public void renderCape(AbstractClientPlayerEntity player,
                           MatrixStack matrices,
                           VertexConsumerProvider vertexConsumers,
                           int light) {
        if (!BoogerClient.config.capeEnabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (!player.getUuid().equals(client.player.getUuid())) return;

        try {
            CapeStyle style = CapeStyle.valueOf(BoogerClient.config.capeStyle);
            if (style == CapeStyle.NONE) return;

            String texturePath = CAPE_TEXTURES.get(style);
            if (texturePath == null) return;

            Identifier capeTexture = Identifier.of(BoogerClient.MOD_ID, texturePath);
            CapeRenderer.render(matrices, vertexConsumers, light, capeTexture, player);

        } catch (IllegalArgumentException e) {
            // Cape style not found in enum — silently ignore
        }
    }

    public String[] getAvailableCapes() {
        return new String[]{"NONE", "GRADIENT_RED", "GRADIENT_BLUE", "GRADIENT_GREEN",
                            "FLAME", "GALAXY", "RAINBOW"};
    }

    public String[] getAvailableHats() {
        return new String[]{"NONE", "CROWN", "PARTY", "SANTA", "WITCH"};
    }
}
