package com.yurisuika.seasons.utils;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection", "unused"})
public class ModConfig {

    public static class SeasonLock {
        public boolean isSeasonLocked = false;
        public Season lockedSeason = Season.EARLY_SPRING;
    }

    public static class HardcodedColors {
        public int earlySpringColor;
        public int midSpringColor;
        public int lateSpringColor;
        public int earlySummerColor;
        public int midSummerColor;
        public int lateSummerColor;
        public int earlyAutumnColor;
        public int midAutumnColor;
        public int lateAutumnColor;
        public int earlyWinterColor;
        public int midWinterColor;
        public int lateWinterColor;

        HardcodedColors(int earlySpringColor, int midSpringColor, int lateSpringColor, int earlySummerColor, int midSummerColor, int lateSummerColor, int earlyAutumnColor, int midAutumnColor, int lateAutumnColor, int earlyWinterColor, int midWinterColor, int lateWinterColor) {
            this.earlySpringColor = earlySpringColor;
            this.midSpringColor = midSpringColor;
            this.lateSpringColor = lateSpringColor;
            this.earlySummerColor = earlySummerColor;
            this.midSummerColor = midSummerColor;
            this.lateSummerColor = lateSummerColor;
            this.earlyAutumnColor = earlyAutumnColor;
            this.midAutumnColor = midAutumnColor;
            this.lateAutumnColor = lateAutumnColor;
            this.earlyWinterColor = earlyWinterColor;
            this.midWinterColor = midWinterColor;
            this.lateWinterColor = lateWinterColor;
        }

        public int getColor(Season season) {
            return switch (season) {
                case EARLY_SPRING -> earlySpringColor;
                case MID_SPRING -> midSpringColor;
                case LATE_SPRING -> lateSpringColor;
                case EARLY_SUMMER -> earlySummerColor;
                case MID_SUMMER -> midSummerColor;
                case LATE_SUMMER -> lateSummerColor;
                case EARLY_AUTUMN -> earlyAutumnColor;
                case MID_AUTUMN -> midAutumnColor;
                case LATE_AUTUMN -> lateAutumnColor;
                case EARLY_WINTER -> earlyWinterColor;
                case MID_WINTER -> midWinterColor;
                case LATE_WINTER -> lateWinterColor;
            };
        }
    }

    public static class BiomeColors {

        public String biomeIdentifier;
        public HardcodedColors colors;

        BiomeColors() {
            this.biomeIdentifier = "";
            this.colors = new HardcodedColors(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0);
        }

        BiomeColors(String biomeIdentifier, int earlySpringColor, int midSpringColor, int lateSpringColor, int earlySummerColor, int midSummerColor, int lateSummerColor, int earlyAutumnColor, int midAutumnColor, int lateAutumnColor, int earlyWinterColor, int midWinterColor, int lateWinterColor) {
            this.biomeIdentifier = biomeIdentifier;
            this.colors = new HardcodedColors(earlySpringColor, midSpringColor, lateSpringColor, earlySummerColor, midSummerColor, lateSummerColor, earlyAutumnColor, midAutumnColor, lateAutumnColor, earlyWinterColor, midWinterColor, lateWinterColor);
        }

    }


    public int seasonLength = 240000;

    public SeasonLock seasonLock = new SeasonLock();

    public List<String> dimensionWhitelist = List.of("minecraft:overworld");

    public boolean doTemperatureChanges = true;

    public boolean isSeasonTiedWithSystemTime = false;

    public boolean isInNorthHemisphere = true;

