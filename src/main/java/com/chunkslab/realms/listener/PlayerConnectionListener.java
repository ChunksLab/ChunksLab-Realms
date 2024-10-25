package com.chunkslab.realms.listener;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerConnectionListener implements Listener {

    private final RealmsPlugin plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getScheduler().runTaskAsync(() -> plugin.getDatabase().loadPlayer(player.getUniqueId()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        plugin.getScheduler().runTaskAsync(() -> {
            if (realmPlayer != null)
                plugin.getDatabase().savePlayer(realmPlayer);
        });
        plugin.getPlayerManager().removePlayer(player.getUniqueId());
    }
}
