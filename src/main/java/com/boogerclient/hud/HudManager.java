package com.boogerclient.hud;

import com.boogerclient.BoogerClient;
import com.boogerclient.config.BoogerConfig;
import com.boogerclient.hud.elements.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class HudManager {

    private final List<HudElement> elements = new ArrayList<>();
    private boolean editMode = false;
    private HudElement draggingElement = null;

    public HudManager() {
        BoogerConfig cfg = BoogerClient.config;
        registerElement(new FpsElement(cfg.fpsPosX, cfg.fpsPosY));
        registerElement(new PingElement(cfg.pingPosX, cfg.pingPosY));
        registerElement(new CpsElement(cfg.cpsPosX, cfg.cpsPosY));
        registerElement(new ArmorElement(cfg.armorPosX, cfg.armorPosY));
        registerElement(new ComboElement(cfg.comboPosX, cfg.comboPosY));
        registerElement(new SpeedometerElement(cfg.speedPosX, cfg.speedPosY));
        registerElement(new CoordinatesElement(cfg.coordsPosX, cfg.coordsPosY));
        registerElement(new PotionStatusElement(0.87f, 0.30f));
        registerElement(new DirectionElement(0.50f, 0.06f));
    }

    private void registerElement(HudElement el) {
        elements.add(el);
    }

    public void render(DrawContext ctx, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        if (editMode) {
            // Dim overlay
            ctx.fill(0, 0, client.getWindow().getScaledWidth(),
                client.getWindow().getScaledHeight(), 0x55000000);
            // Edit mode title
            String hint = "§eHUD Edit Mode §7- Drag elements | §aF6§7 to save & exit";
            int tw = client.textRenderer.getWidth(hint);
            ctx.drawText(client.textRenderer, hint,
                (client.getWindow().getScaledWidth() - tw) / 2, 4, 0xFFFFFF, true);

            for (HudElement el : elements) {
                el.renderEditHandle(ctx, client);
            }
        } else {
            if (!BoogerClient.config.hudEnabled) return;
            for (HudElement el : elements) {
                if (el.isVisible()) {
                    el.render(ctx, client, tickDelta);
                }
            }
        }
    }

    public void toggleEditMode() {
        editMode = !editMode;
        if (!editMode) {
            savePositions();
        }
    }

    public boolean isEditMode() { return editMode; }

    public void onMouseDown(double mx, double my) {
        if (!editMode) return;
        MinecraftClient client = MinecraftClient.getInstance();
        for (int i = elements.size() - 1; i >= 0; i--) {
            HudElement el = elements.get(i);
            if (el.mouseOver(mx, my, client)) {
                draggingElement = el;
                el.startDrag(mx, my, client);
                return;
            }
        }
    }

    public void onMouseMove(double mx, double my) {
        if (draggingElement == null) return;
        MinecraftClient client = MinecraftClient.getInstance();
        draggingElement.updateDrag(mx, my, client);
    }

    public void onMouseUp() {
        if (draggingElement != null) {
            draggingElement.stopDrag();
            draggingElement = null;
        }
    }

    private void savePositions() {
        BoogerConfig cfg = BoogerClient.config;
        for (HudElement el : elements) {
            switch (el.getId()) {
                case "fps"   -> { cfg.fpsPosX = el.getX(); cfg.fpsPosY = el.getY(); }
                case "ping"  -> { cfg.pingPosX = el.getX(); cfg.pingPosY = el.getY(); }
                case "cps"   -> { cfg.cpsPosX = el.getX(); cfg.cpsPosY = el.getY(); }
                case "armor" -> { cfg.armorPosX = el.getX(); cfg.armorPosY = el.getY(); }
                case "combo" -> { cfg.comboPosX = el.getX(); cfg.comboPosY = el.getY(); }
                case "speed" -> { cfg.speedPosX = el.getX(); cfg.speedPosY = el.getY(); }
                case "coords"-> { cfg.coordsPosX = el.getX(); cfg.coordsPosY = el.getY(); }
            }
        }
        cfg.save();
    }

    public List<HudElement> getElements() { return elements; }
}