    public HardcodedColors minecraftDefaultFoliage = new HardcodedColors(0x44BD0F, 0x23C20A, 0x05C80A, 0x2AB814, 0x48B518, 0x7FC20A, 0xC7CD00, 0xC89C05, 0xBDA50F, 0xB8AE14, 0xB0B518, 0x6FB518);
    public HardcodedColors minecraftSpruceFoliage = new HardcodedColors(0x5E9C68, 0x5D9C6A, 0x5E9C68, 0x5E9B66, 0x5F9A65, 0x609A64, 0x609962, 0x619961, 0x609962, 0x609A64, 0x5F9A65, 0x5E9B66);
    public HardcodedColors minecraftBirchFoliage = new HardcodedColors(0x80AB50, 0x70AE4E, 0x5FB14B, 0x72A953, 0x80A755, 0xA0AE4E, 0xB8B543, 0xC1A73A, 0xB18C4B, 0xAB9250, 0xA99853, 0x96A953);
    public HardcodedColors minecraftSwampGrass1 = new HardcodedColors(0x407939, 0x3D7A38, 0x407939, 0x427839, 0x45783A, 0x47773B, 0x49763B, 0x4C763C, 0x49763B, 0x47773B, 0x45783A, 0x427839);
    public HardcodedColors minecraftSwampGrass2 = new HardcodedColors(0x627336, 0x607335, 0x627336, 0x637236, 0x657137, 0x677138, 0x687038, 0x6A7039, 0x687038, 0x677138, 0x657137, 0x637236);
    public HardcodedColors minecraftBadlandsGrass = new HardcodedColors(0x907B4D, 0x90814D, 0x907B4D, 0x90764D, 0x90704D, 0x906B4D, 0x90654D, 0x90604D, 0x90654D, 0x906B4D, 0x90704D, 0x90764D);
    public HardcodedColors minecraftBadlandsFoliage = new HardcodedColors(0x9E7E4D, 0x9E814D, 0x9E7E4D, 0x9E7C4D, 0x9E794D, 0x9E764D, 0x9E744D, 0x9E714D, 0x9E744D, 0x9E764D, 0x9E794D, 0x9E7C4D);

    public boolean isDefaultHSBShiftEnabled = false;

    public static class HSBShift {
        public float hue;
        public float saturation;
        public float brightness;

        public HSBShift(float hue, float saturation, float brightness) {
            this.hue = hue;
            this.saturation = saturation;
            this.brightness = brightness;
        }
    }

    public static class DefaultHSBShift {
        public HSBShift earlySpringHSBShift = new HSBShift(0f, 95f, -10f);
        public HSBShift midSpringHSBShift = new HSBShift(0f, 100f, 0f);
        public HSBShift lateSpringHSBShift = new HSBShift(0f, 120f, -4f);
        public HSBShift earlySummerHSBShift = new HSBShift(0f, 130f, -8f);
        public HSBShift midSummerHSBShift = new HSBShift(0f, 150f, -10f);
        public HSBShift lateSummerHSBShift = new HSBShift(0f, 140f, -12f);
        public HSBShift earlyAutumnHSBShift = new HSBShift(-65f, 135f, -13f);
        public HSBShift midAutumnHSBShift = new HSBShift(-65f, 125f, -15f);
        public HSBShift lateAutumnHSBShift = new HSBShift(-65f, 110f, -25f);
        public HSBShift earlyWinterHSBShift = new HSBShift(-65f, 95f, -30f);
        public HSBShift midWinterHSBShift = new HSBShift(-65f, 80f, -40f);
        public HSBShift lateWinterHSBShift = new HSBShift(-65f, 85f, -20f);

        public HSBShift getHSBShift(Season season) {
            return switch (season) {
                case EARLY_SPRING -> earlySpringHSBShift;
                case MID_SPRING -> midSpringHSBShift;
                case LATE_SPRING -> lateSpringHSBShift;
                case EARLY_SUMMER -> earlySummerHSBShift;
                case MID_SUMMER -> midSummerHSBShift;
                case LATE_SUMMER -> lateSummerHSBShift;
                case EARLY_AUTUMN -> earlyAutumnHSBShift;
                case MID_AUTUMN -> midAutumnHSBShift;
                case LATE_AUTUMN -> lateAutumnHSBShift;
                case EARLY_WINTER -> earlyWinterHSBShift;
                case MID_WINTER -> midWinterHSBShift;
                case LATE_WINTER -> lateWinterHSBShift;
            };
        }

    }


    public DefaultHSBShift defaultHSBShift = new DefaultHSBShift();

    public final List<BiomeColors> foliageColorList = new ArrayList<>();
    public final List<BiomeColors> grassColorList = new ArrayList<>();

    public int getShiftedColor(Season season, int defaultColor) {
        Color initialColor = new Color(defaultColor);
        HSBShift hueShift = defaultHSBShift.getHSBShift(season);
        Color finalColor = ColorHelper.changeHueSatBri(initialColor, hueShift.hue, hueShift.saturation, hueShift.brightness);
        return finalColor.getRGB();
    }

