package com.boogerclient.mixin;

import com.boogerclient.BoogerClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    /** Fired when the local player attacks an entity — register combo hit. */
    @Inject(method = "attack", at = @At("HEAD"))
    private void onAttack(net.minecraft.entity.Entity target, CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity)(Object)this;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (!self.getUuid().equals(client.player.getUuid())) return;
        if (!(target instanceof LivingEntity living)) return;
        if (BoogerClient.pvpManager != null) {
            BoogerClient.pvpManager.onAttack(living);
        }
    }

    /** Fired when the player takes damage — reset combo. */
    @Inject(method = "damage", at = @At("HEAD"))
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {
        PlayerEntity self = (PlayerEntity)(Object)this;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (!self.getUuid().equals(client.player.getUuid())) return;
        if (BoogerClient.pvpManager != null) {
            BoogerClient.pvpManager.onPlayerDamaged();
        }
    }
}
