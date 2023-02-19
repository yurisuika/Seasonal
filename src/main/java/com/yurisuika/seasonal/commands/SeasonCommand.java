package com.yurisuika.seasonal.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.yurisuika.seasonal.Seasonal;
import com.yurisuika.seasonal.utils.Season;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TimeCommand;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.Locale;

public class SeasonCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(CommandManager.literal("season").requires((source) -> source.hasPermissionLevel(2))
            .then(CommandManager.literal("set")
                .then(CommandManager.literal("spring")
                    .executes(context -> TimeCommand.executeSet(context.getSource(), 0))
                )
                .then(CommandManager.literal("summer")
                    .executes(context -> TimeCommand.executeSet(context.getSource(), Seasonal.CONFIG.getSeasonLength()*3))
                )
                .then(CommandManager.literal("autumn")
                    .executes(context -> TimeCommand.executeSet(context.getSource(), Seasonal.CONFIG.getSeasonLength()*6))
                )
                .then(CommandManager.literal("winter")
                    .executes(context -> TimeCommand.executeSet(context.getSource(), Seasonal.CONFIG.getSeasonLength()*9))
                )
            )
            .then(CommandManager.literal("query")
                .executes(context -> {
                    World world = context.getSource().getWorld();
                    Season season = Seasonal.getCurrentSeason(world);
                    context.getSource().sendFeedback(Text.translatable("tooltip.seasonal.calendar_info_1").append(Text.translatable("tooltip.seasonal."+season.name().toLowerCase(Locale.ROOT))), false);
                    context.getSource().sendFeedback(Text.literal(Long.toString(((Seasonal.CONFIG.getSeasonLength() - (world.getTimeOfDay() - ((world.getTimeOfDay()/ Seasonal.CONFIG.getSeasonLength())* Seasonal.CONFIG.getSeasonLength()) )) % Seasonal.CONFIG.getSeasonLength())/24000L)).append(Text.translatable("tooltip.seasonal.calendar_info_2").append(Text.translatable("tooltip.seasonal."+season.getNext().name().toLowerCase(Locale.ROOT)))), false);
                    return season.ordinal();
                })
            )
            .then(CommandManager.literal("skip")
                .executes(context -> TimeCommand.executeAdd(context.getSource(), Seasonal.CONFIG.getSeasonLength()))
                .then(CommandManager.literal("spring")
                    .executes(context -> {
                        World world = context.getSource().getWorld();
                        Season season = Seasonal.getCurrentSeason(world);
                        int timeToNextSeason = Math.toIntExact((Seasonal.CONFIG.getSeasonLength() - (world.getTimeOfDay() - ((world.getTimeOfDay()/ Seasonal.CONFIG.getSeasonLength())* Seasonal.CONFIG.getSeasonLength()) )) % Seasonal.CONFIG.getSeasonLength());
                        return switch (season) {
                            case EARLY_SPRING -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 9));
                            case MID_SPRING -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 8));
                            case LATE_SPRING -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 7));
                            case EARLY_SUMMER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 6));
                            case MID_SUMMER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 5));
                            case LATE_SUMMER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 4));
                            case EARLY_AUTUMN -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 3));
                            case MID_AUTUMN -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 2));
                            case LATE_AUTUMN -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + Seasonal.CONFIG.getSeasonLength());
                            case EARLY_WINTER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason);
                            case MID_WINTER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 11));
                            case LATE_WINTER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 10));
                        };
                    })
                )
                .then(CommandManager.literal("summer")
                    .executes(context -> {
                        World world = context.getSource().getWorld();
                        Season season = Seasonal.getCurrentSeason(world);
                        int timeToNextSeason = Math.toIntExact((Seasonal.CONFIG.getSeasonLength() - (world.getTimeOfDay() - ((world.getTimeOfDay()/ Seasonal.CONFIG.getSeasonLength())* Seasonal.CONFIG.getSeasonLength()) )) % Seasonal.CONFIG.getSeasonLength());
                        return switch (season) {
                            case EARLY_SPRING -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason);
                            case MID_SPRING -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 11));
                            case LATE_SPRING -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 10));
                            case EARLY_SUMMER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 9));
                            case MID_SUMMER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 8));
                            case LATE_SUMMER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 7));
                            case EARLY_AUTUMN -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 6));
                            case MID_AUTUMN -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 5));
                            case LATE_AUTUMN -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 4));
                            case EARLY_WINTER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 3));
                            case MID_WINTER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 2));
                            case LATE_WINTER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + Seasonal.CONFIG.getSeasonLength());
                        };
                    })
                )
                .then(CommandManager.literal("autumn")
                    .executes(context -> {
                        World world = context.getSource().getWorld();
                        Season season = Seasonal.getCurrentSeason(world);
                        int timeToNextSeason = Math.toIntExact((Seasonal.CONFIG.getSeasonLength() - (world.getTimeOfDay() - ((world.getTimeOfDay()/ Seasonal.CONFIG.getSeasonLength())* Seasonal.CONFIG.getSeasonLength()) )) % Seasonal.CONFIG.getSeasonLength());
                        return switch (season) {
                            case EARLY_SPRING -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 3));
                            case MID_SPRING -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 2));
                            case LATE_SPRING -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + Seasonal.CONFIG.getSeasonLength());
                            case EARLY_SUMMER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason);
                            case MID_SUMMER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 11));
                            case LATE_SUMMER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 10));
                            case EARLY_AUTUMN -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 9));
                            case MID_AUTUMN -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 8));
                            case LATE_AUTUMN -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 7));
                            case EARLY_WINTER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 6));
                            case MID_WINTER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 5));
                            case LATE_WINTER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 4));
                        };
                    })
                )
                .then(CommandManager.literal("winter")
                    .executes(context -> {
                        World world = context.getSource().getWorld();
                        Season season = Seasonal.getCurrentSeason(world);
                        int timeToNextSeason = Math.toIntExact((Seasonal.CONFIG.getSeasonLength() - (world.getTimeOfDay() - ((world.getTimeOfDay()/ Seasonal.CONFIG.getSeasonLength())* Seasonal.CONFIG.getSeasonLength()) )) % Seasonal.CONFIG.getSeasonLength());
                        return switch (season) {
                            case EARLY_SPRING -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 6));
                            case MID_SPRING -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 5));
                            case LATE_SPRING -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 4));
                            case EARLY_SUMMER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 3));
                            case MID_SUMMER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 2));
                            case LATE_SUMMER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + Seasonal.CONFIG.getSeasonLength());
                            case EARLY_AUTUMN -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason);
                            case MID_AUTUMN -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 11));
                            case LATE_AUTUMN -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 10));
                            case EARLY_WINTER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 9));
                            case MID_WINTER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 8));
                            case LATE_WINTER -> TimeCommand.executeAdd(context.getSource(), timeToNextSeason + (Seasonal.CONFIG.getSeasonLength() * 7));
                        };
                    })
                )
            )
        );
    }

}