package com.boogerclient.hud.elements;

import com.boogerclient.BoogerClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

public class PingElement extends TextHudElement {

    public PingElement(float x, float y) {
        super("ping", "Ping", x, y);
    }

    @Override
    public boolean isVisible() { return BoogerClient.config.showPing; }

    @Override
    protected String buildLabel() { return "§7Ping: §f"; }

    @Override
    protected String getValue(MinecraftClient client, float tickDelta) {
        if (client.player == null || client.getNetworkHandler() == null) return "§7N/A";
        PlayerListEntry entry = client.getNetworkHandler()
            .getPlayerListEntry(client.player.getUuid());
        if (entry == null) return "§7N/A";
        int ping = entry.getLatency();
        String color = ping < 50 ? "§a" : ping < 120 ? "§e" : "§c";
        return color + ping + "ms";
    }
}
