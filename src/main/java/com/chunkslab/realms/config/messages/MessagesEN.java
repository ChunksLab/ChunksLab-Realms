package com.chunkslab.realms.config.messages;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import lombok.Getter;
import lombok.Setter;

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
public class MessagesEN extends OkaeriConfig {

    private String prefix = "<#D0EFB1>Realms <dark_gray>•";

    private String invalidArgument = "<prefix> <#DC2625>Invalid argument.";

    private String tooManyArguments = "<prefix> <#DC2625>Too many arguments.";

    private String notEnoughArguments = "<prefix> <#DC2625>Not enough arguments.";

    private String unknownCommand = "<prefix> <#DC2625>Unknown command.";

    private String notEnoughPermission = "<prefix> <#DC2625>You don't have enough permission to do this.";
}