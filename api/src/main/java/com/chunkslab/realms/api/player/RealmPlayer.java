package com.chunkslab.realms.api.player;

import com.chunkslab.realms.api.realm.Realm;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface RealmPlayer {

    /**
     * Get the unique id of this player.
     * @return unique id
     */
    UUID getUniqueId();

    /**
     * Get the name of the player.
     * @return string name
     */
    String getName();

    /**
     * Get the last login of this player.
     * @return last login milliseconds instant
     */
    long getLastLogout();

    /**
     * Check if this player is online.
     * @return true if online
     */
    boolean isOnline();

    /**
     * Get the realm that this player is in.
     * @return realm
     */
    @Nullable Realm getRealm();

    /**
     * Get the player instance if online.
     * @return player instance
     */
    @Nullable
    default Player getPlayer() {
        return Bukkit.getPlayer(getUniqueId());
    }

    /**
     * Get the offline player instance.
     * @return offline player instance
     */
    @NotNull
    default OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getUniqueId());
    }

    /**
     * Set the name of the player.
     * @param name string name
     */
    void setName(String name);

    /**
     * Set the last login of this player.
     * @param lastLogout last login milliseconds instant
     */
    void setLastLogout(long lastLogout);

    /**
     * Set if this player is online.
     * @param online true if online
     */
    void setOnline(boolean online);

    /**
     * Set the realm that this player is in.
     * @param realm realm
     */
    void setRealm(@Nullable Realm realm);
}