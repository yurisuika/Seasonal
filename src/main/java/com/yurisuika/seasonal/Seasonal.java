package com.yurisuika.seasonal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.yurisuika.seasonal.commands.SeasonCommand;
import com.yurisuika.seasonal.mixin.WeatherAccessor;
import com.yurisuika.seasonal.utils.ModConfig;
import com.yurisuika.seasonal.utils.Season;
import com.yurisuika.seasonal.utils.WeatherCache;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Seasonal implements ModInitializer {

    public static final String MOD_ID = "seasonal";
    public static final Logger LOGGER = LogManager.getLogger("Seasonal");

    public static ModConfig CONFIG;

    public static final JsonParser JSON_PARSER = new JsonParser();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Block ORIGINAL_ICE;
    public static Block ORIGINAL_SNOW;

    public static HashMap<Item, Block> SEEDS_MAP = new HashMap<>();


    public static Identifier ASK_FOR_CONFIG = new Identifier(MOD_ID, "ask_for_config");
    public static Identifier ANSWER_CONFIG = new Identifier(MOD_ID, "answer_config");

    @Override
    public void onInitialize() {

        Path configPath = FabricLoader.getInstance().getConfigDir();
        File configFile = new File(configPath + File.separator + "seasonal.json");

        LOGGER.info("Trying to read config file...");
        try {
            if (configFile.createNewFile()) {
                LOGGER.info("No config file found, creating a new one...");
                String json = GSON.toJson(JSON_PARSER.parse(GSON.toJson(new ModConfig())));
                try (PrintWriter out = new PrintWriter(configFile)) {
                    out.println(json);
                }
                CONFIG = new ModConfig();
                LOGGER.info("Successfully created default config file.");
            } else {
                LOGGER.info("A config file was found, loading it..");
                CONFIG = GSON.fromJson(new String(Files.readAllBytes(configFile.toPath())), ModConfig.class);
                if(CONFIG == null) {
                    throw new NullPointerException("The config file was empty.");
                }else{
                    LOGGER.info("Successfully loaded config file.");
                }
            }
        }catch (Exception exception) {
            LOGGER.error("There was an error creating/loading the config file!", exception);
            CONFIG = new ModConfig();
            LOGGER.warn("Defaulting to original config.");
        }

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, dedicated) -> SeasonCommand.register(dispatcher, registryAccess));

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            SEEDS_MAP.clear();
            Registry.ITEM.forEach(item -> {
                if(item instanceof BlockItem) {
                    Block block = ((BlockItem) item).getBlock();
                    if(block instanceof CropBlock || block instanceof StemBlock || block instanceof CocoaBlock || block instanceof SaplingBlock) {
                        Seasonal.SEEDS_MAP.put(item, ((BlockItem) item).getBlock());
                    }
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(ASK_FOR_CONFIG, (server, player, handler, buf, responseSender) -> {
            String configJson = GSON.toJson(JSON_PARSER.parse(GSON.toJson(CONFIG)));
            PacketByteBuf configBuf = PacketByteBufs.create();
            configBuf.writeInt(configJson.length());
            configBuf.writeString(configJson, configJson.length());
            ServerPlayNetworking.send(player, ANSWER_CONFIG, configBuf);
        });
    }

    public static Season getCurrentSeason(World world) {
        RegistryKey<World> dimension = world.getRegistryKey();
        if (CONFIG.isValidInDimension(dimension)) {
            if(CONFIG.isSeasonLocked()) {
                return CONFIG.getLockedSeason();
            }
            if(CONFIG.isSeasonTiedWithSystemTime()) {
                return getCurrentSystemSeason();
            }
            int worldTime = Math.toIntExact(world.getTimeOfDay());
            int seasonTime = (worldTime / CONFIG.getSeasonLength());
            return Season.values()[seasonTime % 12];
        }
        return Season.EARLY_SPRING;
    }

    @Environment(EnvType.CLIENT)
    public static Season getCurrentSeason() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if(player != null && player.world != null) {
            return getCurrentSeason(player.world);
        }
        return Season.EARLY_SPRING;
    }

    private static Season getCurrentSystemSeason() {
        LocalDateTime date = LocalDateTime.now();
        int m = date.getMonthValue();
        int d = date.getDayOfMonth();
        Season season;

        if (CONFIG.isInNorthHemisphere()) {
            if (m == 1)
                season = Season.EARLY_WINTER;
            else if (m == 2)
                season = Season.MID_WINTER;
            else if (m == 3)
                season = Season.LATE_WINTER;
            else if (m == 4)
                season = Season.EARLY_SPRING;
            else if (m == 5)
                season = Season.MID_SPRING;
            else if (m == 6)
                season = Season.LATE_SPRING;
            else if (m == 7)
                season = Season.EARLY_SUMMER;
            else if (m == 8)
                season = Season.MID_SUMMER;
            else if (m == 9)
                season = Season.LATE_SUMMER;
            else if (m == 10)
                season = Season.EARLY_AUTUMN;
            else if (m == 11)
                season = Season.MID_AUTUMN;
            else
                season = Season.LATE_AUTUMN;

            if (m == 3 && d > 19)
                season = Season.EARLY_SPRING;
            else if (m == 6 && d > 20)
                season = Season.EARLY_SUMMER;
            else if (m == 9 && d > 21)
                season = Season.EARLY_AUTUMN;
            else if (m == 12 && d > 20)
                season = Season.EARLY_WINTER;
        } else {
            if (m == 1)
                season = Season.EARLY_SUMMER;
            else if (m == 2)
                season = Season.MID_SUMMER;
            else if (m == 3)
                season = Season.LATE_SUMMER;
            else if (m == 4)
                season = Season.EARLY_AUTUMN;
            else if (m == 5)
                season = Season.MID_AUTUMN;
            else if (m == 6)
                season = Season.LATE_AUTUMN;
            else if (m == 7)
                season = Season.EARLY_WINTER;
            else if (m == 8)
                season = Season.MID_WINTER;
            else if (m == 9)
                season = Season.LATE_WINTER;
            else if (m == 10)
                season = Season.EARLY_SPRING;
            else if (m == 11)
                season = Season.MID_SPRING;
            else
                season = Season.LATE_SPRING;

            if (m == 3 && d > 19)
                season = Season.EARLY_AUTUMN;
            else if (m == 6 && d > 20)
                season = Season.EARLY_WINTER;
            else if (m == 9 && d > 21)
                season = Season.EARLY_SPRING;
            else if (m == 12 && d > 20)
                season = Season.EARLY_SUMMER;
        }

        return season;
    }

    public static void injectBiomeTemperature(RegistryEntry<Biome> biome, World world) {
        if(!CONFIG.doTemperatureChanges()) return;

        List<TagKey<Biome>> ignoredCategories = Arrays.asList(BiomeTags.IS_NETHER, BiomeTags.IS_END, BiomeTags.IS_OCEAN);
        if(ignoredCategories.stream().anyMatch(biome::isIn)) return;

        Season season = Seasonal.getCurrentSeason(world);

        Identifier biomeIdentifier = world.getRegistryManager().get(Registry.BIOME_KEY).getId(biome.value());
        Biome.Weather currentWeather = biome.value().weather;

        Biome.Weather originalWeather;
        if (!WeatherCache.hasCache(biomeIdentifier)) {
            originalWeather = new Biome.Weather(currentWeather.precipitation, currentWeather.temperature, currentWeather.temperatureModifier, currentWeather.downfall);
            WeatherCache.setCache(biomeIdentifier, originalWeather);
        } else {
            originalWeather = WeatherCache.getCache(biomeIdentifier);
        }

        if(originalWeather == null) {
            return;
        }
        float temp = originalWeather.temperature;
        if(biome.isIn(BiomeTags.IS_JUNGLE) || biome.isIn(BiomeTags.HAS_CLOSER_WATER_FOG)) {
            //Jungle Biomes
            if (season == Season.EARLY_WINTER | season == Season.MID_WINTER | season == Season.LATE_WINTER) {
                ((WeatherAccessor) (Object) currentWeather).setPrecipitation(originalWeather.precipitation);
                ((WeatherAccessor) (Object) currentWeather).setTemperature(temp - 0.1f);
            } else {
                ((WeatherAccessor) (Object) currentWeather).setPrecipitation(originalWeather.precipitation);
                ((WeatherAccessor) (Object) currentWeather).setTemperature(temp);
            }
        }
        else if(biome.isIn(BiomeTags.IS_BADLANDS)) {
            //Badlands Biomes
            switch (season) {
                case EARLY_SPRING, MID_SPRING, LATE_SPRING -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(Biome.Precipitation.RAIN);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp);
                }
                case EARLY_SUMMER, MID_SUMMER, LATE_SUMMER -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(originalWeather.precipitation);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp + 0.2f);
                }
                case EARLY_WINTER, MID_WINTER, LATE_WINTER -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(Biome.Precipitation.SNOW);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp - 2.0f);
                }
                default -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(originalWeather.precipitation);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp);
                }
            }
        }
        else if(temp <= 0.1) {
            //Frozen Biomes
            switch (season) {
                case EARLY_SUMMER, MID_SUMMER, LATE_SUMMER -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(Biome.Precipitation.RAIN);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp + 0.3f);
                }
                case EARLY_WINTER, MID_WINTER, LATE_WINTER -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(Biome.Precipitation.SNOW);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp - 0.2f);
                }
                default -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(originalWeather.precipitation);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp);
                }
            }
        }
        else if(temp <= 0.3) {
            //Cold Biomes
            switch (season) {
                case EARLY_SPRING, MID_SPRING, LATE_SPRING -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(Biome.Precipitation.RAIN);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp);
                }
                case EARLY_SUMMER, MID_SUMMER, LATE_SUMMER -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(Biome.Precipitation.RAIN);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp + 0.2f);
                }
                case EARLY_WINTER, MID_WINTER, LATE_WINTER -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(Biome.Precipitation.SNOW);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp - 0.2f);
                }
                default -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(originalWeather.precipitation);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp);
                }
            }
        }
        else if(temp <= 0.95) {
            //Temperate Biomes
            switch (season) {
                case EARLY_SUMMER, MID_SUMMER, LATE_SUMMER -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(originalWeather.precipitation);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp + 0.2f);
                }
                case EARLY_AUTUMN, MID_AUTUMN, LATE_AUTUMN -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(originalWeather.precipitation);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp - 0.1f);
                }
                case EARLY_WINTER, MID_WINTER, LATE_WINTER -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(Biome.Precipitation.SNOW);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp - 0.7f);
                }
                default -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(originalWeather.precipitation);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp);
                }
            }
        }
        else {
            //Hot Biomes
            switch (season) {
                case EARLY_SUMMER, MID_SUMMER, LATE_SUMMER -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(originalWeather.precipitation);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp + 0.2f);
                }
                case EARLY_WINTER, MID_WINTER, LATE_WINTER -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(Biome.Precipitation.RAIN);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp - 0.2f);
                }
                default -> {
                    ((WeatherAccessor) (Object) currentWeather).setPrecipitation(originalWeather.precipitation);
                    ((WeatherAccessor) (Object) currentWeather).setTemperature(temp);
                }
            }
        }
    }

}