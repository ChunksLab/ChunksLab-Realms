package com.chunkslab.realms.command.player;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.location.ServerLocation;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.util.ChatUtils;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(value = "realms", alias = {"realm"})
@RequiredArgsConstructor
public class SetSpawnCommand extends BaseCommand {

    private final RealmsPlugin plugin;
    private final Permission permission;

    @SubCommand("setspawn")
    public void setSpawnCommand(Player player) {
        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player);
        if (realmPlayer == null) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getDataLoading()));
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
        if (plugin.getRealmManager().getRealm(player.getPlayer().getLocation()) == null) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getSetSpawnOnlyInRealm()));
            return;
        }
        realmPlayer.getRealm().setSpawnLocation(ServerLocation.Builder.create(player.getLocation()).build());
        ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getRealmSpawnSet()));
    }
}
