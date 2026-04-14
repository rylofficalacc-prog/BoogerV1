package com.boogerclient.pvp;

import com.boogerclient.BoogerClient;
import com.boogerclient.hud.elements.ComboElement;
import com.boogerclient.hud.elements.CpsElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;

public class PvpManager {

    private long lastAttackTime = 0;
    private static final long COMBO_WINDOW_MS = 3500;

    public void tick(MinecraftClient client) {
        if (client.player == null) return;

        // Auto-sprint: keep sprinting when moving forward
        if (BoogerClient.config.autoSprintEnabled) {
            if (client.player.forwardSpeed > 0
                && !client.player.isSprinting()
                && !client.player.isBlocking()
                && client.player.getHungerManager().getFoodLevel() > 6) {
                client.player.setSprinting(true);
            }
        }
    }

    /** Called when the player successfully attacks an entity */
    public void onAttack(LivingEntity target) {
        lastAttackTime = System.currentTimeMillis();
        // Register combo hit
        getComboElement().registerHit();
    }

    /** Called when the player takes damage (combo reset) */
    public void onPlayerDamaged() {
        getComboElement().resetCombo();
    }

    /** Called on left mouse click */
    public void onLeftClick() {
        getCpsElement().registerLeftClick();
    }

    /** Called on right mouse click */
    public void onRightClick() {
        getCpsElement().registerRightClick();
    }

    private ComboElement getComboElement() {
        return (ComboElement) BoogerClient.hudManager.getElements().stream()
            .filter(e -> e.getId().equals("combo"))
            .findFirst().orElse(null);
    }

    private CpsElement getCpsElement() {
        return (CpsElement) BoogerClient.hudManager.getElements().stream()
            .filter(e -> e.getId().equals("cps"))
            .findFirst().orElse(null);
    }
}
