package com.chunkslab.realms.command.player;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.util.ChatUtils;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Optional;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.Collection;

@Command(value = "realms", alias = {"realm"})
@RequiredArgsConstructor
public class TeleportCommand extends BaseCommand {

    private final RealmsPlugin plugin;
    private final Permission permission;

    @SubCommand("tp")
    public void teleportCommand(Player player, @Optional @Suggestion("players") String target) {
        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player);
        if (realmPlayer == null) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getDataLoading()));
            return;
        }
        if (!realmPlayer.hasPermission(permission)) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getNotEnoughPermission()));
            return;
        }
        if (target == null) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getTeleportToRealm()));
            player.teleportAsync(realmPlayer.getRealm().getSpawnLocation().getLocation());
        } else {
            Collection<String> players = plugin.getServerManager().getAllOnlinePlayers();
            if (!players.contains(target)) {
                ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getPlayerNotOnline(), Placeholder.unparsed("player", target)));
                return;
            }

            RealmPlayer targetPlayer = plugin.getDatabase().loadPlayer(target);
            if (targetPlayer.getRealmId() == null) {
                ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getNoRealm()));
                return;
            }

            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getTeleportToRealm()));
            player.teleportAsync(targetPlayer.getRealm().getSpawnLocation().getLocation());
        }
    }
}
