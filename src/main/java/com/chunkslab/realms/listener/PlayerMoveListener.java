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
import org.bukkit.event.player.PlayerMoveEvent;

@RequiredArgsConstructor
public class PlayerMoveListener implements Listener {

    private final RealmsPlugin plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMove(PlayerMoveEvent event) {
        if (!event.hasChangedBlock())
            return;

        Player player = event.getPlayer();

        if (!player.getWorld().getName().startsWith(plugin.getWorldManager().getWorldPrefix(null)))
            return;

        Realm from = plugin.getRealmManager().getRealm(event.getFrom());
        Realm to = plugin.getRealmManager().getRealm(event.getTo());
        if (from == to) return;

        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player);
        if (realmPlayer == null) return;

        if (from != null) {
            // TODO: Visitor removing here
            WorldBorderUtils.reset(player);
        }

        if (to != null) {
            // TODO: if player has banned do event cancel

            // TODO: Visit add here
            LogUtils.debug("Sending Border...");
            plugin.getScheduler().runTaskSyncLater(() -> WorldBorderUtils.send(player, to), to.getCenterLocation().getLocation(), 5);

            if (player.isFlying() && !player.hasPermission("chunkslab.realms.permission.bypass.fly")) {
                player.setFlying(false);
                player.setAllowFlight(false);
                player.sendMessage(ChatUtils.format("<#DC2625>You do not have permission to fly in this realm."));
            }
        }
    }
}