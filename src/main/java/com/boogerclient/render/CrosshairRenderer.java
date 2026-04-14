package com.boogerclient.render;

import com.boogerclient.BoogerClient;
import com.boogerclient.config.BoogerConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class CrosshairRenderer {

    private int hitFlashTicks = 0;
    private static final int HIT_FLASH_DURATION = 5;

    public void onEntityHit() {
        hitFlashTicks = HIT_FLASH_DURATION;
    }

    public void render(DrawContext ctx, MinecraftClient client) {
        if (!BoogerClient.config.customCrosshair) return;

        BoogerConfig cfg = BoogerClient.config;
        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        int cx = sw / 2;
        int cy = sh / 2;

        int color = (hitFlashTicks > 0 && cfg.hitColorEnabled) ? cfg.hitColor : cfg.crosshairColor;
        if (hitFlashTicks > 0) hitFlashTicks--;

        int size = cfg.crosshairSize;
        int gap  = cfg.crosshairGap;
        int thick = cfg.crosshairThickness;

        switch (cfg.crosshairStyle) {
            case CROSS  -> drawCross(ctx, cx, cy, size, gap, thick, color, cfg.crosshairOutline);
            case DOT    -> drawDot(ctx, cx, cy, thick + 1, color);
            case CIRCLE -> drawCircle(ctx, cx, cy, size, thick, color);
            case SQUARE -> drawSquare(ctx, cx, cy, size, gap, thick, color);
            case ARROW  -> drawArrow(ctx, cx, cy, size, color);
            case PLUS   -> drawPlus(ctx, cx, cy, size, thick, color);
        }

        if (cfg.crosshairDot) {
            drawDot(ctx, cx, cy, 1, color);
        }
    }

    private void drawCross(DrawContext ctx, int cx, int cy, int size, int gap, int thick, int color, boolean outline) {
        // Horizontal arms
        int x1 = cx - size - gap;
        int x2 = cx + size + gap + 1;
        int y1 = cy - thick / 2;
        int y2 = cy + thick / 2 + 1;
        // Vertical arms
        int vx1 = cx - thick / 2;
        int vx2 = cx + thick / 2 + 1;
        int vy1 = cy - size - gap;
        int vy2 = cy + size + gap + 1;

        if (outline) {
            int ol = 0xFF000000;
            ctx.fill(x1 - 1, y1 - 1, x2 + 1, y2 + 1, ol);
            ctx.fill(vx1 - 1, vy1 - 1, vx2 + 1, vy2 + 1, ol);
            // Gap cutout
            ctx.fill(cx - gap, y1 - 1, cx + gap + 1, y2 + 1, ol);
            ctx.fill(vx1 - 1, cy - gap, vx2 + 1, cy + gap + 1, ol);
        }

        ctx.fill(x1, y1, cx - gap, y2, color);
        ctx.fill(cx + gap + 1, y1, x2, y2, color);
        ctx.fill(vx1, vy1, vx2, cy - gap, color);
        ctx.fill(vx1, cy + gap + 1, vx2, vy2, color);
    }

    private void drawDot(DrawContext ctx, int cx, int cy, int r, int color) {
        ctx.fill(cx - r, cy - r, cx + r + 1, cy + r + 1, color);
    }

    private void drawCircle(DrawContext ctx, int cx, int cy, int r, int thick, int color) {
        // Approximate circle with filled ring
        for (int dx = -r - thick; dx <= r + thick; dx++) {
            for (int dy = -r - thick; dy <= r + thick; dy++) {
                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist >= r && dist <= r + thick) {
                    ctx.fill(cx + dx, cy + dy, cx + dx + 1, cy + dy + 1, color);
                }
            }
        }
    }

    private void drawSquare(DrawContext ctx, int cx, int cy, int size, int gap, int thick, int color) {
        int left  = cx - size - gap;
        int right = cx + size + gap + 1;
        int top   = cy - size - gap;
        int bot   = cy + size + gap + 1;
        // Top
        ctx.fill(left, top, right, top + thick, color);
        // Bottom
        ctx.fill(left, bot - thick, right, bot, color);
        // Left
        ctx.fill(left, top, left + thick, bot, color);
        // Right
        ctx.fill(right - thick, top, right, bot, color);
    }

    private void drawArrow(DrawContext ctx, int cx, int cy, int size, int color) {
        for (int i = 0; i < size; i++) {
            ctx.fill(cx - i, cy + i, cx + i + 1, cy + i + 1, color);
        }
        ctx.fill(cx - 1, cy, cx + 2, cy + size, color);
    }

    private void drawPlus(DrawContext ctx, int cx, int cy, int size, int thick, int color) {
        ctx.fill(cx - size, cy - thick, cx + size + 1, cy + thick + 1, color);
        ctx.fill(cx - thick, cy - size, cx + thick + 1, cy + size + 1, color);
    }
}
