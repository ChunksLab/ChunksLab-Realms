package com.chunkslab.realms.command.player;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.util.LogUtils;
import com.chunkslab.realms.gui.InviteGui;
import com.chunkslab.realms.util.ChatUtils;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.Collection;

@Command(value = "realms", alias = {"realm"})
@RequiredArgsConstructor
public class InviteCommand extends BaseCommand {

    private final RealmsPlugin plugin;

    @SubCommand("invite")
    public void inviteCommand(Player player, @Suggestion("players") String target) {
        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player);
        if (realmPlayer == null) {
            ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>Your data is still loading, please try again."));
            return;
        }
        if (realmPlayer.getRealmId() == null) {
            ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>You don't have any realm."));
            return;
        }
        
        // TODO: Role permission check

        if (target.equalsIgnoreCase(player.getName())) {
            ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>You cannot invite yourself."));
            return;
        }
        
        plugin.getScheduler().runTaskAsync(() -> {
            Collection<String> players = plugin.getServerManager().getAllOnlinePlayers();
            if (!players.contains(target)) {
                ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625><player> is not online.", Placeholder.unparsed("player", target)));
                return;
            }

            RealmPlayer targetPlayer = plugin.getDatabase().loadPlayer(target);
            if (targetPlayer.getRealmId().equals(realmPlayer.getRealmId())) {
                ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>Target player already member of your realm."));
                return;
            }

            plugin.getInviteManager().getInvite(targetPlayer.getUniqueId()).whenComplete((invite, ex) -> {
                if (ex != null) {
                    LogUtils.warn("An exception was found!", ex);
                    return;
                }

                if (invite != null) {
                    ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>Target player already has an invite, please wait for 3 minutes and try again."));
                    return;
                }

                InviteGui.open(realmPlayer, targetPlayer, plugin);
            });
        });
    }
}
