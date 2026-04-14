package com.boogerclient.hud.elements;

import com.boogerclient.BoogerClient;
import net.minecraft.client.MinecraftClient;

public class FpsElement extends TextHudElement {

    public FpsElement(float x, float y) {
        super("fps", "FPS", x, y);
    }

    @Override
    public boolean isVisible() { return BoogerClient.config.showFps; }

    @Override
    protected String buildLabel() { return "§7FPS: §f"; }

    @Override
    protected String getValue(MinecraftClient client, float tickDelta) {
        int fps = MinecraftClient.getInstance().getCurrentFps();
        String color = fps >= 120 ? "§a" : fps >= 60 ? "§e" : "§c";
        return color + fps;
    }
}
