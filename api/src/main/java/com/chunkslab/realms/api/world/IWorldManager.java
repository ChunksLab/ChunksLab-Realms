package com.chunkslab.realms.api.world;

import com.chunkslab.realms.api.realm.Realm;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public interface IWorldManager {

    String getName();

    void loadWorlds();

    Location getNextLocation(boolean subtract);

    BlockFace getRealmFace(Location location);

    Location getLastLocation();

    void setLastLocation(Location location);

    Location getSafeSpawnLocation();

    void setSafeSpawnLocation(Location location);

    String getWorldPrefix(Realm realm);

    String getNormalWorldName(Realm realm);

    String getNetherWorldName(Realm realm);

    String getEndWorldName(Realm realms);

}