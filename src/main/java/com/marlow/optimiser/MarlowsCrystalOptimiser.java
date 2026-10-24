package com.marlow.optimiser;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Client-only and cosmetic. Changes how the money number on the sidebar scoreboard
 * is DRAWN on your own screen. Nothing is sent to the server.
 */
public class MarlowsCrystalOptimiser implements ClientModInitializer {

    /** True only while the sidebar scoreboard is being rendered. */
    public static volatile boolean inSidebar = false;

    public static volatile boolean enabled = true;
    public static volatile String fakeAmount = "5B";

    private static KeyBinding openMenuKey;

    private static final Path CONFIG =
            FabricLoader.getInstance().getConfigDir().resolve("marlows_crystal_optimiser.properties");

    @Override
    public void onInitializeClient() {
        load();

        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.marlows_crystal_optimiser.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "key.categories.marlows_crystal_optimiser"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new OptimiserScreen());
                }
            }
        });
    }

    private static void load() {
        try {
            if (Files.exists(CONFIG)) {
                Properties p = new Properties();
                try (InputStream in = Files.newInputStream(CONFIG)) {
                    p.load(in);
                }
                enabled = Boolean.parseBoolean(p.getProperty("enabled", "true"));
                fakeAmount = p.getProperty("amount", fakeAmount);
            } else {
                save();
            }
        } catch (IOException ignored) {
        }
    }

    public static void save() {
        try {
            Properties p = new Properties();
            p.setProperty("enabled", Boolean.toString(enabled));
            p.setProperty("amount", fakeAmount);
            try (OutputStream out = Files.newOutputStream(CONFIG)) {
                p.store(out, "Marlow's Crystal Optimiser (client only, cosmetic)");
            }
        } catch (IOException ignored) {
        }
    }
}
