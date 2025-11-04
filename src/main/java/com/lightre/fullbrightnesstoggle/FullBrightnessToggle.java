package com.lightre.fullbrightnesstoggle;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
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
        double currentGamma = options.gamma;
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
        Text message;
        if (!isFullBright) {
            previousGamma = options.gamma;
            options.gamma = 10.0;
            message = new LiteralText("Full Brightness ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)).append(new LiteralText("ON").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
        } else {
            options.gamma = previousGamma;

            message = new LiteralText("Full Brightness ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)).append(new LiteralText("OFF").setStyle(Style.EMPTY.withColor(Formatting.RED)));
        }

        client.inGameHud.setOverlayMessage(message, false);
        isFullBright = !isFullBright;

        client.options.write();
    }
}