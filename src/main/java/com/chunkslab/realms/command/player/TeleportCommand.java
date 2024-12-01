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
public class TeleportCommand extends BaseCommand {

    private final RealmsPlugin plugin;

    @SubCommand("tp")
    public void teleportCommand(Player player) {
        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player);
        if (realmPlayer == null) {
            ChatUtils.sendMessage(player, ChatUtils.format("<red>Your data is still loading, please try again."));
            return;
        }
        ChatUtils.sendMessage(player, ChatUtils.format("<#85CC16>Teleporting to your realm..."));
        player.teleportAsync(realmPlayer.getRealm().getSpawnLocation().getLocation());
    }
}
