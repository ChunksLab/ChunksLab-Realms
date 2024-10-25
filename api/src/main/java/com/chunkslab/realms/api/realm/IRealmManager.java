package com.chunkslab.realms.api.realm;

import com.chunkslab.realms.api.location.ServerLocation;

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

    /**
     * Get a realm by its location.
     * @param location location
     * @return realm or null if not found
     */
    Realm getRealm(ServerLocation location);

    /**
     * Create a new realm.
     * @param realm realm to create
     * @return true if the realm has been created
     */
    CompletableFuture<Boolean> createRealm(Realm realm);

    
}