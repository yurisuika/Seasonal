package com.yurisuika.seasonal.mixin;

import com.yurisuika.seasonal.Seasonal;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({CropBlock.class, CocoaBlock.class, StemBlock.class, SaplingBlock.class})
public abstract class FertilizableMixin extends Block implements Fertilizable {

    private boolean seasonal$shouldInject = true;

    public FertilizableMixin(Settings settings) {
        super(settings);
    }

    @SuppressWarnings("deprecation")
    @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
    public void randomTickInject(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        boolean shouldGrowNormally = (world.getLightLevel(LightType.SKY, pos) == 0 && Seasonal.CONFIG.doCropsGrowsNormallyUnderground());
        if(!shouldGrowNormally && Seasonal.CONFIG.isSeasonMessingCrops() && seasonal$shouldInject) {
            Identifier cropIdentifier = Registry.BLOCK.getId(state.getBlock());
            float multiplier = Seasonal.CONFIG.getSeasonCropMultiplier(cropIdentifier, Seasonal.getCurrentSeason(world));
            while(multiplier > 0f) {
                float rand = random.nextFloat();
                if(multiplier >= rand) {
                    seasonal$shouldInject = false;
                    this.randomTick(state, world, pos, random);
                    multiplier -= 1f;
                }
            }
            seasonal$shouldInject = true;
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "grow", cancellable = true)
    public void growInject(ServerWorld world, Random random, BlockPos pos, BlockState state, CallbackInfo ci) {
        boolean shouldGrowNormally = (world.getLightLevel(LightType.SKY, pos) == 0 && Seasonal.CONFIG.doCropsGrowsNormallyUnderground());
        if(!shouldGrowNormally && Seasonal.CONFIG.isSeasonMessingBonemeal() && seasonal$shouldInject) {
            Identifier cropIdentifier = Registry.BLOCK.getId(state.getBlock());
            float multiplier = Seasonal.CONFIG.getSeasonCropMultiplier(cropIdentifier, Seasonal.getCurrentSeason(world));
            while(multiplier > 0f) {
                float rand = random.nextFloat();
                if(multiplier >= rand) {
                    seasonal$shouldInject = false;
                    this.grow(world, random, pos, state);
                    multiplier -= 1f;
                }
            }
            seasonal$shouldInject = true;
            ci.cancel();
        }
    }

}