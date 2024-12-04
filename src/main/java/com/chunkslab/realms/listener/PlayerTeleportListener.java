package com.chunkslab.realms.listener;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.api.util.LogUtils;
import com.chunkslab.realms.util.ChatUtils;
import com.chunkslab.realms.util.WorldBorderUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

@RequiredArgsConstructor
public class PlayerTeleportListener implements Listener {

    private final RealmsPlugin plugin;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMove(PlayerTeleportEvent event) {
        if (!event.hasChangedBlock())
            return;

        Player player = event.getPlayer();

        Realm from = plugin.getRealmManager().getRealm(event.getFrom());
        Realm to = plugin.getRealmManager().getRealm(event.getTo());
        if (from == to) return;

        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player);
        if (realmPlayer == null) return;

        if (from != null) {
            if (from.getMembersController().isVisitor(realmPlayer))
                from.getMembersController().getVisitors().remove(realmPlayer);
            WorldBorderUtils.reset(player);
        }

        if (to != null) {
            if (to.getMembersController().isBanned(realmPlayer)) {
                ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>The teleport was canceled because you were banned from the realms you tried to teleport to."));
                event.setCancelled(true);
            }

            if (!to.getMembersController().isMember(realmPlayer))
                to.getMembersController().getVisitors().add(realmPlayer);
            LogUtils.debug("Sending Border...");
            plugin.getScheduler().runTaskSyncLater(() -> WorldBorderUtils.send(player, to), to.getCenterLocation().getLocation(), 5);

            if (player.isFlying() && !player.hasPermission("chunkslab.realms.permission.bypass.fly")) {
                player.setFlying(false);
                player.setAllowFlight(false);
                ChatUtils.sendMessage(player, ChatUtils.format("<#DC2625>You do not have permission to fly in this realm."));
            }
        }
    }
}