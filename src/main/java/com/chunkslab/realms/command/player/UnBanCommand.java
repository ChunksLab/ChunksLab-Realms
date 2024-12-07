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
public class UnBanCommand extends BaseCommand {

    private final RealmsPlugin plugin;

    @SubCommand("unban")
    public void unbanCommand(Player player, String target) {
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
        if (target.equalsIgnoreCase(player.getName())) {
            ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>You cannot unban yourself."));
            return;
        }
        Realm realm = realmPlayer.getRealm();
        if (!realm.getMembersController().isBanned(targetPlayer)) {
            ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>Player not banned"));
            return;
        }
        realm.getMembersController().getBans().remove(realm.getMembersController().getBanned(targetPlayer));
    }
}
