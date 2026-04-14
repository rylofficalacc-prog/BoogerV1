package com.boogerclient.hud.elements;

import com.boogerclient.BoogerClient;
import com.boogerclient.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ComboElement extends HudElement {

    private int combo = 0;
    private long lastHitTime = 0;
    private static final long COMBO_TIMEOUT_MS = 3000;

    // Animation
    private float displayScale = 1.0f;
    private float targetScale = 1.0f;

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
        targetScale = 1.3f; // pop animation trigger
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

        // Lerp scale back to 1.0
        displayScale += (targetScale - displayScale) * 0.3f;
        targetScale += (1.0f - targetScale) * 0.2f;

        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        int cx = (int)(x * sw);
        int cy = (int)(y * sh);

        // Scale matrix
        ctx.getMatrices().push();
        ctx.getMatrices().translate(cx, cy, 0);
        ctx.getMatrices().scale(displayScale, displayScale, 1.0f);

        String comboText = "§f✦ " + combo + " HIT COMBO";
        String color = combo >= 20 ? "§c" : combo >= 10 ? "§e" : combo >= 5 ? "§a" : "§f";
        String text = color + combo + " §7Combo";

        int tw = client.textRenderer.getWidth(text);
        ctx.fill(-tw/2 - 4, -2, tw/2 + 4, 12, 0x88000000);
        ctx.drawText(client.textRenderer, text, -tw/2, 0, 0xFFFFFF, true);

        ctx.getMatrices().pop();
    }

    @Override
    public int getWidth(MinecraftClient client) { return 80; }
    @Override
    public int getHeight(MinecraftClient client) { return 12; }

    public int getCombo() { return combo; }
}
