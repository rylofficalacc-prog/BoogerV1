package com.boogerclient.hud.elements;

import com.boogerclient.BoogerClient;
import com.boogerclient.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class TextHudElement extends HudElement {

    protected static final int TEXT_COLOR   = 0xFFFFFFFF;
    protected static final int SHADOW_COLOR = 0xFF000000;

    public TextHudElement(String id, String displayName, float defaultX, float defaultY) {
        super(id, displayName, defaultX, defaultY);
    }

    @Override
    public void render(DrawContext ctx, MinecraftClient client, float tickDelta) {
        String text = buildLabel() + getValue(client, tickDelta);
        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        int px = (int)(x * sw);
        int py = (int)(y * sh);

        if (BoogerClient.config.hudBackground) {
            int tw = client.textRenderer.getWidth(text);
            ctx.fill(px - 1, py - 1, px + tw + 1, py + 9, 0x55000000);
        }
        ctx.drawText(client.textRenderer, text, px, py, TEXT_COLOR,
            BoogerClient.config.hudTextShadow);
    }

    protected abstract String buildLabel();
    protected abstract String getValue(MinecraftClient client, float tickDelta);

    @Override
    protected String getValueText() {
        return buildLabel() + "000";
    }
}
