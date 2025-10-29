package com.lightre.fullbrightnesstoggle;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import org.lwjgl.glfw.GLFW;

public class FullBrightnessToggle implements ClientModInitializer {

    private static KeyBinding toggleKey;
    private static boolean isFullBright = false;
    private static double previousGamma = 1.0;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fullbright.toggle", GLFW.GLFW_KEY_G, "category.fullbright"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            checkGammaAndToggleState(client.options);

            while (toggleKey.wasPressed()) {
                toggleBrightness(client.options, client);
            }
        });
    }

    private void checkGammaAndToggleState(GameOptions options) {
        SimpleOption<Double> gammaOption = options.getGamma();
        double currentGamma = gammaOption.getValue();
        boolean gammaIsFull = currentGamma >= 10.0;

        if (gammaIsFull && !isFullBright) {
            isFullBright = true;
            previousGamma = 1.0;
        } else if (!gammaIsFull && isFullBright) {
            isFullBright = false;
            previousGamma = currentGamma;
        }
    }

    private void toggleBrightness(GameOptions options, net.minecraft.client.MinecraftClient client) {
        SimpleOption<Double> gammaOption = options.getGamma();

        Text message;
        if (!isFullBright) {
            previousGamma = gammaOption.getValue();
            gammaOption.setValue(10.0);
            message = Text.literal("Full Brightness ").formatted(Formatting.WHITE).append(Text.literal("ON").formatted(Formatting.GREEN));
        } else {
            gammaOption.setValue(previousGamma);
            message = Text.literal("Full Brightness ").formatted(Formatting.WHITE).append(Text.literal("OFF").formatted(Formatting.RED));
        }

        client.inGameHud.setOverlayMessage(message, false);
        isFullBright = !isFullBright;

        client.options.write();
    }
}