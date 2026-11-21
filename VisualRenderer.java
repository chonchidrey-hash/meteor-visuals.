package com.example.meteorvisuals;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public final class VisualRenderer {
    private VisualRenderer() {}

    public static void render(WorldRenderContext context) {
        if (context.world() == null || context.camera() == null || context.matrixStack() == null || context.consumers() == null) return;
        if (!VisualsConfig.hitboxesEnabled && !VisualsConfig.tracersEnabled) return;

        Vec3d camera = context.camera().getPos();
        MatrixStack matrices = context.matrixStack();
        VertexConsumerProvider consumers = context.consumers();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        double maxDistanceSq = (double) VisualsConfig.maxDistance * VisualsConfig.maxDistance;

        VertexConsumer boxLines = VisualsConfig.hitboxesEnabled ? consumers.getBuffer(RenderLayer.getLines()) : null;
        VertexConsumer tracerLines = VisualsConfig.tracersEnabled
            ? consumers.getBuffer(RenderLayer.getDebugLineStrip(VisualsConfig.tracerWidth)) : null;

        for (Entity entity : context.world().getEntities()) {
            if (!(entity instanceof LivingEntity living) || entity == context.camera().getFocusedEntity()) continue;
            if (!isSelected(living)) continue;
            if (camera.squaredDistanceTo(entity.getPos()) > maxDistanceSq) continue;

            if (VisualsConfig.hitboxesEnabled) {
                Box relativeBox = entity.getBoundingBox().offset(-camera.x, -camera.y, -camera.z);
                float[] c = rgba(VisualsConfig.hitboxColor);
                VertexRendering.drawBox(matrices, boxLines, relativeBox, c[0], c[1], c[2], c[3]);
            }

            if (VisualsConfig.tracersEnabled) {
                Vec3d target = entity.getBoundingBox().getCenter().subtract(camera);
                float[] c = rgba(VisualsConfig.tracerColor);
                tracerLines.vertex(matrix, 0.0f, 0.0f, 0.0f).color(c[0], c[1], c[2], c[3]);
                tracerLines.vertex(matrix, (float) target.x, (float) target.y, (float) target.z)
                    .color(c[0], c[1], c[2], c[3]);
                // Flush each two-vertex strip so separate tracers do not join into one zig-zag.
                if (consumers instanceof VertexConsumerProvider.Immediate immediate) {
                    immediate.draw(RenderLayer.getDebugLineStrip(VisualsConfig.tracerWidth));
                }
                tracerLines = consumers.getBuffer(RenderLayer.getDebugLineStrip(VisualsConfig.tracerWidth));
            }
        }
    }

    private static boolean isSelected(LivingEntity entity) {
        if (entity instanceof PlayerEntity) return VisualsConfig.players;
        return entity instanceof MobEntity && VisualsConfig.mobs;
    }

    private static float[] rgba(int argb) {
        return new float[] {
            ((argb >> 16) & 255) / 255.0f,
            ((argb >> 8) & 255) / 255.0f,
            (argb & 255) / 255.0f,
            ((argb >>> 24) & 255) / 255.0f
        };
    }
}
