package com.chunkslab.realms.command.player;

import com.chunkslab.realms.RealmsPlugin;
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
public class CreateCommand extends BaseCommand {

    private final RealmsPlugin plugin;
    private final Permission permission;

    @SubCommand("create")
    public void createCommand(Player player) {
        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player);
        if (realmPlayer == null) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getDataLoading()));
            return;
        }
        if (realmPlayer.getRealmId() != null) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getAlreadyMember()));
            return;
        }
        if (!realmPlayer.hasPermission(permission)) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getNotEnoughPermission()));
            return;
        }
        plugin.getRealmManager().createRealm(plugin.getBiomeManager().getDefaultBiome(), realmPlayer).thenAccept(result -> {
           if (!result) {
               ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getProblemNotifyAdmin()));
               return;
           }

           plugin.getScheduler().runTaskSync(() -> player.teleportAsync(realmPlayer.getRealm().getSpawnLocation().getLocation()), player.getLocation());
           ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getRealmCreated()));
        });
    }
}
