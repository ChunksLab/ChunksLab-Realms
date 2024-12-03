package com.chunkslab.realms.command.player;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.util.LogUtils;
import com.chunkslab.realms.util.ChatUtils;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(value = "realms", alias = {"realm"})
@RequiredArgsConstructor
public class AcceptCommand extends BaseCommand {

    private final RealmsPlugin plugin;

    @SubCommand("accept")
    public void acceptCommand(Player player) {
        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player);
        if (realmPlayer == null) {
            player.sendMessage(ChatUtils.format("<#DC2625>Your data is still loading, please try again."));
            return;
        }

        plugin.getInviteManager().getInvite(player.getUniqueId()).whenComplete((realmId, ex) -> {
            if (ex != null) {
                LogUtils.warn("An exception was found on redis!", ex);
                return;
            }

            if (realmId == null) {
                player.sendMessage(ChatUtils.format("<#DC2625>You do not have an invite. -_-"));
                return;
            }

            player.sendMessage(ChatUtils.format("<#85CC16>Invite accepted. Teleporting..."));
            plugin.getInviteManager().acceptInvite(realmPlayer, realmId);
        });
    }
}
