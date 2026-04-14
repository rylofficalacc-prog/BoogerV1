package com.boogerclient.hud.elements;

import com.boogerclient.BoogerClient;
import com.boogerclient.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

import java.util.Collection;

public class PotionStatusElement extends HudElement {

    public PotionStatusElement(float x, float y) {
        super("potions", "Potions", x, y);
    }

    @Override
    public boolean isVisible() { return BoogerClient.config.showPotionStatus; }

    @Override
    public void render(DrawContext ctx, MinecraftClient client, float tickDelta) {
        if (client.player == null) return;
        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        int px = (int)(x * sw);
        int py = (int)(y * sh);

        Collection<StatusEffectInstance> effects = client.player.getStatusEffects();
        int row = 0;
        for (StatusEffectInstance effect : effects) {
            RegistryEntry<StatusEffect> type = effect.getEffectType();
            int duration = effect.getDuration();
            int amplifier = effect.getAmplifier();

            String name = type.value().getName().getString();
            if (amplifier > 0) name += " " + toRoman(amplifier + 1);

            String timeStr = formatDuration(duration);
            boolean isNegative = !type.value().isBeneficial();
            String color = isNegative ? "§c" : duration < 200 ? "§e" : "§a";

            String line = color + name + " §7" + timeStr;
            if (BoogerClient.config.hudBackground) {
                int tw = client.textRenderer.getWidth(line);
                ctx.fill(px - 1, py + row * 10 - 1, px + tw + 1, py + row * 10 + 9, 0x55000000);
            }
            ctx.drawText(client.textRenderer, line, px, py + row * 10, 0xFFFFFF,
                BoogerClient.config.hudTextShadow);
            row++;
        }
    }

    private String formatDuration(int ticks) {
        int totalSec = ticks / 20;
        int min = totalSec / 60;
        int sec = totalSec % 60;
        return String.format("%d:%02d", min, sec);
    }

    private String toRoman(int n) {
        return switch (n) {
            case 2 -> "II"; case 3 -> "III"; case 4 -> "IV"; case 5 -> "V";
            default -> String.valueOf(n);
        };
    }

    @Override
    public int getWidth(MinecraftClient client) { return 100; }
    @Override
    public int getHeight(MinecraftClient client) { return 80; }
}
