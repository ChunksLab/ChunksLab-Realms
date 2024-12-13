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
public class DenyCommand extends BaseCommand {

    private final RealmsPlugin plugin;
    private final Permission permission;

    @SubCommand("deny")
    public void denyCommand(Player player) {
        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player);
        if (realmPlayer == null) {
            ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>Your data is still loading, please try again."));
            return;
        }
        if (!realmPlayer.hasPermission(permission)) {
            ChatUtils.sendMessage(player, ChatUtils.format("<red>You dont have required permission."));
            return;
        }
        plugin.getInviteManager().getInvite(player.getUniqueId()).whenComplete((realmId, ex) -> {
            if (realmId == null) {
                ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>You do not have an invite. -_-"));
                return;
            }

            ChatUtils.sendMessage(player, ChatUtils.format("<#85CC16>Invite rejected."));
            plugin.getInviteManager().rejectInvite(realmPlayer);
        });
    }
}
