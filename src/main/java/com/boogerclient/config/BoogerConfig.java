package com.boogerclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;

public class BoogerConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
        .getConfigDir().resolve("boogerclient.json");

    // ── HUD settings ─────────────────────────────────────────────────
    public boolean hudEnabled = true;
    public boolean showFps = true;
    public boolean showPing = true;
    public boolean showCps = true;
    public boolean showArmor = true;
    public boolean showPotionStatus = true;
    public boolean showComboCounter = true;
    public boolean showReachDisplay = false;
    public boolean showSpeedometer = true;
    public boolean showCoordinates = true;
    public boolean showDirection = true;
    public boolean showClock = false;

    // HUD element positions (x, y as fraction of screen: 0.0 to 1.0)
    public float fpsPosX = 0.01f, fpsPosY = 0.01f;
    public float pingPosX = 0.01f, pingPosY = 0.04f;
    public float cpsPosX = 0.01f, cpsPosY = 0.07f;
    public float armorPosX = 0.01f, armorPosY = 0.10f;
    public float comboPosX = 0.50f, comboPosY = 0.35f;
    public float speedPosX = 0.01f, speedPosY = 0.13f;
    public float coordsPosX = 0.01f, coordsPosY = 0.16f;

    // HUD scale
    public float hudScale = 1.0f;

    // HUD color (ARGB hex string)
    public String hudColor = "#FFFFFFFF";
    public String hudShadowColor = "#AA000000";
    public boolean hudTextShadow = true;
    public boolean hudBackground = true;
    public String hudBackgroundColor = "#55000000";

    // ── Crosshair settings ────────────────────────────────────────────
    public boolean customCrosshair = true;
    public CrosshairStyle crosshairStyle = CrosshairStyle.CROSS;
    public int crosshairColor = 0xFFFFFFFF;
    public int crosshairSize = 5;
    public int crosshairThickness = 1;
    public int crosshairGap = 2;
    public boolean crosshairDot = false;
    public boolean crosshairOutline = true;
    public boolean hitColorEnabled = true;
    public int hitColor = 0xFFFF4444;

    // ── PvP settings ──────────────────────────────────────────────────
    public boolean autoSprintEnabled = true;
    public boolean blockHitAnimation = true;
    public boolean oldAnimations = false;
    public boolean reachDisplay = false;
    public boolean hitboxes = false;
    public boolean armorStatusBars = true;

    // ── Cosmetics ─────────────────────────────────────────────────────
    public boolean capeEnabled = false;
    public String capeStyle = "NONE";
    public boolean customWingEnabled = false;
    public boolean hatEnabled = false;
    public String hatStyle = "NONE";

    // ── Performance ───────────────────────────────────────────────────
    public boolean reducedParticles = false;
    public boolean smoothFps = true;
    public boolean entityCulling = true;

    public enum CrosshairStyle {
        CROSS, DOT, CIRCLE, SQUARE, ARROW, PLUS
    }

    public static BoogerConfig load() {
        if (CONFIG_PATH.toFile().exists()) {
            try (Reader r = new FileReader(CONFIG_PATH.toFile())) {
                return GSON.fromJson(r, BoogerConfig.class);
            } catch (Exception e) {
                return new BoogerConfig();
            }
        }
        BoogerConfig cfg = new BoogerConfig();
        cfg.save();
        return cfg;
    }

    public void save() {
        try (Writer w = new FileWriter(CONFIG_PATH.toFile())) {
            GSON.toJson(this, w);
        } catch (Exception e) {
            com.boogerclient.BoogerClient.LOGGER.error("Failed to save config", e);
        }
    }
}
