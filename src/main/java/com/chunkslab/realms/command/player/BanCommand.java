package com.chunkslab.realms.command.player;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.ban.DefaultBannedPlayer;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.util.ChatUtils;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

@Command(value = "realms", alias = {"realm"})
@RequiredArgsConstructor
public class BanCommand extends BaseCommand {

    private final RealmsPlugin plugin;
    private final Permission permission;

    @SubCommand("ban")
    public void banCommand(Player player, @Suggestion("players") String target) {
        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player);
        RealmPlayer targetPlayer = plugin.getPlayerManager().getPlayer(target);
        if (realmPlayer == null) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getDataLoading()));
            return;
        }
        if (targetPlayer == null) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getPlayerUnavailable()));
            return;
        }
        if (realmPlayer.getRealmId() == null) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getNoRealm()));
            return;
        }
        if (!realmPlayer.hasPermission(permission)) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getNotEnoughPermission()));
            return;
        }
        if (target.equalsIgnoreCase(player.getName())) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getCannotBanYourself()));
            return;
        }
        Realm realm = realmPlayer.getRealm();
        if (realm.getMembersController().isBanned(targetPlayer)) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getPlayerAlreadyBanned()));
            return;
        }
        if (realm.getMembersController().isMember(targetPlayer))
            realm.getMembersController().kick(targetPlayer, player.getName());
        realm.getMembersController().getBans().add(new DefaultBannedPlayer(targetPlayer.getContext(), System.currentTimeMillis()));
        ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getPlayerBanned(), Placeholder.unparsed("player", target)));
    }
}
