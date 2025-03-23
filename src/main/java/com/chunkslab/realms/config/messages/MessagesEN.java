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

    private String dataLoading = "<prefix> <#DC2625>Your data is still loading. Please try again shortly.";
    private String noRealm = "<prefix> <#DC2625>You don’t own a realm yet.";
    private String problemNotifyAdmin = "<prefix> <#DC2625>An error occurred. Please contact an admin!";
    private String playerUnavailable = "<prefix> <#DC2625>That player is currently unavailable. Try again later.";
    private String alreadyMember = "<prefix> <#DC2625>You’re already in a realm!";
    private String cannotBanYourself = "<prefix> <#DC2625>You cannot ban yourself.";
    private String playerAlreadyBanned = "<prefix> <#DC2625>This player is already banned.";
    private String cannotUnbanYourself = "<prefix> <#DC2625>You cannot unban yourself.";
    private String playerNotBanned = "<prefix> <#DC2625>This player is not banned.";
    private String cannotRemoveYourself = "<prefix> <#DC2625>You can’t remove yourself from the realm.";
    private String cannotChangeRank = "<prefix> <#DC2625>You can’t change your own rank.";
    private String targetNotMemberOfRealm = "<prefix> <#DC2625>This player is not a member of your realm.";
    private String cannotInviteYourself = "<prefix> <#DC2625>You cannot invite yourself.";
    private String playerNotOnline = "<prefix> <#DC2625><player> is currently not online.";
    private String targetAlreadyMember = "<prefix> <#DC2625>This player is already in your realm.";
    private String targetAlreadyHasInvite = "<prefix> <#DC2625>This player already has a pending invite. Please wait 3 minutes and try again.";
    private String noInvite = "<prefix> <#DC2625>You don’t have any pending invites.";
    private String onlyRealm = "<prefix> <#DC2625>This command can only be used inside your realm.";
    private String setSpawnOnlyInRealm = "<prefix> <#DC2625>You can only set your spawn while inside your own realm.";
    private String actionCancelled = "<prefix> <#DC2625>You’re not allowed to perform this action in this realm.";
    private String teleportCancelled = "<prefix> <#DC2625>Your teleportation was canceled due to the realm's teleport settings.";
    private String noFlyPermission = "<prefix> <#DC2625>You don’t have permission to fly in this realm.";
    private String biomeNotFound = "<prefix> <#DC2625>No biome could be located.";

    private String realmSpawnSet = "<prefix> <#85CC16>Your realm spawn point has been set successfully!";
    private String teleportToRealm = "<prefix> <#85CC16>Teleporting to your realm...";
    private String realmCreating = "<prefix> <#85CC16>Your realm has been started to creating...";
    private String realmCreated = "<prefix> <#85CC16>Your realm has been created successfully.";
    private String realmDeleted = "<prefix> <#DC2625>Your realm has been deleted.";
    private String realmDeletedVisitor = "<prefix> <#DC2625>The realm you are on has been deleted. Teleporting to the spawn...";
    private String playerRemoved = "<prefix> <#85CC16>Player has been removed from your realm.";
    private String playerBanned = "<prefix> <#85CC16><player> has been banned from your realm.";
    private String playerUnbanned = "<prefix> <#85CC16><player>'s ban has been lifted.";
    private String roleModified = "<prefix> <#85CC16>Member's role has been updated!";
    private String borderColorChanged = "<prefix> <#85CC16>Realm border color updated.";
    private String playerInvited = "<prefix> <#85CC16>Player has been invited to your realm.";
    private String inviteReceived = "<yellow>You’ve received a realm invite from <player>.";
    private String inviteAccept = "<#85CC16>ACCEPT";
    private String inviteReject = "<#DC2625>DENY";
    private String inviteAccepted = "<prefix> <#85CC16>Invite accepted. Teleporting you to the realm...";
    private String inviteRejected = "<prefix> <#85CC16>You have declined the invite.";
    private String joinRealm = "<prefix> <#85CC16>Everyone welcome <yellow><name><#85CC16> to the realm!";
    private String kickRealm = "<prefix> <yellow><name> <#DC2625>was kicked from the realm by <yellow><kicker>.";
}
