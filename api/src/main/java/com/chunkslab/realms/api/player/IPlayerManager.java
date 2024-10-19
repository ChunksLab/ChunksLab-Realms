package com.chunkslab.realms.api.player;

import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.UUID;

public interface IPlayerManager {

    Collection<RealmPlayer> getPlayers();

    void addPlayer(RealmPlayer player);

    void removePlayer(UUID uuid);

    void removePlayer(RealmPlayer player);

    void removePlayer(OfflinePlayer offlinePlayer);

    RealmPlayer getPlayer(UUID uuid);

    RealmPlayer getPlayer(OfflinePlayer offlinePlayer);

    RealmPlayer getPlayer(String name);
}