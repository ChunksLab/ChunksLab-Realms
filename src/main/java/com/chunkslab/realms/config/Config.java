package com.chunkslab.realms.config;

import com.chunkslab.realms.api.player.BorderColor;
import com.chunkslab.realms.api.player.MessagePreference;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Header("################################################################")
@Header("#                                                              #")
@Header("#                       ChunksLab Realms                       #")
@Header("#                                                              #")
@Header("#     Need help configuring the plugin? Join our discord!      #")
@Header("#                https://discord.chunkslab.com                 #")
@Header("#                                                              #")
@Header("################################################################")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
@Getter @Setter
public class Config extends OkaeriConfig {

    @Comment("Currently supports: EN, DE, TR")
    private Language language = Language.EN;

    @Comment("Plugin settings")
    private Settings settings = new Settings();

    @Comment("Schematic paster settings")
    private Paster paster = new Paster();

    @Comment("These settings are used to configure the thread pool for the plugin")
    @Comment("Please be careful when changing these values")
    @Comment("If you are unsure, please leave them as they are")
    private ThreadSettings threadSettings = new ThreadSettings();

    @Comment("Number formatting settings")
    private NumberSettings numberSettings = new NumberSettings();

    @RequiredArgsConstructor
    @Getter
    public static enum Language {
        EN("messages_en.yml"),
        DE("messages_de.yml"),
        TR("messages_tr.yml"),
        ;

        private final String fileName;

    }

    @Getter @Setter
    public static class Settings extends OkaeriConfig {
        @Comment("World prefix for realms world")
        @Comment("Do not use _ its automatically added")
        private String worldPrefix = "Realms";
        @Comment("Maximum length of a realm name")
        private int maxRealmNameLength = 16;
        @Comment("Maximum length of a realm description")
        private int maxRealmDescriptionLength = 64;
        @Comment("Default realm name")
        private String defaultName = "<player>'s Realm";
        @Comment("Default realm description")
        private String defaultDescription = "A realm created by <player>";
        @Comment("Change players default preference on how messages are received")
        @Comment("CHAT, TITLE, SUBTITLE, ACTION_BAR, BOSS_BAR")
        private MessagePreference defaultMessagePreference = MessagePreference.CHAT;
        @Comment("Change players default preference on realm border color")
        @Comment("BLUE, GREEN, RED")
        private BorderColor defaultBorderColor = BorderColor.BLUE;
        @Comment("Blocked keywords for name and description")
        private List<String> blockedKeywords = List.of("admin", "mod", "owner", "chunkslab", "fuck");
        @Comment("Blocks forbidden to set realm spawn location")
        private List<String> forbiddenBlocks = List.of("WATER", "LAVA");
        @Comment("Items that can act as switches")
        private List<String> switchItems = List.of(
                "CHEST",
                "SHULKER_BOXES",
                "TRAPPED_CHEST",
                "FURNACE",
                "BLAST_FURNACE",
                "DISPENSER",
                "HOPPER",
                "DROPPER",
                "JUKEBOX",
                "SMOKER",
                "COMPOSTER",
                "BELL",
                "BARREL",
                "BREWING_STAND",
                "LEVER",
                "NON_WOODEN_PRESSURE_PLATES",
                "BUTTONS",
                "WOOD_DOORS",
                "FENCE_GATES",
                "TRAPDOORS",
                "MINECARTS",
                "LODESTONE",
                "RESPAWN_ANCHOR",
                "TARGET",
                "OAK_CHEST_BOAT",
                "DECORATED_POT",
                "CRAFTER"
        );
        @Comment("Items allowed for specific use cases")
        private List<String> useItems = List.of(
                "MINECARTS",
                "BOATS",
                "ENDER_PEARL",
                "FIREBALL",
                "CHORUS_FRUIT",
                "LEAD",
                "EGG"
        );
    }

    @Getter @Setter
    public static class Paster extends OkaeriConfig {
        @Comment("Schematic paster plugin name")
        private String name = "WorldEdit";
        @Comment("Distance range for other realms")
        private int distanceRange = 5000;
    }

    @Getter @Setter
    public static class ThreadSettings extends OkaeriConfig {
        private int maximumPoolSize = 10;
        private int corePoolSize = 10;
        private int keepAliveTime = 30;
    }

    @Getter @Setter
    public static class NumberSettings extends OkaeriConfig {
        private String locale = "en-US";
        private String style = "SHORT";
    }
}