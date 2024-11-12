package com.chunkslab.realms.api.player.objects;

import com.chunkslab.realms.api.player.contexts.RealmPlayerContext;
import com.chunkslab.realms.api.player.data.RealmPlayerData;
import com.chunkslab.realms.api.realm.Realm;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface RealmPlayer {

    /**
     * Get player context (name, uuid...)
     * @return player context
     */
    RealmPlayerContext getContext();

    @Nullable UUID getRealmId();

    @Nullable Realm getRealm();

    RealmPlayerData getData();

    void setRealmId(UUID realmId);

    String getName();

    UUID getUniqueId();

    boolean isLocalPlayer();

    default Player getBukkitPlayer() {
        return Bukkit.getPlayer(getName());
    }

    default OfflinePlayer getBukkitOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getUniqueId());
    }

}
