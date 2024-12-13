package com.chunkslab.realms.command.player;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.gui.MembersGui;
import com.chunkslab.realms.gui.RankGui;
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
public class MemberCommand extends BaseCommand {

    private final RealmsPlugin plugin;
    private final Permission permission;

    @SubCommand("member")
    public void memberCommand(Player player, @Suggestion("players") String target) {
        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player);
        if (realmPlayer == null) {
            ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>Your data is still loading, please try again."));
            return;
        }
        if (realmPlayer.getRealmId() == null) {
            ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>You don't have any realm."));
            return;
        }
        if (!realmPlayer.hasPermission(permission)) {
            ChatUtils.sendMessage(player, ChatUtils.format("<red>You dont have required permission."));
            return;
        }
        if (target.equalsIgnoreCase(player.getName())) {
            ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>You cannot change rank yourself."));
            return;
        }
        plugin.getScheduler().runTaskAsync(() -> {
            Collection<String> players = plugin.getServerManager().getAllOnlinePlayers();
            if (!players.contains(target)) {
                ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625><player> is not online.", Placeholder.unparsed("player", target)));
                return;
            }
            RealmPlayer targetPlayer = plugin.getDatabase().loadPlayer(target);
            if (!targetPlayer.getRealmId().equals(realmPlayer.getRealmId())) {
                ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>Target player not member of your realm."));
                return;
            }

            Realm realm = realmPlayer.getRealm();
            RankGui.open(realmPlayer, realm.getMembersController().getMember(targetPlayer), realm, plugin);
        });
        Realm realm = realmPlayer.getRealm();
        MembersGui.open(realmPlayer, realm, plugin);
    }
}
