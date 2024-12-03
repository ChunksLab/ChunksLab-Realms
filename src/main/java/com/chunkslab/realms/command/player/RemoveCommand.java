package com.chunkslab.realms.command.player;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.util.ChatUtils;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(value = "realms", alias = {"realm"})
@RequiredArgsConstructor
public class RemoveCommand extends BaseCommand {

    private final RealmsPlugin plugin;

    @SubCommand("remove")
    public void removeCommand(Player player, Player target) {
        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player);
        RealmPlayer targetPlayer = plugin.getPlayerManager().getPlayer(target);
        if (realmPlayer == null) {
            ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>Your data is still loading, please try again."));
            return;
        }
        if (targetPlayer == null) {
            ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>Player is not available, please try again."));
            return;
        }
        if (realmPlayer.getRealmId() == null) {
            ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>You don't have any realm."));
            return;
        }
        Realm realm = realmPlayer.getRealm();
        if (!realm.getMembersController().isMember(targetPlayer)) {
            ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>Target not member of your realm..."));
            return;
        }
        realm.getMembersController().kick(targetPlayer, realmPlayer.getName());
    }
}
