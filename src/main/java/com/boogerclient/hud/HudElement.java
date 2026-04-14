package com.boogerclient.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * Base class for all draggable HUD elements.
 */
public abstract class HudElement {

    protected String id;
    protected String displayName;
    protected float x; // 0.0 – 1.0 fraction of screen width
    protected float y; // 0.0 – 1.0 fraction of screen height
    protected boolean visible = true;

    // Drag state (only used during edit mode)
    protected boolean dragging = false;
    protected float dragOffsetX, dragOffsetY;

    public HudElement(String id, String displayName, float defaultX, float defaultY) {
        this.id = id;
        this.displayName = displayName;
        this.x = defaultX;
        this.y = defaultY;
    }

    /** Render the element in normal gameplay. */
    public abstract void render(DrawContext ctx, MinecraftClient client, float tickDelta);

    /** Render the element's drag handle in edit mode. */
    public void renderEditHandle(DrawContext ctx, MinecraftClient client) {
        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        int px = (int)(x * sw);
        int py = (int)(y * sh);
        int w = getWidth(client);
        int h = getHeight(client);

        // Background box
        ctx.fill(px - 2, py - 2, px + w + 2, py + h + 2, 0x88000000);
        // Border
        ctx.drawBorder(px - 2, py - 2, w + 4, h + 4, dragging ? 0xFFFFAA00 : 0xFFFFFFFF);
        // Label
        ctx.drawText(client.textRenderer, displayName, px, py, 0xFFFFFF, false);
    }

    public int getWidth(MinecraftClient client) {
        return client.textRenderer.getWidth(getValueText()) + 4;
    }

    public int getHeight(MinecraftClient client) {
        return 10;
    }

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

    // Getters / setters
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
