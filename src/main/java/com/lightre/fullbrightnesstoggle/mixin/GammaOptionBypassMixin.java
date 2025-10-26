package com.lightre.fullbrightnesstoggle.mixin;

import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SimpleOption.DoubleSliderCallbacks.class)
public class GammaOptionBypassMixin {

    @Inject(method = "validate(Ljava/lang/Double;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    private void alwaysValid(Double value, CallbackInfoReturnable<Optional<Double>> cir) {
        // Gelen değeri logla
        System.out.println("[FullBrightnessToggle] validate çağrıldı. Gelen değer: " + value);

        try {
            Optional<Double> result = Optional.ofNullable(value);
            System.out.println("[FullBrightnessToggle] validate sonucu: " + result);
            cir.setReturnValue(result);
        } catch (Exception e) {
            System.err.println("[FullBrightnessToggle] validate sırasında hata oluştu:");
            e.printStackTrace(); // Stack trace logla
        }
    }
}