package com.boogerclient.hud.elements;

import com.boogerclient.BoogerClient;
import com.boogerclient.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class DirectionElement extends HudElement {

    public DirectionElement(float x, float y) {
        super("direction", "Direction", x, y);
    }

    @Override
    public boolean isVisible() { return BoogerClient.config.showDirection; }

    @Override
    public void render(DrawContext ctx, MinecraftClient client, float tickDelta) {
        if (client.player == null) return;
        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        int cx = (int)(x * sw);
        int cy = (int)(y * sh);

        float yaw = client.player.getYaw() % 360;
        if (yaw < 0) yaw += 360;

        String[] dirs = {"S", "SW", "W", "NW", "N", "NE", "E", "SE"};
        String cardinal = getCardinal(yaw);
        String facing = "§7Facing: §f" + cardinal + " §7(" + (int)yaw + "°)";

        // Compass bar
        int barWidth = 120;
        int barX = cx - barWidth / 2;
        int barY = cy;

        ctx.fill(barX, barY, barX + barWidth, barY + 8, 0x88000000);

        // Draw tick marks
        for (int i = 0; i < 360; i += 45) {
            float relYaw = ((i - yaw + 360) % 360);
            if (relYaw > 180) relYaw -= 360;
            if (Math.abs(relYaw) <= 90) {
                int tickX = barX + barWidth/2 + (int)(relYaw / 90f * (barWidth/2));
                String label = dirs[i / 45];
                boolean isCardinal = i % 90 == 0;
                int tc = isCardinal ? 0xFFFFAA00 : 0xFFAAAAAA;
                ctx.drawText(client.textRenderer, label,
                    tickX - client.textRenderer.getWidth(label)/2, barY, tc, true);
            }
        }

        // Center marker
        ctx.fill(cx - 1, barY - 2, cx + 1, barY + 10, 0xFFFF4444);

        // Text below
        ctx.drawText(client.textRenderer, facing, cx - client.textRenderer.getWidth(facing)/2,
            barY + 11, 0xFFFFFF, BoogerClient.config.hudTextShadow);
    }

    private String getCardinal(float yaw) {
        if (yaw < 22.5 || yaw >= 337.5) return "S";
        if (yaw < 67.5)  return "SW";
        if (yaw < 112.5) return "W";
        if (yaw < 157.5) return "NW";
        if (yaw < 202.5) return "N";
        if (yaw < 247.5) return "NE";
        if (yaw < 292.5) return "E";
        return "SE";
    }

    @Override
    public int getWidth(MinecraftClient client) { return 120; }
    @Override
    public int getHeight(MinecraftClient client) { return 20; }
}
