package com.lightre.fullbrightnesstoggle.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
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
public class GammaValidationMixin {
    @Unique
    private static final Logger LOGGER = LogManager.getLogger("FullBrightnessToggle");

    @Inject(method = "validate(Ljava/lang/Double;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    private void alwaysValid(Double value, CallbackInfoReturnable<Optional<Double>> cir) {
        try {
            cir.setReturnValue(Optional.ofNullable(value));
        } catch (Exception e) {
            LOGGER.error("[FullBrightnessToggle] validate error", e);
        }
    }

    @Inject(method = "codec", at = @At("RETURN"), cancellable = true)
    private void overrideCodec(CallbackInfoReturnable<Codec<Double>> cir) {
        cir.setReturnValue(
                Codec.DOUBLE.flatXmap(
                        DataResult::success,
                        DataResult::success
                )
        );
    }
}
