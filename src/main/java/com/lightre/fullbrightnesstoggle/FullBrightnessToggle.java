package com.lightre.fullbrightnesstoggle;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import com.mojang.blaze3d.platform.InputConstants;

public class FullBrightnessToggle implements ClientModInitializer {

    private static KeyMapping toggleKey;
    private static boolean isFullBright = false;
    private static double previousGamma = 1.0;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.fullbright.toggle", InputConstants.KEY_G, KeyMapping.Category.register(Identifier.fromNamespaceAndPath("fullbrightnesstoggle", "main"))));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            checkGammaAndToggleState(client.options);

            while (toggleKey.consumeClick()) {
                toggleBrightness(client.options, client);
            }
        });
    }

    private void checkGammaAndToggleState(Options options) {
        OptionInstance<@NotNull Double> gammaOption = options.gamma();
        double currentGamma = gammaOption.get();
        boolean gammaIsFull = currentGamma >= 10.0;

        if (gammaIsFull && !isFullBright) {
            isFullBright = true;
            previousGamma = 1.0;
        } else if (!gammaIsFull && isFullBright) {
            isFullBright = false;
            previousGamma = currentGamma;
        }
    }

    private void toggleBrightness(Options options, Minecraft client) {
        OptionInstance<@NotNull Double> gammaOption = options.gamma();

        Component message;
        if (!isFullBright) {
            previousGamma = gammaOption.get();
            gammaOption.set(10.0);
            message = Component.literal("Full Brightness ").withStyle(ChatFormatting.WHITE).append(Component.literal("ON").withStyle(ChatFormatting.GREEN));
        } else {
            gammaOption.set(previousGamma);
            message = Component.literal("Full Brightness ").withStyle(ChatFormatting.WHITE).append(Component.literal("OFF").withStyle(ChatFormatting.RED));
        }

        if (client.player != null) {
            client.gui.hud.setOverlayMessage(message, false);
        }

        isFullBright = !isFullBright;

        client.options.save();
    }
}