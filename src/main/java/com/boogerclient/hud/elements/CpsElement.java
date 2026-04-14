package com.boogerclient.hud.elements;

import com.boogerclient.BoogerClient;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayDeque;
import java.util.Deque;

public class CpsElement extends TextHudElement {

    private final Deque<Long> leftClicks  = new ArrayDeque<>();
    private final Deque<Long> rightClicks = new ArrayDeque<>();

    public CpsElement(float x, float y) {
        super("cps", "CPS", x, y);
    }

    @Override
    public boolean isVisible() { return BoogerClient.config.showCps; }

    public void registerLeftClick()  { leftClicks.addLast(System.currentTimeMillis()); }
    public void registerRightClick() { rightClicks.addLast(System.currentTimeMillis()); }

    @Override
    protected String buildLabel() { return "§7CPS: §f"; }

    @Override
    protected String getValue(MinecraftClient client, float tickDelta) {
        long now = System.currentTimeMillis();
        long cutoff = now - 1000;
        leftClicks.removeIf(t  -> t < cutoff);
        rightClicks.removeIf(t -> t < cutoff);
        int l = leftClicks.size();
        int r = rightClicks.size();
        String lColor = l >= 12 ? "§a" : l >= 8 ? "§e" : "§f";
        return lColor + l + " §7| §f" + r;
    }

    @Override
    protected String getValueText() { return "CPS: 14 | 2"; }
}
