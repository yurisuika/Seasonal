package com.yurisuika.seasonal.mixin;

import com.yurisuika.seasonal.Seasonal;
import com.yurisuika.seasonal.utils.Season;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimalEntity.class)
public class AnimalEntityMixin {

    @Inject(at = @At("HEAD"), method = "breed", cancellable = true)
    public void breedInject(ServerWorld serverWorld, AnimalEntity animalEntity, CallbackInfo info) {
        if(Seasonal.getCurrentSeason(serverWorld) == Season.EARLY_WINTER && !Seasonal.CONFIG.doAnimalsBreedInWinter()) {
            info.cancel();
        }
        else if(Seasonal.getCurrentSeason(serverWorld) == Season.MID_WINTER && !Seasonal.CONFIG.doAnimalsBreedInWinter()) {
            info.cancel();
        }
        else if(Seasonal.getCurrentSeason(serverWorld) == Season.LATE_WINTER && !Seasonal.CONFIG.doAnimalsBreedInWinter()) {
            info.cancel();
        }
    }

}