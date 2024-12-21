package com.chunkslab.realms.listener;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.util.WorldBorderUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

@RequiredArgsConstructor
public class PlayerRespawnListener implements Listener {

    private final RealmsPlugin plugin;

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Realm realm = plugin.getRealmManager().getRealm(event.getPlayer().getLocation());
        if (realm != null) {
            WorldBorderUtils.send(plugin.getPlayerManager().getPlayer(player), realm);
        } else {
            WorldBorderUtils.reset(player);
        }
    }

}
