package com.boogerclient.hud.elements;

import com.boogerclient.BoogerClient;
import com.boogerclient.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;

public class CoordinatesElement extends HudElement {

    public CoordinatesElement(float x, float y) {
        super("coords", "Coordinates", x, y);
    }

    @Override
    public boolean isVisible() { return BoogerClient.config.showCoordinates; }

    @Override
    public void render(DrawContext ctx, MinecraftClient client, float tickDelta) {
        if (client.player == null) return;
        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        int px = (int)(x * sw);
        int py = (int)(y * sh);

        BlockPos pos = client.player.getBlockPos();
        String xStr = "§7X: §f" + pos.getX();
        String yStr = "§7Y: §f" + pos.getY();
        String zStr = "§7Z: §f" + pos.getZ();

        int bgW = 60;
        if (BoogerClient.config.hudBackground) {
            ctx.fill(px - 1, py - 1, px + bgW + 1, py + 25, 0x55000000);
        }
        ctx.drawText(client.textRenderer, xStr, px, py,      0xFFFFFF, BoogerClient.config.hudTextShadow);
        ctx.drawText(client.textRenderer, yStr, px, py + 9,  0xFFFFFF, BoogerClient.config.hudTextShadow);
        ctx.drawText(client.textRenderer, zStr, px, py + 18, 0xFFFFFF, BoogerClient.config.hudTextShadow);
    }

    @Override
    public int getWidth(MinecraftClient client) { return 70; }
    @Override
    public int getHeight(MinecraftClient client) { return 27; }
}
