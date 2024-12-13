package com.chunkslab.realms.api.player.objects;

import com.chunkslab.realms.api.location.ServerLocation;
import com.chunkslab.realms.api.player.contexts.RealmPlayerContext;
import com.chunkslab.realms.api.player.data.RealmPlayerData;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.api.realm.Realm;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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

    void teleport(ServerLocation location);

    List<Permission> getPermissions();

    boolean hasPermission(Permission permission);

    boolean isOnline();

    default Player getBukkitPlayer() {
        return Bukkit.getPlayer(getName());
    }

    default OfflinePlayer getBukkitOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getUniqueId());
    }

}
