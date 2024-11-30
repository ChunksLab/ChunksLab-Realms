package com.chunkslab.realms.config;

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
        @Comment("Default realm member amount")
        private int defaultRealmMemberAmount = 8;
        @Comment("Blocked keywords for name and description")
        private List<String> blockedKeywords = List.of("admin", "mod", "owner", "chunkslab", "fuck");
        @Comment("Blocks forbidden to set realm spawn location")
        private List<String> forbiddenBlocks = List.of("WATER", "LAVA");
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