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

    private String dataLoading = "<prefix> <#DC2625>Your data is still loading, please try again.";
    private String noRealm = "<prefix> <#DC2625>You don't have any realm.";
    private String problemNotifyAdmin = "<prefix> <#DC2625>Problem encountered, please notify the administrator!";
    private String playerUnavailable = "<prefix> <#DC2625>Player is not available, please try again.";
    private String alreadyMember = "<prefix> <#DC2625>You are already a member of a realm.";
    private String cannotBanYourself = "<prefix> <#DC2625>You cannot ban yourself.";
    private String playerAlreadyBanned = "<prefix> <#DC2625>Player already banned.";
    private String cannotUnbanYourself = "<prefix> <#DC2625>You cannot unban yourself.";
    private String playerNotBanned = "<prefix> <#DC2625>Player not banned.";
    private String cannotRemoveYourself = "<prefix> <#DC2625>You cannot remove yourself.";
    private String cannotChangeRank = "<prefix> <#DC2625>You cannot change rank yourself.";
    private String targetNotMemberOfRealm = "<prefix> <#DC2625>Target player not a member of your realm.";
    private String cannotInviteYourself = "<prefix> <#DC2625>You cannot invite yourself.";
    private String playerNotOnline = "<prefix> <#DC2625><player> is not online.";
    private String targetAlreadyMember = "<prefix> <#DC2625>Target player already a member of your realm.";
    private String targetAlreadyHasInvite = "<prefix> <#DC2625>Target player already has an invite, please wait for 3 minutes and try again.";
    private String noInvite = "<prefix> <#DC2625>You do not have an invite. -_-";

    private String realmSpawnSet = "<prefix> <#85CC16>You successfully set your realm spawn point!";
    private String teleportToRealm = "<prefix> <#85CC16>Teleporting to your realm...";
    private String realmCreated = "<prefix> <#85CC16>Realm successfully created.";
    private String playerRemoved = "<prefix> <#85CC16>Player successfully removed.";
    private String playerBanned = "<prefix> <#85CC16><player> has been successfully banned.";
    private String playerUnbanned = "<prefix> <#85CC16><player>'s ban has been successfully lifted.";
    private String borderColorChanged = "<prefix> <#85CC16>Border color changed successfully.";
    private String playerInvited = "<prefix> <#85CC16>Player invited successfully.";
    private String inviteAccepted = "<prefix> <#85CC16>Invite accepted. Teleporting...";
    private String inviteRejected = "<prefix> <#85CC16>Invite rejected.";
}
