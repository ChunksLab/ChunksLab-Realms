package com.chunkslab.realms.api.database;

import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.realm.Realm;

import java.util.UUID;

public interface Database {

    void enable();

    void disable();

    Realm loadRealm(UUID realmUUID);

    default Realm loadRealm(RealmPlayer player) { return loadRealm(player.getRealm().getUniqueId()); }

    RealmPlayer loadPlayer(UUID playerUUID);

    RealmPlayer loadPlayer(String name);

    void saveRealm(Realm realm);

    void savePlayer(RealmPlayer player);

    void deleteRealm(UUID realmUUID);

    default void deleteRealm(Realm realm) { deleteRealm(realm.getUniqueId()); }
}