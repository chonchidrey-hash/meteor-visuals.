package com.example.meteorvisuals;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class VisualsScreen extends Screen {
    private static final int PANEL_WIDTH = 430;
    private static final int PANEL_HEIGHT = 330;
    private int left;
    private int top;

    public VisualsScreen() {
        super(Text.literal("Meteor Visuals"));
    }

    @Override
    protected void init() {
        left = (width - PANEL_WIDTH) / 2;
        top = (height - PANEL_HEIGHT) / 2;
        int x1 = left + 18;
        int x2 = left + 222;
        int w = 190;
        int y = top + 62;
        int h = 20;
        int gap = 25;

        addButton("Hitboxes: " + onOff(VisualsConfig.hitboxesEnabled), x1, y, w, h, () -> VisualsConfig.hitboxesEnabled = !VisualsConfig.hitboxesEnabled);
        addButton("Tracers: " + onOff(VisualsConfig.tracersEnabled), x2, y, w, h, () -> VisualsConfig.tracersEnabled = !VisualsConfig.tracersEnabled);
        y += gap;
        addButton("Players: " + onOff(VisualsConfig.players), x1, y, w, h, () -> VisualsConfig.players = !VisualsConfig.players);
        addButton("Mobs / animals: " + onOff(VisualsConfig.mobs), x2, y, w, h, () -> VisualsConfig.mobs = !VisualsConfig.mobs);
        y += gap;
        addButton("Hitbox color: " + colorName(VisualsConfig.hitboxColor), x1, y, w, h, () -> VisualsConfig.hitboxColor = nextColor(VisualsConfig.hitboxColor));
        addButton("Tracer color: " + colorName(VisualsConfig.tracerColor), x2, y, w, h, () -> VisualsConfig.tracerColor = nextColor(VisualsConfig.tracerColor));
        y += gap;
        addButton("Tracer width: " + String.format("%.1f", VisualsConfig.tracerWidth), x1, y, w, h,
            () -> VisualsConfig.tracerWidth = VisualsConfig.tracerWidth >= 5.5f ? 1.0f : VisualsConfig.tracerWidth + 0.5f);
        addButton("Max distance: " + VisualsConfig.maxDistance, x2, y, w, h,
            () -> VisualsConfig.maxDistance = VisualsConfig.maxDistance >= 128 ? 16 : VisualsConfig.maxDistance + 16);
        y += gap;
        addButton("Reset defaults", x1, y, PANEL_WIDTH - 36, h, this::resetDefaults);
        y += 34;
        addButton("Save settings", left + 18, y, PANEL_WIDTH - 36, 22, VisualsConfig::save);
    }

    private void addButton(String label, int x, int y, int width, int height, Runnable action) {
        addDrawableChild(ButtonWidget.builder(Text.literal(label), button -> {
            action.run();
            VisualsConfig.save();
            clearAndInit();
        }).dimensions(x, y, width, height).build());
    }

    private void resetDefaults() {
        VisualsConfig.hitboxesEnabled = false;
        VisualsConfig.tracersEnabled = false;
        VisualsConfig.players = true;
        VisualsConfig.mobs = false;
        VisualsConfig.hitboxColor = 0xFF35D07F;
        VisualsConfig.tracerColor = 0xFFFFB347;
        VisualsConfig.maxDistance = 64;
        VisualsConfig.tracerWidth = 2.0f;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xEE111318);
        context.fill(left, top, left + PANEL_WIDTH, top + 34, 0xFF20252E);
        context.fill(left, top + 34, left + PANEL_WIDTH, top + 36, 0xFF35D07F);
        context.drawTextWithShadow(textRenderer, "METEOR VISUALS", left + 18, top + 12, 0xFFFFFFFF);
        context.drawTextWithShadow(textRenderer, "VISUAL MODULES", left + 18, top + 45, 0xFF9DA7B5);
        context.drawTextWithShadow(textRenderer, "Right Shift  •  Toggle modules  •  Settings save automatically", left + 18, top + PANEL_HEIGHT - 19, 0xFF8D96A3);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private static String onOff(boolean value) { return value ? "ON" : "OFF"; }

    private static String colorName(int color) {
        return switch (color) {
            case 0xFF35D07F -> "Green";
            case 0xFFFFB347 -> "Orange";
            case 0xFF55AAFF -> "Blue";
            case 0xFFFF5C7A -> "Pink";
            case 0xFFFFFFFF -> "White";
            default -> "Custom";
        };
    }

    private static int nextColor(int color) {
        return switch (color) {
            case 0xFF35D07F -> 0xFFFFB347;
            case 0xFFFFB347 -> 0xFF55AAFF;
            case 0xFF55AAFF -> 0xFFFF5C7A;
            case 0xFFFF5C7A -> 0xFFFFFFFF;
            default -> 0xFF35D07F;
        };
    }
}
