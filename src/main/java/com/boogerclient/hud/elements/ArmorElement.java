package com.boogerclient.hud.elements;

import com.boogerclient.BoogerClient;
import com.boogerclient.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class ArmorElement extends HudElement {

    private static final EquipmentSlot[] SLOTS = {
        EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };
    private static final String[] LABELS = {"H", "C", "L", "B"};

    public ArmorElement(float x, float y) {
        super("armor", "Armor", x, y);
    }

    @Override
    public boolean isVisible() { return BoogerClient.config.showArmor; }

    @Override
    public void render(DrawContext ctx, MinecraftClient client, float tickDelta) {
        if (client.player == null) return;
        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        int px = (int)(x * sw);
        int py = (int)(y * sh);

        int row = 0;
        for (int i = 0; i < SLOTS.length; i++) {
            ItemStack stack = client.player.getEquippedStack(SLOTS[i]);
            if (stack.isEmpty()) continue;

            int maxDurability = stack.getMaxDamage();
            if (maxDurability <= 0) continue;
            int durability = maxDurability - stack.getDamage();
            float pct = (float) durability / maxDurability;

            int barWidth = 40;
            int barHeight = 4;
            int barX = px + 14;
            int barY = py + row * 11 + 2;

            // Background
            ctx.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF333333);
            // Fill
            int fillColor = pct > 0.6f ? 0xFF55FF55 : pct > 0.3f ? 0xFFFFFF55 : 0xFFFF5555;
            ctx.fill(barX, barY, barX + (int)(barWidth * pct), barY + barHeight, fillColor);

            // Label
            ctx.drawText(client.textRenderer, "§7" + LABELS[i], px, py + row * 11, 0xFFFFFF, true);
            // Durability text
            String durText = durability + "/" + maxDurability;
            ctx.drawText(client.textRenderer, "§f" + durText, barX + barWidth + 2, py + row * 11, 0xFFFFFF, false);

            row++;
        }
    }

    @Override
    public int getWidth(MinecraftClient client) { return 80; }
    @Override
    public int getHeight(MinecraftClient client) { return 44; }
}
