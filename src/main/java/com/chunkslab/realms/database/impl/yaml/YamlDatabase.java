package com.chunkslab.realms.database.impl.yaml;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.database.Database;
import com.chunkslab.realms.api.player.RealmPlayer;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.api.util.LocationUtils;
import com.chunkslab.realms.api.util.LogUtils;
import com.chunkslab.realms.player.RealmPlayerImpl;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.UUID;

@RequiredArgsConstructor
public class YamlDatabase implements Database {

    private static final File REALMS_FOLDER = new File(RealmsPlugin.getInstance().getDataFolder(), "realm-data");
    private static final File PLAYERS_FOLDER = new File(RealmsPlugin.getInstance().getDataFolder(), "player-data");
    private static final YamlData OTHER_DATA = new YamlData(RealmsPlugin.getInstance(), "data.yml");

    private final RealmsPlugin plugin;

    @Override
    public void enable() {
        if ((!REALMS_FOLDER.exists() && !REALMS_FOLDER.mkdirs() || !PLAYERS_FOLDER.exists() && !PLAYERS_FOLDER.mkdirs())) {
            LogUtils.severe("Failed to create the realms/player data folders.");
            return;
        }

        OTHER_DATA.create();
        if (OTHER_DATA.isSet("last-realm-location")) {
            plugin.getWorldManager().setLastLocation(LocationUtils.getLocation(OTHER_DATA.getString("last-realm-location")));
        } else {
            World world = Bukkit.getWorld(plugin.getWorldManager().getNormalWorldName(null));
            plugin.getWorldManager().setLastLocation(new Location(world, 0, 99, 0));
        }
    }

    @Override
    public void disable() {
        for (RealmPlayer player : RealmsPlugin.getInstance().getPlayerManager().getPlayers()) {
            savePlayer(player);
        }

        OTHER_DATA.set("last-realm-location", LocationUtils.getLocation(plugin.getWorldManager().getLastLocation()));
        OTHER_DATA.save();
    }

    @Override
    public Realm loadRealm(UUID realmUUID) {
        return null;
    }

    @Override
    public RealmPlayer loadPlayer(UUID playerUUID, boolean loadAnyway) {
        RealmPlayer player = RealmsPlugin.getInstance().getPlayerManager().getPlayer(playerUUID);
        if (player != null && !loadAnyway) {
            return player;
        }

        YamlData data = new YamlData(PLAYERS_FOLDER.getPath(), playerUUID.toString());
        data.create();

        player = new RealmPlayerImpl(playerUUID);
        String name = data.getString("name");
        long lastLogout = data.getLong("lastLogout");
        if (data.isSet("realm")) {
            UUID realmUUID = UUID.fromString(data.getString("realm"));
            Realm realm = loadRealm(realmUUID);
            player.setRealm(realm);
        }

        player.setName(name);
        player.setLastLogout(lastLogout);
        player.setOnline(true);

        plugin.getPlayerManager().addPlayer(player);

        return player;
    }

    @Override
    public RealmPlayer loadPlayer(String name, boolean loadAnyway) {
        RealmPlayer player = RealmsPlugin.getInstance().getPlayerManager().getPlayer(name);
        if (player != null && !loadAnyway) {
            return player;
        }

        File[] files = PLAYERS_FOLDER.listFiles();
        if (files == null) return null;

        for (File file : files) {
            if (!file.getName().endsWith(".yml")) continue;

            YamlData data = new YamlData(PLAYERS_FOLDER.getPath(), file.getName());
            data.create();

            if (!name.equals(data.getString("name"))) continue;

            player = new RealmPlayerImpl(UUID.fromString(file.getName().replace(".yml", "")));
            long lastLogout = data.getLong("lastLogout");
            if (data.isSet("realm")) {
                UUID realmUUID = UUID.fromString(data.getString("realm"));
                Realm realm = loadRealm(realmUUID);
                player.setRealm(realm);
            }

            player.setName(name);
            player.setLastLogout(lastLogout);
            player.setOnline(true);

            plugin.getPlayerManager().addPlayer(player);

            return player;
        }

        return null;
    }

    @Override
    public void saveRealm(Realm realm) {

    }

    @Override
    public void savePlayer(RealmPlayer player) {
        YamlData data = new YamlData(PLAYERS_FOLDER.getPath(), player.getUniqueId().toString());
        data.create();

        data.set("name", player.getName());

        if (player.getRealm() != null)
            data.set("realm", player.getRealm().getUniqueId());
        data.set("lastLogout", System.currentTimeMillis());

        data.save();
    }

    @Override
    public void deleteRealm(UUID realmUUID) {

    }
}
