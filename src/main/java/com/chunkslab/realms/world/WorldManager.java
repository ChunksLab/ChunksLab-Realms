package com.chunkslab.realms.world;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.api.util.LogUtils;
import com.chunkslab.realms.api.world.IWorldManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.BlockFace;

@RequiredArgsConstructor
public class WorldManager implements IWorldManager {

    private final RealmsPlugin plugin;

    @Getter @Setter Location lastLocation;

    @Getter @Setter Location safeSpawnLocation;

    @Override
    public String getName() {
        return "Default";
    }

    @Override
    public void loadWorlds() {
        World normalWorld = Bukkit.getWorld(getNormalWorldName(null));
        World netherWorld = Bukkit.getWorld(getNetherWorldName(null));
        World endWorld = Bukkit.getWorld(getEndWorldName(null));

        if (normalWorld == null) {
            LogUtils.info("Creating normal realm world...");
            normalWorld = WorldCreator.name(getNormalWorldName(null)).type(WorldType.FLAT)
                    .environment(World.Environment.NORMAL).generator(new VoidGenerator()).createWorld();
            registerToMultiverse(normalWorld);
            LogUtils.info("Successfully created normal realm world...");
        }

        if (netherWorld == null) {
            LogUtils.info("Creating nether realm world...");
            netherWorld = WorldCreator.name(getNetherWorldName(null)).type(WorldType.FLAT)
                    .environment(World.Environment.NETHER).generator(new VoidGenerator()).createWorld();
            registerToMultiverse(netherWorld);
            LogUtils.info("Successfully created nether realm world...");
        }

        if (endWorld == null) {
            LogUtils.info("Creating end realm world...");
            endWorld = WorldCreator.name(getEndWorldName(null)).type(WorldType.FLAT)
                    .environment(World.Environment.THE_END).generator(new VoidGenerator()).createWorld();
            registerToMultiverse(endWorld);
            LogUtils.info("Successfully created end realm world...");
        }

        if (normalWorld != null)
            normalWorld.setDifficulty(Difficulty.NORMAL);

        if (netherWorld != null)
            netherWorld.setDifficulty(Difficulty.NORMAL);

        if (endWorld != null)
            endWorld.setDifficulty(Difficulty.NORMAL);
    }

    private void registerToMultiverse(World world) {
        if (Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core") == null) return;

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv import " + world.getName() + " normal -g " + RealmsPlugin.getInstance().getName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv modify set generator " + RealmsPlugin.getInstance().getName() + " " + world.getName());
    }

    @Override
    public Location getNextLocation(boolean subtract) {
        Location location = getLastLocation().clone();
        int range = plugin.getPluginConfig().getPaster().getDistanceRange();
        BlockFace realmFace = getRealmFace(location);

        if (realmFace == BlockFace.NORTH) {
            location.add(range, 0, 0);
        } else if (realmFace == BlockFace.EAST) {
            if (location.getX() == -location.getZ())
                location.add(range, 0, 0);
            else if (location.getX() == location.getZ())
                location.subtract(range, 0, 0);
            else
                location.add(0, 0, range);
        } else if (realmFace == BlockFace.SOUTH) {
            if (location.getX() == -location.getZ())
                location.subtract(0, 0, range);
            else
                location.subtract(range, 0, 0);
        } else if (realmFace == BlockFace.WEST) {
            if (location.getX() == location.getZ())
                location.add(range, 0, 0);
            else
                location.subtract(0, 0, range);
        }

        setLastLocation(subtract ? location.subtract(.5, 0, .5) : location);
        if (subtract)
            return location.subtract(.5, 0, .5);
        return location;
    }

    // huge credit goes to bg-software-llc
    @Override
    public BlockFace getRealmFace(Location location) {
        if (location.getX() >= location.getZ())
            return -location.getX() > location.getZ() ? BlockFace.NORTH : BlockFace.EAST;
        else
            return -location.getX() > location.getZ() ? BlockFace.WEST : BlockFace.SOUTH;
    }

    @Override
    public String getWorldPrefix(Realm realm) {
        return plugin.getPluginConfig().getSettings().getWorldPrefix();
    }

    @Override
    public String getNormalWorldName(Realm realm) {
        return getWorldPrefix(realm) + "_normal";
    }

    @Override
    public String getNetherWorldName(Realm realm) {
        return getWorldPrefix(realm) + "_nether";
    }

    @Override
    public String getEndWorldName(Realm realms) {
        return getWorldPrefix(realms) + "_end";
    }
}