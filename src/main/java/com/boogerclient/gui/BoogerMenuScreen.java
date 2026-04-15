package com.boogerclient.gui;

import com.boogerclient.BoogerClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class BoogerMenuScreen extends Screen {

    private static final int PANEL_W = 340;
    private static final int PANEL_H = 260;

    private final float[] dripY    = {0, 0, 0, 0, 0};
    private final float[] dripSpeed = {1.2f, 0.9f, 1.5f, 1.1f, 0.8f};
    private final int[]   dripX    = {12, 55, 98, 141, 184};
    private long openTime;

    private int tab = 0;

    private static final String[] TAB_NAMES = {"HUD", "PvP", "Crosshair", "Cosmetics", "Performance"};
    private static final int[] TAB_COLORS = {0xFF5599FF, 0xFFFF5555, 0xFF55FFAA, 0xFFAA55FF, 0xFFFFAA33};

    public BoogerMenuScreen() {
        super(Text.literal("Booger Client"));
        openTime = System.currentTimeMillis();
    }

    @Override
    protected void init() {
        super.init();
        int px = (width - PANEL_W) / 2;
        int py = (height - PANEL_H) / 2;

        for (int i = 0; i < TAB_NAMES.length; i++) {
            final int tabIdx = i;
            int btnX = px + i * (PANEL_W / TAB_NAMES.length);
            addDrawableChild(ButtonWidget.builder(Text.literal(TAB_NAMES[i]), btn -> {
                tab = tabIdx;
                clearAndInit();
            }).position(btnX, py - 22).size(PANEL_W / TAB_NAMES.length - 2, 20).build());
        }

        renderTabButtons(px, py);

        addDrawableChild(ButtonWidget.builder(Text.literal("✕ Close"), btn -> close())
            .position(px + PANEL_W - 60, py + PANEL_H - 24).size(56, 20).build());
    }

    private void renderTabButtons(int px, int py) {
        int bx = px + 8;
        int by = py + 30;
        int bw = PANEL_W - 16;

        switch (tab) {
            case 0 -> {
                addToggle(bx, by,      bw, "Show FPS",         BoogerClient.config.showFps,
                    v -> { BoogerClient.config.showFps = v; save(); });
                addToggle(bx, by+24,   bw, "Show Ping",        BoogerClient.config.showPing,
                    v -> { BoogerClient.config.showPing = v; save(); });
                addToggle(bx, by+48,   bw, "Show CPS",         BoogerClient.config.showCps,
                    v -> { BoogerClient.config.showCps = v; save(); });
                addToggle(bx, by+72,   bw, "Show Armor",       BoogerClient.config.showArmor,
                    v -> { BoogerClient.config.showArmor = v; save(); });
                addToggle(bx, by+96,   bw, "Show Potions",     BoogerClient.config.showPotionStatus,
                    v -> { BoogerClient.config.showPotionStatus = v; save(); });
                addToggle(bx, by+120,  bw, "Show Combo",       BoogerClient.config.showComboCounter,
                    v -> { BoogerClient.config.showComboCounter = v; save(); });
                addToggle(bx, by+144,  bw, "Show Speedometer", BoogerClient.config.showSpeedometer,
                    v -> { BoogerClient.config.showSpeedometer = v; save(); });
                addToggle(bx, by+168,  bw, "Show Coordinates", BoogerClient.config.showCoordinates,
                    v -> { BoogerClient.config.showCoordinates = v; save(); });
                addDrawableChild(ButtonWidget.builder(Text.literal("⊞ Edit HUD Layout (F6)"), btn -> {
                    close();
                    BoogerClient.hudManager.toggleEditMode();
                }).position(bx, by+192).size(bw, 20).build());
            }
            case 1 -> {
                addToggle(bx, by,     bw, "Auto Sprint",       BoogerClient.config.autoSprintEnabled,
                    v -> { BoogerClient.config.autoSprintEnabled = v; save(); });
                addToggle(bx, by+24,  bw, "Hit Animations",    BoogerClient.config.blockHitAnimation,
                    v -> { BoogerClient.config.blockHitAnimation = v; save(); });
                addToggle(bx, by+48,  bw, "Old Animations",    BoogerClient.config.oldAnimations,
                    v -> { BoogerClient.config.oldAnimations = v; save(); });
                addToggle(bx, by+72,  bw, "Show Reach",        BoogerClient.config.reachDisplay,
                    v -> { BoogerClient.config.reachDisplay = v; save(); });
                addToggle(bx, by+96,  bw, "Show Hitboxes",     BoogerClient.config.hitboxes,
                    v -> { BoogerClient.config.hitboxes = v; save(); });
                addToggle(bx, by+120, bw, "Armor Status Bars", BoogerClient.config.armorStatusBars,
                    v -> { BoogerClient.config.armorStatusBars = v; save(); });
            }
            case 2 -> {
                addToggle(bx, by,    bw, "Custom Crosshair", BoogerClient.config.customCrosshair,
                    v -> { BoogerClient.config.customCrosshair = v; save(); });
                addToggle(bx, by+24, bw, "Center Dot",       BoogerClient.config.crosshairDot,
                    v -> { BoogerClient.config.crosshairDot = v; save(); });
                addToggle(bx, by+48, bw, "Outline",          BoogerClient.config.crosshairOutline,
                    v -> { BoogerClient.config.crosshairOutline = v; save(); });
                addToggle(bx, by+72, bw, "Hit Color Flash",  BoogerClient.config.hitColorEnabled,
                    v -> { BoogerClient.config.hitColorEnabled = v; save(); });
                addDrawableChild(ButtonWidget.builder(
                    Text.literal("Style: " + BoogerClient.config.crosshairStyle.name()),
                    btn -> {
                        var styles = com.boogerclient.config.BoogerConfig.CrosshairStyle.values();
                        int next = (BoogerClient.config.crosshairStyle.ordinal() + 1) % styles.length;
                        BoogerClient.config.crosshairStyle = styles[next];
                        save(); clearAndInit();
                    }).position(bx, by+96).size(bw, 20).build());
                addDrawableChild(ButtonWidget.builder(
                    Text.literal("Size: " + BoogerClient.config.crosshairSize + "  [−]"),
                    btn -> { BoogerClient.config.crosshairSize = Math.max(1, BoogerClient.config.crosshairSize - 1); save(); clearAndInit(); })
                    .position(bx, by+120).size(bw/2 - 2, 20).build());
                addDrawableChild(ButtonWidget.builder(
                    Text.literal("[+]"),
                    btn -> { BoogerClient.config.crosshairSize = Math.min(20, BoogerClient.config.crosshairSize + 1); save(); clearAndInit(); })
                    .position(bx + bw/2 + 2, by+120).size(bw/2 - 2, 20).build());
                addDrawableChild(ButtonWidget.builder(
                    Text.literal("Gap: " + BoogerClient.config.crosshairGap + "  [−]"),
                    btn -> { BoogerClient.config.crosshairGap = Math.max(0, BoogerClient.config.crosshairGap - 1); save(); clearAndInit(); })
                    .position(bx, by+144).size(bw/2 - 2, 20).build());
                addDrawableChild(ButtonWidget.builder(
                    Text.literal("[+]"),
                    btn -> { BoogerClient.config.crosshairGap = Math.min(15, BoogerClient.config.crosshairGap + 1); save(); clearAndInit(); })
                    .position(bx + bw/2 + 2, by+144).size(bw/2 - 2, 20).build());
            }
            case 3 -> {
                addToggle(bx, by, bw, "Enable Cape", BoogerClient.config.capeEnabled,
                    v -> { BoogerClient.config.capeEnabled = v; save(); });
                addDrawableChild(ButtonWidget.builder(
                    Text.literal("Cape: " + BoogerClient.config.capeStyle),
                    btn -> {
                        String[] capes = BoogerClient.cosmeticsManager.getAvailableCapes();
                        int cur = 0;
                        for (int i = 0; i < capes.length; i++) if (capes[i].equals(BoogerClient.config.capeStyle)) cur = i;
                        BoogerClient.config.capeStyle = capes[(cur + 1) % capes.length];
                        save(); clearAndInit();
                    }).position(bx, by+24).size(bw, 20).build());
                addToggle(bx, by+48, bw, "Enable Wings", BoogerClient.config.customWingEnabled,
                    v -> { BoogerClient.config.customWingEnabled = v; save(); });
                addToggle(bx, by+72, bw, "Enable Hat", BoogerClient.config.hatEnabled,
                    v -> { BoogerClient.config.hatEnabled = v; save(); });
                addDrawableChild(ButtonWidget.builder(
                    Text.literal("Hat: " + BoogerClient.config.hatStyle),
                    btn -> {
                        String[] hats = BoogerClient.cosmeticsManager.getAvailableHats();
                        int cur = 0;
                        for (int i = 0; i < hats.length; i++) if (hats[i].equals(BoogerClient.config.hatStyle)) cur = i;
                        BoogerClient.config.hatStyle = hats[(cur + 1) % hats.length];
                        save(); clearAndInit();
                    }).position(bx, by+96).size(bw, 20).build());
            }
            case 4 -> {
                addToggle(bx, by,    bw, "Reduce Particles", BoogerClient.config.reducedParticles,
                    v -> { BoogerClient.config.reducedParticles = v; save(); });
                addToggle(bx, by+24, bw, "Entity Culling",   BoogerClient.config.entityCulling,
                    v -> { BoogerClient.config.entityCulling = v; save(); });
                addToggle(bx, by+48, bw, "Smooth FPS",       BoogerClient.config.smoothFps,
                    v -> { BoogerClient.config.smoothFps = v; save(); });
            }
        }
    }

    private interface BoolConsumer { void accept(boolean value); }

    private void addToggle(int x, int y, int width, String label, boolean current, BoolConsumer onChange) {
        addDrawableChild(ButtonWidget.builder(
            Text.literal((current ? "§a✔ " : "§c✗ ") + "§f" + label),
            btn -> onChange.accept(!current)
        ).position(x, y).size(width, 20).build());
    }

    private void save() {
        BoogerClient.config.save();
        clearAndInit();
    }

    private void drawBorder(DrawContext ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x,         y,         x + w, y + 1,     color);
        ctx.fill(x,         y + h - 1, x + w, y + h,     color);
        ctx.fill(x,         y,         x + 1, y + h,     color);
        ctx.fill(x + w - 1, y,         x + w, y + h,     color);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx, mouseX, mouseY, delta);

        int px = (width - PANEL_W) / 2;
        int py = (height - PANEL_H) / 2;

        renderSplash(ctx, px, py, delta);

        ctx.fill(px, py, px + PANEL_W, py + PANEL_H, 0xEE071407);
        drawBorder(ctx, px, py, PANEL_W, PANEL_H, TAB_COLORS[tab]);

        ctx.fill(px, py, px + PANEL_W, py + 22, 0xFF050D05);
        ctx.drawText(textRenderer, "§2🟢 §aBoogerClient §7v1.0", px + 6, py + 7, 0xFFFFFF, true);

        String tabName = TAB_NAMES[tab];
        int tw = textRenderer.getWidth(tabName);
        ctx.drawText(textRenderer, "§e" + tabName, px + PANEL_W - tw - 8, py + 7, 0xFFFFFF, true);

        super.render(ctx, mouseX, mouseY, delta);
    }

    private void renderSplash(DrawContext ctx, int px, int py, float delta) {
        long elapsed = System.currentTimeMillis() - openTime;

        int bx = px;
        int by = py - 68;
        int bw = PANEL_W;
        int bh = 62;

        ctx.fill(bx, by, bx + bw, by + bh, 0xEE071407);
        drawBorder(ctx, bx, by, bw, bh, 0xFF33AA33);

        for (int i = 0; i < dripY.length; i++) {
            dripY[i] += dripSpeed[i] * delta * 0.6f;
            if (dripY[i] > bh + 12) dripY[i] = 0;
            int dx = bx + dripX[i];
            int dy = by + (int) dripY[i];
            ctx.fill(dx, by, dx + 3, dy, 0xFF22AA22);
            ctx.fill(dx - 2, dy, dx + 5, dy + 5, 0xFF44CC44);
            ctx.fill(dx - 1, dy + 5, dx + 4, dy + 7, 0xFF22AA22);
        }

        String title = "BOOGER CLIENT";
        int titleW = textRenderer.getWidth("§a" + title);
        float pulse = (float)(Math.sin(elapsed / 350.0) * 0.5 + 0.5);

        ctx.drawText(textRenderer, "§2" + title, bx + (bw - titleW)/2 + 2, by + 9 + 1, 0xFF001800, false);
        ctx.drawText(textRenderer, "§2" + title, bx + (bw - titleW)/2 + 1, by + 9 + 1, 0xFF001800, false);
        String titleColored = pulse > 0.5f ? "§a" + title : "§2" + title;
        ctx.drawText(textRenderer, titleColored, bx + (bw - titleW)/2, by + 9, 0xFFFFFFFF, false);

        String[] taglines = {
            "§7The grossest client you'll ever love.",
            "§7100x better than Lunar. 1000x grosser.",
            "§7Snot included. Performance sold separately.",
            "§7Your opponents won't know what hit them.",
            "§7Making PvP slimy since 2024."
        };
        String tagline = taglines[(int)((elapsed / 3500) % taglines.length)];
        int tlW = textRenderer.getWidth(tagline);
        ctx.drawText(textRenderer, tagline, bx + (bw - tlW)/2, by + 23, 0xFFAAAAAA, true);

        String ver = "§2[ §av1.0 STABLE §2]";
        int vw = textRenderer.getWidth(ver);
        ctx.drawText(textRenderer, ver, bx + (bw - vw)/2, by + 36, 0xFFFFFFFF, true);

        drawSnotBlob(ctx, bx + 4,       by + 4);
        drawSnotBlob(ctx, bx + bw - 12, by + 4);
        drawSnotBlob(ctx, bx + 4,       by + bh - 12);
        drawSnotBlob(ctx, bx + bw - 12, by + bh - 12);

        for (int x = bx; x < bx + bw; x += 3) {
            int drip = (int)(Math.sin(x * 0.25 + elapsed * 0.002) * 3 + 4);
            ctx.fill(x, by + bh, x + 2, by + bh + drip, 0xBB22AA22);
        }
    }

    private void drawSnotBlob(DrawContext ctx, int x, int y) {
        ctx.fill(x,     y + 2, x + 6, y + 6, 0xFF33BB33);
        ctx.fill(x + 2, y,     x + 4, y + 8, 0xFF33BB33);
        ctx.fill(x + 1, y + 1, x + 5, y + 7, 0xFF55DD55);
    }

    @Override
    public boolean shouldPause() { return false; }
}