    public Optional<Integer> getSeasonFoliageColor(Biome biome, Identifier biomeIdentifier, Season season) {
        Optional<BiomeColors> colors = foliageColorList.stream().filter(it -> it.biomeIdentifier.equals(biomeIdentifier.toString())).findFirst();
        Optional<Integer> color = colors.map(biomeColors -> biomeColors.colors.getColor(season));
        if(color.isEmpty() && isDefaultHSBShiftEnabled) {
            Optional<Integer> defaultColor = biome.getEffects().getFoliageColor();
            if(defaultColor.isPresent()) {
                return Optional.of(getShiftedColor(season, defaultColor.get()));
            }
        }
        return color;
    }


    public Optional<Integer> getSeasonGrassColor(Biome biome, Identifier biomeIdentifier, Season season) {
        Optional<BiomeColors> colors = grassColorList.stream().filter(it -> it.biomeIdentifier.equals(biomeIdentifier.toString())).findFirst();
        Optional<Integer> color = colors.map(biomeColors -> biomeColors.colors.getColor(season));
        if(color.isEmpty() && isDefaultHSBShiftEnabled) {
            Optional<Integer> defaultColor = biome.getEffects().getGrassColor();
            if(defaultColor.isPresent()) {
                return Optional.of(getShiftedColor(season, defaultColor.get()));
            }
        }
        return color;
    }

    public static class DefaultCropConfig {

        public float earlySpringModifier, midSpringModifier, lateSpringModifier, earlySummerModifier, midSummerModifier, lateSummerModifier, earlyAutumnModifier, midAutumnModifier, lateAutumnModifier, earlyWinterModifier, midWinterModifier, lateWinterModifier;

        public DefaultCropConfig(float earlySpringModifier, float midSpringModifier, float lateSpringModifier, float earlySummerModifier, float midSummerModifier, float lateSummerModifier, float earlyAutumnModifier, float midAutumnModifier, float lateAutumnModifier, float earlyWinterModifier, float midWinterModifier, float lateWinterModifier) {
            this.earlySpringModifier = earlySpringModifier;
            this.midSpringModifier = midSpringModifier;
            this.lateSpringModifier = lateSpringModifier;
            this.earlySummerModifier = earlySummerModifier;
            this.midSummerModifier = midSummerModifier;
            this.lateSummerModifier = lateSummerModifier;
            this.earlyAutumnModifier = earlyAutumnModifier;
            this.midAutumnModifier = midAutumnModifier;
            this.lateAutumnModifier = lateAutumnModifier;
            this.earlyWinterModifier = earlyWinterModifier;
            this.midWinterModifier = midWinterModifier;
            this.lateWinterModifier = lateWinterModifier;
        }

        public float getModifier(Season season) {
            return switch (season) {
                case EARLY_SPRING -> earlySpringModifier;
                case MID_SPRING -> midSpringModifier;
                case LATE_SPRING -> lateSpringModifier;
                case EARLY_SUMMER -> earlySummerModifier;
                case MID_SUMMER -> midSummerModifier;
                case LATE_SUMMER -> lateSummerModifier;
                case EARLY_AUTUMN -> earlyAutumnModifier;
                case MID_AUTUMN -> midAutumnModifier;
                case LATE_AUTUMN -> lateAutumnModifier;
                case EARLY_WINTER -> earlyWinterModifier;
                case MID_WINTER -> midWinterModifier;
                case LATE_WINTER -> lateWinterModifier;
            };
        }
    }

    public static class CropConfig {

        public String cropIdentifier;
        public float earlySpringModifier, midSpringModifier, lateSpringModifier, earlySummerModifier, midSummerModifier, lateSummerModifier, earlyAutumnModifier, midAutumnModifier, lateAutumnModifier, earlyWinterModifier, midWinterModifier, lateWinterModifier;

        public CropConfig() {
            this.cropIdentifier = "";
            this.earlySpringModifier = 0.6f;
            this.midSpringModifier = 0.8f;
            this.lateSpringModifier = 1.0f;
            this.earlySummerModifier = 0.9f;
            this.midSummerModifier = 0.8f;
            this.lateSummerModifier = 0.7f;
            this.earlyAutumnModifier = 0.6f;
            this.midAutumnModifier = 0.5f;
            this.lateAutumnModifier = 0.3f;
            this.earlyWinterModifier = 0.1f;
            this.midWinterModifier = 0.0f;
            this.lateWinterModifier = 0.0f;
        }

