package com.example.meteorvisuals;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.lwjgl.glfw.GLFW;

public final class MeteorVisualsClient implements ClientModInitializer {
    public static final String MOD_ID = "meteor_visuals";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        VisualsConfig.load();
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.meteor_visuals.open_gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.meteor_visuals"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new VisualsScreen());
            }
        });
        WorldRenderEvents.AFTER_ENTITIES.register(VisualRenderer::render);
        LOGGER.info("Meteor Visuals initialized. Press Right Shift to open the GUI.");
    }
}
