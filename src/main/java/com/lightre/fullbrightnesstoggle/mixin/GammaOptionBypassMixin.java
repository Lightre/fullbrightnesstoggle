package com.lightre.fullbrightnesstoggle.mixin;

import net.minecraft.client.option.SimpleOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;


@Mixin(SimpleOption.DoubleSliderCallbacks.class)
public class GammaOptionBypassMixin {
    @Unique
    private static final Logger LOGGER = LogManager.getLogger("FullBrightnessToggle");

    @Inject(method = "validate(Ljava/lang/Double;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    private void alwaysValid(Double value, CallbackInfoReturnable<Optional<Double>> cir) {
        LOGGER.info("[FullBrightnessToggle] validate çağrıldı. Gelen değer: {}", value);
        try {
            Optional<Double> result = Optional.ofNullable(value);
            LOGGER.info("[FullBrightnessToggle] validate sonucu: {}", result);
            cir.setReturnValue(result);
        } catch (Exception e) {
            LOGGER.error("[FullBrightnessToggle] validate sırasında hata oluştu", e);
        }
    }
}