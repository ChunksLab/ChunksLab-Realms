package com.chunkslab.realms.api.realm;

import com.chunkslab.realms.api.biome.Biome;
import com.chunkslab.realms.api.location.ServerLocation;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import org.bukkit.Location;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IRealmManager {

    /**
     * Get all realms that are currently loaded.
     * @return collection of realms
     */
    Collection<Realm> getRealms();

    /**
     * Get a realm by its unique id.
     * @param uniqueId unique id
     * @return realm or null if not found
     */
    Realm getRealm(UUID uniqueId);

    CompletableFuture<Boolean> createRealm(Biome biome, RealmPlayer realmPlayer);

    CompletableFuture<Boolean> loadRealm(Realm realm);

    void unloadRealm(Realm realm);

    Realm getRealm(Location location);
}