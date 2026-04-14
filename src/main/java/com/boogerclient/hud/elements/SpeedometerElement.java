package com.boogerclient.hud.elements;

import com.boogerclient.BoogerClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class SpeedometerElement extends TextHudElement {

    public SpeedometerElement(float x, float y) {
        super("speed", "Speed", x, y);
    }

    @Override
    public boolean isVisible() { return BoogerClient.config.showSpeedometer; }

    @Override
    protected String buildLabel() { return "§7Speed: §f"; }

    @Override
    protected String getValue(MinecraftClient client, float tickDelta) {
        if (client.player == null) return "0.00";
        PlayerEntity p = client.player;
        double dx = p.getX() - p.prevX;
        double dz = p.getZ() - p.prevZ;
        double speed = Math.sqrt(dx * dx + dz * dz) * 20; // blocks/sec
        String color = speed > 6 ? "§a" : speed > 4 ? "§e" : "§f";
        return color + String.format("%.2f", speed) + " §7b/s";
    }
}
