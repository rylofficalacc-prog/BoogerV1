package com.boogerclient.mixin;

import com.boogerclient.BoogerClient;
import com.boogerclient.render.CrosshairRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Unique
    private final CrosshairRenderer apexCrosshairRenderer = new CrosshairRenderer();

    /** Cancel vanilla crosshair rendering and substitute our own. */
    @Inject(
        method = "renderCrosshair",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onRenderCrosshair(DrawContext ctx, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (BoogerClient.config.customCrosshair) {
            apexCrosshairRenderer.render(ctx, net.minecraft.client.MinecraftClient.getInstance());
            ci.cancel();
        }
    }
}
