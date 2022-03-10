package com.yurisuika.seasonal.mixin;

import com.yurisuika.seasonal.Seasonal;
import com.yurisuika.seasonal.utils.Season;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import randommcsomethin.fallingleaves.config.LeafSettingsEntry;
import randommcsomethin.fallingleaves.util.LeafUtil;

import static randommcsomethin.fallingleaves.init.Config.CONFIG;

@Mixin(value = LeafUtil.class, remap = false)
public class LeafUtilMixin {

    @Inject(method = "getModifiedSpawnChance", at = @At("RETURN"))
    private static void injectSpawnChance(LeafSettingsEntry leafSettings, CallbackInfoReturnable<Double> cir) {
        double spawnChance = leafSettings.getSpawnChance();

        if (FabricLoader.getInstance().isModLoaded("seasonal")) {
            if (Seasonal.getCurrentSeason() == Season.EARLY_AUTUMN || Seasonal.getCurrentSeason() == Season.MID_AUTUMN || Seasonal.getCurrentSeason() == Season.LATE_AUTUMN) {
                spawnChance *= CONFIG.fallSpawnRateFactor;
            } else if (Seasonal.getCurrentSeason() == Season.EARLY_WINTER || Seasonal.getCurrentSeason() == Season.MID_WINTER || Seasonal.getCurrentSeason() == Season.LATE_WINTER) {
                spawnChance *= CONFIG.winterSpawnRateFactor;
            }
        }
    }

}
