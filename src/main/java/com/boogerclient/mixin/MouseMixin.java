package com.boogerclient.mixin;

import com.boogerclient.BoogerClient;
import net.minecraft.client.Mouse;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if (action != GLFW.GLFW_PRESS) return;
        if (BoogerClient.pvpManager == null) return;

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            BoogerClient.pvpManager.onLeftClick();
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            BoogerClient.pvpManager.onRightClick();
        }
    }
}
