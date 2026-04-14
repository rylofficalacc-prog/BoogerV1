package com.boogerclient.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class HudElement {

    protected String id;
    protected String displayName;
    protected float x;
    protected float y;
    protected boolean visible = true;

    protected boolean dragging = false;
    protected float dragOffsetX, dragOffsetY;

    public HudElement(String id, String displayName, float defaultX, float defaultY) {
        this.id = id;
        this.displayName = displayName;
        this.x = defaultX;
        this.y = defaultY;
    }

    public abstract void render(DrawContext ctx, MinecraftClient client, float tickDelta);

    public void renderEditHandle(DrawContext ctx, MinecraftClient client) {
        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        int px = (int)(x * sw);
        int py = (int)(y * sh);
        int w = getWidth(client);
        int h = getHeight(client);

        ctx.fill(px - 2, py - 2, px + w + 2, py + h + 2, 0x88000000);
        int borderColor = dragging ? 0xFFFFAA00 : 0xFFFFFFFF;
        drawBorder(ctx, px - 2, py - 2, w + 4, h + 4, borderColor);
        ctx.drawText(client.textRenderer, displayName, px, py, 0xFFFFFF, false);
    }

    protected void drawBorder(DrawContext ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x,         y,         x + w, y + 1,     color);
        ctx.fill(x,         y + h - 1, x + w, y + h,     color);
        ctx.fill(x,         y,         x + 1, y + h,     color);
        ctx.fill(x + w - 1, y,         x + w, y + h,     color);
    }

    public int getWidth(MinecraftClient client) {
        return client.textRenderer.getWidth(getValueText()) + 4;
    }

    public int getHeight(MinecraftClient client) { return 10; }

    protected String getValueText() { return displayName; }

    public boolean mouseOver(double mx, double my, MinecraftClient client) {
        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        int px = (int)(x * sw);
        int py = (int)(y * sh);
        int w = getWidth(client) + 4;
        int h = getHeight(client) + 4;
        return mx >= px - 2 && mx <= px + w && my >= py - 2 && my <= py + h;
    }

    public void startDrag(double mx, double my, MinecraftClient client) {
        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        dragOffsetX = (float)(mx / sw - x);
        dragOffsetY = (float)(my / sh - y);
        dragging = true;
    }

    public void updateDrag(double mx, double my, MinecraftClient client) {
        if (!dragging) return;
        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        x = (float)(mx / sw - dragOffsetX);
        y = (float)(my / sh - dragOffsetY);
        x = Math.max(0, Math.min(1, x));
        y = Math.max(0, Math.min(1, y));
    }

    public void stopDrag() { dragging = false; }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public float getX() { return x; }
    public float getY() { return y; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean v) { this.visible = v; }
    public boolean isDragging() { return dragging; }
}
