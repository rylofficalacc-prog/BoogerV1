package com.boogerclient.cosmetics;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;

public class CapeRenderer {

    /**
     * Renders a cape for the given player using the provided texture.
     * The cape is rendered as a quad behind the player, swinging based on movement.
     */
    public static void render(MatrixStack matrices,
                               VertexConsumerProvider vertexConsumers,
                               int light,
                               Identifier texture,
                               AbstractClientPlayerEntity player) {

        matrices.push();

        // Position behind the player's back
        matrices.translate(0.0, 0.0, 0.125);

        // Cape swing based on player velocity
        double dx = player.getX() - player.prevX;
        double dz = player.getZ() - player.prevZ;
        float swingAngle = (float) Math.sqrt(dx * dx + dz * dz) * 10.0f;
        swingAngle = MathHelper.clamp(swingAngle, 0.0f, 20.0f);

        // Cape flap based on y movement
        float flapAngle = (float)(player.getY() - player.prevY) * -5.0f;

        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X
            .rotationDegrees(6.0f + swingAngle * 0.5f + flapAngle));
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Z
            .rotationDegrees(swingAngle * 0.1f));

        // Offset downward from shoulder point
        matrices.translate(0.0, -0.0625, 0.0);

        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(texture));
        Matrix4f posMatrix = matrices.peek().getPositionMatrix();

        // Cape quad: 0.625 x 1.0 blocks
        float w = 0.5f;
        float h = 1.0f;

        addVertex(consumer, posMatrix, -w,  0,  0, 0.0f, 0.0f, light);
        addVertex(consumer, posMatrix,  w,  0,  0, 1.0f, 0.0f, light);
        addVertex(consumer, posMatrix,  w, -h,  0, 1.0f, 1.0f, light);
        addVertex(consumer, posMatrix, -w, -h,  0, 0.0f, 1.0f, light);

        matrices.pop();
    }

    private static void addVertex(VertexConsumer consumer, Matrix4f pos,
                                   float x, float y, float z, float u, float v, int light) {
        consumer.vertex(pos, x, y, z)
            .color(255, 255, 255, 255)
            .texture(u, v)
            .overlay(OverlayTexture.DEFAULT_UV)
            .light(light)
            .normal(0.0f, 0.0f, 1.0f);
    }
}
