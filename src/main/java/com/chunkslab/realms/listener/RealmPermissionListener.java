package com.chunkslab.realms.listener;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.util.ChatUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public abstract class RealmPermissionListener implements Listener {
    private final Permission permission;

    protected void checkAndCancel(Cancellable event, Player player) {
        RealmPlayer realmPlayer = RealmsPlugin.getInstance().getPlayerManager().getPlayer(player);
        checkAndCancel(event, realmPlayer);
    }

    protected void checkAndCancel(Cancellable event, RealmPlayer player) {
        Player bukkitPlayer = player.getBukkitPlayer();
        Realm realm = RealmsPlugin.getInstance().getRealmManager().getRealm(bukkitPlayer.getLocation());
        if (realm == null) return;

        if (!player.hasPermission(permission)) {
            event.setCancelled(true);
            ChatUtils.sendMessage(bukkitPlayer, ChatUtils.format(RealmsPlugin.getInstance().getPluginMessages().getActionCancelled()));
            bukkitPlayer.playSound(player.getBukkitPlayer(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
    }

    protected boolean check(Player player) {
        RealmPlayer realmPlayer = RealmsPlugin.getInstance().getPlayerManager().getPlayer(player);
        return check(realmPlayer);
    }

    protected boolean check(RealmPlayer player) {
        Player bukkitPlayer = player.getBukkitPlayer();
        Realm realm = RealmsPlugin.getInstance().getRealmManager().getRealm(bukkitPlayer.getLocation());
        if (realm == null) return false;

        if (!player.hasPermission(permission)) {
            ChatUtils.sendMessage(bukkitPlayer, ChatUtils.format(RealmsPlugin.getInstance().getPluginMessages().getActionCancelled()));
            bukkitPlayer.playSound(player.getBukkitPlayer(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return true;
        }
        return false;
    }
}
