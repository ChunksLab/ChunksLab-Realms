package com.chunkslab.realms.command.player;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
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

    @SubCommand("create")
    public void createCommand(Player player) {
        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player);
        if (realmPlayer == null) {
            ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>Your data is still loading, please try again."));
            return;
        }
        if (realmPlayer.getRealmId() != null) {
            ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>You are already a member of a realm"));
            return;
        }

        plugin.getRealmManager().createRealm(plugin.getBiomeManager().getDefaultBiome(), realmPlayer).thenAccept(result -> {
           if (!result) {
               ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>Problem encountered please notify the administrator"));
               return;
           }

           plugin.getScheduler().runTaskSync(() -> player.teleportAsync(realmPlayer.getRealm().getSpawnLocation().getLocation()), player.getLocation());
        });
    }
}