        public CropConfig(String cropIdentifier, float earlySpringModifier, float midSpringModifier, float lateSpringModifier, float earlySummerModifier, float midSummerModifier, float lateSummerModifier, float earlyAutumnModifier, float midAutumnModifier, float lateAutumnModifier, float earlyWinterModifier, float midWinterModifier, float lateWinterModifier) {
            this.cropIdentifier = cropIdentifier;
            this.earlySpringModifier = earlySpringModifier;
            this.midSpringModifier = midSpringModifier;
            this.lateSpringModifier = lateSpringModifier;
            this.earlySummerModifier = earlySummerModifier;
            this.midSummerModifier = midSummerModifier;
            this.lateSummerModifier = lateSummerModifier;
            this.earlyAutumnModifier = earlyAutumnModifier;
            this.midAutumnModifier = midAutumnModifier;
            this.lateAutumnModifier = lateAutumnModifier;
            this.earlyWinterModifier = earlyWinterModifier;
            this.midWinterModifier = midWinterModifier;
            this.lateWinterModifier = lateWinterModifier;
        }

        public float getModifier(Season season) {
            return switch (season) {
                case EARLY_SPRING -> earlySpringModifier;
                case MID_SPRING -> midSpringModifier;
                case LATE_SPRING -> lateSpringModifier;
                case EARLY_SUMMER -> earlySummerModifier;
                case MID_SUMMER -> midSummerModifier;
                case LATE_SUMMER -> lateSummerModifier;
                case EARLY_AUTUMN -> earlyAutumnModifier;
                case MID_AUTUMN -> midAutumnModifier;
                case LATE_AUTUMN -> lateAutumnModifier;
                case EARLY_WINTER -> earlyWinterModifier;
                case MID_WINTER -> midWinterModifier;
                case LATE_WINTER -> lateWinterModifier;
            };
        }

    }

    public boolean isSeasonMessingCrops = true;
    public boolean isSeasonMessingBonemeal = false;
    public boolean doCropsGrowsNormallyUnderground = false;

    public DefaultCropConfig defaultCropConfig = new DefaultCropConfig(0.6f, 0.8f, 1.0f, 0.9f, 0.8f, 0.7f, 0.6f, 0.5f, 0.3f, 0.1f, 0.0f, 0.0f);

    public final List<CropConfig> cropConfigs = new ArrayList<>();

    public float getSeasonCropMultiplier(Identifier cropIdentifier, Season season) {
        Optional<CropConfig> config = cropConfigs.stream().filter(it -> it.cropIdentifier.equals(cropIdentifier.toString())).findFirst();
        return config.map(cropConfig -> cropConfig.getModifier(season)).orElse(defaultCropConfig.getModifier(season));
    }

    public boolean doAnimalsBreedInWinter = false;

    public boolean doAnimalsBreedInWinter() {
        return doAnimalsBreedInWinter;
    }

    public boolean isSeasonMessingCrops() {
        return isSeasonMessingCrops;
    }

    public boolean isSeasonMessingBonemeal() {
        return isSeasonMessingBonemeal;
    }

    public boolean doCropsGrowsNormallyUnderground() {
        return doCropsGrowsNormallyUnderground;
    }

    public HardcodedColors getMinecraftDefaultFoliage() {
        return minecraftDefaultFoliage;
    }

    public HardcodedColors getMinecraftSpruceFoliage() {
        return minecraftSpruceFoliage;
    }

    public HardcodedColors getMinecraftBirchFoliage() {
        return minecraftBirchFoliage;
    }

    public HardcodedColors getMinecraftSwampGrass1() {
        return minecraftSwampGrass1;
    }

    public HardcodedColors getMinecraftSwampGrass2() {
        return minecraftSwampGrass2;
    }

    public HardcodedColors getMinecraftBadlandsGrass() {
        return minecraftBadlandsGrass;
    }

    public HardcodedColors getMinecraftBadlandsFoliage() {
        return minecraftBadlandsFoliage;
    }

    public boolean doTemperatureChanges() {
        return doTemperatureChanges;
    }

    public int getSeasonLength() {
        return seasonLength;
    }

    public boolean isSeasonLocked() {
        return seasonLock.isSeasonLocked;
    }

    public Season getLockedSeason() {
        return seasonLock.lockedSeason;
    }

    public boolean isValidInDimension(RegistryKey<World> dimension) {
        return dimensionWhitelist.contains(dimension.getValue().toString());
    }

    public boolean isSeasonTiedWithSystemTime() {
        return isSeasonTiedWithSystemTime;
    }

    public boolean isInNorthHemisphere() {
        return isInNorthHemisphere;
    }
}
