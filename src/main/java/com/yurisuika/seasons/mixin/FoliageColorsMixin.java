package com.yurisuika.seasons.mixin;

import com.yurisuika.seasons.Seasons;
import com.yurisuika.seasons.colors.SeasonFoliageColors;
import net.minecraft.client.color.world.FoliageColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FoliageColors.class, priority = 2000)
public class FoliageColorsMixin {

    @Inject(method = "getSpruceColor", at = @At("RETURN"), cancellable = true)
    private static void injectSpruceColor(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(SeasonFoliageColors.getSpruceColor(Seasons.getCurrentSeason()));
    }

    @Inject(method = "getBirchColor", at = @At("RETURN"), cancellable = true)
    private static void injectBirchColor(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(SeasonFoliageColors.getBirchColor(Seasons.getCurrentSeason()));
    }

    @Inject(method = "getDefaultColor", at = @At("RETURN"), cancellable = true)
    private static void injectDefaultColor(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(SeasonFoliageColors.getDefaultColor(Seasons.getCurrentSeason()));
    }

}
