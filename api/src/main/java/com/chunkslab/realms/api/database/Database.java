package com.chunkslab.realms.api.database;

import com.chunkslab.realms.api.player.RealmPlayer;
import com.chunkslab.realms.api.realm.Realm;

import java.util.UUID;

public interface Database {

    void enable();

    void disable();

    Realm loadRealm(UUID realmUUID);

    default Realm loadRealm(RealmPlayer player) { return loadRealm(player.getRealm().getUniqueId()); }

    RealmPlayer loadPlayer(UUID playerUUID, boolean loadAnyway);

    RealmPlayer loadPlayer(String name, boolean loadAnyway);

    default RealmPlayer loadPlayer(UUID playerUUID) { return loadPlayer(playerUUID, false); }

    default RealmPlayer loadPlayer(String name) { return loadPlayer(name, false); }

    void saveRealm(Realm realm);

    void savePlayer(RealmPlayer player);

    void deleteRealm(UUID realmUUID);

    default void deleteRealm(Realm realm) { deleteRealm(realm.getUniqueId()); }
}