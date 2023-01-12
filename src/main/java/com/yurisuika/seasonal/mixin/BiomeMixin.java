package com.yurisuika.seasonal.mixin;

import com.mojang.serialization.Codec;
import com.yurisuika.seasonal.Seasonal;
import com.yurisuika.seasonal.colors.SeasonFoliageColors;
import com.yurisuika.seasonal.colors.SeasonGrassColors;
import com.yurisuika.seasonal.utils.ColorsCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(Biome.class)
public class BiomeMixin {

    @Shadow @Final public static Codec<RegistryEntry<Biome>> REGISTRY_CODEC;

    @SuppressWarnings("ConstantConditions")
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/BiomeEffects;getGrassColor()Ljava/util/Optional;"), method = "getGrassColorAt")
    public Optional<Integer> getSeasonGrassColor(BiomeEffects effects) {
        Biome biome = (Biome) ((Object) this);
        World world = MinecraftClient.getInstance().world;
        if(ColorsCache.hasGrassCache(biome)) {
            return ColorsCache.getGrassCache(biome);
        }
        else if(RegistryEntry.of(biome).isIn(BiomeTags.IS_BADLANDS)) {
            Optional<Integer> returnColor = effects.getGrassColor();
            if(world != null) {
                Optional<Integer> badlandsGrassColor = Optional.of(Seasonal.CONFIG.getMinecraftBadlandsGrass().getColor(Seasonal.getCurrentSeason()));
                if(badlandsGrassColor.isPresent()) {
                    returnColor = badlandsGrassColor;
                }
            }
            ColorsCache.createFoliageCache(biome, returnColor);
            return returnColor;
        }
        else {
            Optional<Integer> returnColor = effects.getGrassColor();
            if(world != null) {
                Identifier biomeIdentifier = world.getRegistryManager().get(Registry.BIOME_KEY).getId(biome);
                Optional<Integer> seasonGrassColor = Seasonal.CONFIG.getSeasonGrassColor(biome, biomeIdentifier, Seasonal.getCurrentSeason());
                if(seasonGrassColor.isPresent()) {
                    returnColor = seasonGrassColor;
                }
            }
            ColorsCache.createGrassCache(biome, returnColor);
            return returnColor;
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/BiomeEffects;getFoliageColor()Ljava/util/Optional;"), method = "getFoliageColor")
    public Optional<Integer> getSeasonFoliageColor(BiomeEffects effects) {
        Biome biome = (Biome) ((Object) this);
        World world = MinecraftClient.getInstance().world;
        if(ColorsCache.hasFoliageCache(biome)) {
            return ColorsCache.getFoliageCache(biome);
        }
        else if(RegistryEntry.of(biome).isIn(BiomeTags.IS_BADLANDS)) {
            Optional<Integer> returnColor = effects.getFoliageColor();
            if(world != null) {
                Optional<Integer> badlandsFoliageColor = Optional.of(Seasonal.CONFIG.getMinecraftBadlandsFoliage().getColor(Seasonal.getCurrentSeason()));
                if(badlandsFoliageColor.isPresent()) {
                    returnColor = badlandsFoliageColor;
                }
            }
            ColorsCache.createFoliageCache(biome, returnColor);
            return returnColor;
        }
        else{
            Optional<Integer> returnColor = effects.getFoliageColor();
            if(world != null) {
                Identifier biomeIdentifier = world.getRegistryManager().get(Registry.BIOME_KEY).getId(biome);
                Optional<Integer> seasonFoliageColor = Seasonal.CONFIG.getSeasonFoliageColor(biome, biomeIdentifier, Seasonal.getCurrentSeason());
                if(seasonFoliageColor.isPresent()) {
                    returnColor = seasonFoliageColor;
                }
            }
            ColorsCache.createFoliageCache(biome, returnColor);
            return returnColor;
        }

    }

    @SuppressWarnings("removal")
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/BiomeEffects$GrassColorModifier;getModifiedGrassColor(DDI)I"), method = "getGrassColorAt")
    public int getSeasonModifiedGrassColor(BiomeEffects.GrassColorModifier gcm, double x, double z, int color) {
        if(gcm == BiomeEffects.GrassColorModifier.SWAMP) {
            int swampColor1 = Seasonal.CONFIG.getMinecraftSwampGrass1().getColor(Seasonal.getCurrentSeason());
            int swampColor2 = Seasonal.CONFIG.getMinecraftSwampGrass2().getColor(Seasonal.getCurrentSeason());

            double d = Biome.FOLIAGE_NOISE.sample(x * 0.0225D, z * 0.0225D, false);
            return d < -0.1D ? swampColor1 : swampColor2;
        }else{
            return gcm.getModifiedGrassColor(x, z, color);
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/color/world/FoliageColors;getColor(DD)I"), method = "getDefaultFoliageColor")
    public int getSeasonDefaultFoliageColor(double d, double e) {
        return SeasonFoliageColors.getColor(Seasonal.getCurrentSeason(), d, e);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/color/world/GrassColors;getColor(DD)I"), method = "getDefaultGrassColor")
    public int getSeasonDefaultGrassColor(double d, double e) {
        return SeasonGrassColors.getColor(Seasonal.getCurrentSeason(), d, e);
    }

}
