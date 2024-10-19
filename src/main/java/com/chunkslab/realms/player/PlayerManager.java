package com.chunkslab.realms.player;

import com.chunkslab.realms.api.player.IPlayerManager;
import com.chunkslab.realms.api.player.RealmPlayer;
import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager implements IPlayerManager {

    private final Map<UUID, RealmPlayer> playerMap = new ConcurrentHashMap<>();

    @Override
    public Collection<RealmPlayer> getPlayers() {
        return playerMap.values();
    }

    @Override
    public void addPlayer(RealmPlayer player) {
        playerMap.put(player.getUniqueId(), player);
    }

    @Override
    public void removePlayer(UUID uuid) {
        playerMap.remove(uuid);
    }

    @Override
    public void removePlayer(RealmPlayer player) {
        removePlayer(player.getUniqueId());
    }

    @Override
    public void removePlayer(OfflinePlayer offlinePlayer) {
        removePlayer(offlinePlayer.getUniqueId());
    }

    @Override
    public RealmPlayer getPlayer(UUID uuid) {
        return playerMap.get(uuid);
    }

    @Override
    public RealmPlayer getPlayer(OfflinePlayer offlinePlayer) {
        return getPlayer(offlinePlayer.getUniqueId());
    }

    @Override
    public RealmPlayer getPlayer(String name) {
        return this.playerMap.values().stream().filter(player -> player.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}