package com.boogerclient.hud.elements;

import com.boogerclient.BoogerClient;
import com.boogerclient.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ComboElement extends HudElement {

    private int combo = 0;
    private long lastHitTime = 0;
    private static final long COMBO_TIMEOUT_MS = 3000;

    public ComboElement(float x, float y) {
        super("combo", "Combo", x, y);
    }

    @Override
    public boolean isVisible() {
        return BoogerClient.config.showComboCounter && combo > 0;
    }

    public void registerHit() {
        long now = System.currentTimeMillis();
        if (now - lastHitTime < COMBO_TIMEOUT_MS) {
            combo++;
        } else {
            combo = 1;
        }
        lastHitTime = now;
    }

    public void resetCombo() { combo = 0; }

    @Override
    public void render(DrawContext ctx, MinecraftClient client, float tickDelta) {
        if (combo <= 0) return;

        long now = System.currentTimeMillis();
        if (now - lastHitTime > COMBO_TIMEOUT_MS) {
            combo = 0;
            return;
        }

        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        int cx = (int)(x * sw);
        int cy = (int)(y * sh);

        String color = combo >= 20 ? "§c" : combo >= 10 ? "§e" : combo >= 5 ? "§a" : "§f";
        String text = color + combo + " §7Combo";

        int tw = client.textRenderer.getWidth(text);
        ctx.fill(cx - tw/2 - 4, cy - 2, cx + tw/2 + 4, cy + 12, 0x88000000);
        ctx.drawText(client.textRenderer, text, cx - tw/2, cy, 0xFFFFFF, true);
    }

    @Override
    public int getWidth(MinecraftClient client) { return 80; }
    @Override
    public int getHeight(MinecraftClient client) { return 12; }

    public int getCombo() { return combo; }
}
