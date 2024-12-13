package com.chunkslab.realms.command.player;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.Permission;
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
    private final Permission permission;

    @SubCommand("accept")
    public void acceptCommand(Player player) {
        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player);
        if (realmPlayer == null) {
            ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>Your data is still loading, please try again."));
            return;
        }
        if (!realmPlayer.hasPermission(permission)) {
            ChatUtils.sendMessage(player, ChatUtils.format("<red>You dont have required permission."));
            return;
        }
        plugin.getInviteManager().getInvite(player.getUniqueId()).whenComplete((invite, ex) -> {
            if (ex != null) {
                LogUtils.warn("An exception was found on redis!", ex);
                return;
            }

            if (invite == null) {
                ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>You do not have an invite. -_-"));
                return;
            }

            ChatUtils.sendMessage(player, ChatUtils.format("<#85CC16>Invite accepted. Teleporting..."));
            plugin.getInviteManager().acceptInvite(realmPlayer, invite);
        });
    }
}
