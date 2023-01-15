package com.yurisuika.seasonal.mixin;

import com.yurisuika.seasonal.Seasonal;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import randommcsomethin.fallingleaves.config.LeafSettingsEntry;
import randommcsomethin.fallingleaves.init.Config;
import randommcsomethin.fallingleaves.util.LeafUtil;

import static randommcsomethin.fallingleaves.util.LeafUtil.isLeafBlock;

@Mixin(value = LeafUtil.class, remap = false)
public class LeafUtilMixin {

    @Inject(method = "getModifiedSpawnChance", at = @At("RETURN"))
    private static void injectSpawnChance(BlockState state, LeafSettingsEntry leafSettings, CallbackInfoReturnable<Double> cir) {
        double spawnChance = leafSettings.getSpawnChance();

        if (FabricLoader.getInstance().isModLoaded("seasonal")) {
            spawnChance *= Seasonal.CONFIG.getSpawnRateFactor().getFactor(Seasonal.getCurrentSeason());
        }

        if (Config.CONFIG.decaySpawnRateFactor != 1.0D && isLeafBlock(state.getBlock(), true) && state.getBlock().hasRandomTicks(state)) {
            spawnChance *= Config.CONFIG.decaySpawnRateFactor;
        }
    }

}