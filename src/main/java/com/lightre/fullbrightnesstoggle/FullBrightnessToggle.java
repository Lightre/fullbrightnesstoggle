package com.lightre.fullbrightnesstoggle;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.option.KeyBinding;

import org.lwjgl.glfw.GLFW;

public class FullBrightnessToggle implements ClientModInitializer {

    private static KeyBinding toggleKey;
    private static boolean isFullBright = false;
    private static double previousGamma = 1.0;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fullbright.toggle", GLFW.GLFW_KEY_G, "category.fullbright"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            checkGammaAndToggleState(client.options);

            while (toggleKey.wasPressed()) {
                toggleBrightness(client.options);
            }
        });
    }

    private void checkGammaAndToggleState(GameOptions options) {
        SimpleOption<Double> gammaOption = options.getGamma();

        if (gammaOption.getValue() < 10.0 && isFullBright) {
            isFullBright = false;
            System.out.println("Full Brightness kapatıldı. Manuel değişiklik algılandı.");
            previousGamma = gammaOption.getValue();
        }
    }

    private void toggleBrightness(GameOptions options) {
        SimpleOption<Double> gammaOption = options.getGamma();

        if (!isFullBright) {
            previousGamma = gammaOption.getValue();
            gammaOption.setValue(10.0);
            System.out.println("Full Brightness açıldı.");
        } else {
            gammaOption.setValue(previousGamma);
            System.out.println("Full Brightness kapatıldı.");
        }

        isFullBright = !isFullBright;
    }
}