package com.example.meteorvisuals;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class VisualsConfig {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("meteor-visuals.properties");

    public static boolean hitboxesEnabled = false;
    public static boolean tracersEnabled = false;
    public static boolean players = true;
    public static boolean mobs = false;
    public static int hitboxColor = 0xFF35D07F;
    public static int tracerColor = 0xFFFFB347;
    public static int maxDistance = 64;
    public static float tracerWidth = 2.0f;

    private VisualsConfig() {}

    public static void load() {
        if (!Files.exists(FILE)) return;
        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(FILE)) {
            p.load(in);
            hitboxesEnabled = Boolean.parseBoolean(p.getProperty("hitboxes.enabled", "false"));
            tracersEnabled = Boolean.parseBoolean(p.getProperty("tracers.enabled", "false"));
            players = Boolean.parseBoolean(p.getProperty("entities.players", "true"));
            mobs = Boolean.parseBoolean(p.getProperty("entities.mobs", "false"));
            hitboxColor = (int) Long.decode(p.getProperty("hitboxes.color", "0xFF35D07F")).longValue();
            tracerColor = (int) Long.decode(p.getProperty("tracers.color", "0xFFFFB347")).longValue();
            maxDistance = clamp(Integer.parseInt(p.getProperty("render.maxDistance", "64")), 8, 128);
            tracerWidth = Math.max(1.0f, Math.min(6.0f, Float.parseFloat(p.getProperty("tracers.width", "2.0"))));
        } catch (IOException | RuntimeException e) {
            MeteorVisualsClient.LOGGER.warn("Could not load Meteor Visuals config", e);
        }
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("hitboxes.enabled", Boolean.toString(hitboxesEnabled));
        p.setProperty("tracers.enabled", Boolean.toString(tracersEnabled));
        p.setProperty("entities.players", Boolean.toString(players));
        p.setProperty("entities.mobs", Boolean.toString(mobs));
        p.setProperty("hitboxes.color", String.format("0x%08X", hitboxColor));
        p.setProperty("tracers.color", String.format("0x%08X", tracerColor));
        p.setProperty("render.maxDistance", Integer.toString(maxDistance));
        p.setProperty("tracers.width", Float.toString(tracerWidth));
        try {
            Files.createDirectories(FILE.getParent());
            try (OutputStream out = Files.newOutputStream(FILE)) {
                p.store(out, "Meteor Visuals configuration");
            }
        } catch (IOException e) {
            MeteorVisualsClient.LOGGER.warn("Could not save Meteor Visuals config", e);
        }
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
