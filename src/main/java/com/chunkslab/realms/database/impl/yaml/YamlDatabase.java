package com.chunkslab.realms.database.impl.yaml;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.database.Database;
import com.chunkslab.realms.api.player.MessagePreference;
import com.chunkslab.realms.api.player.ban.BannedPlayer;
import com.chunkslab.realms.api.player.ban.DefaultBannedPlayer;
import com.chunkslab.realms.api.player.contexts.RealmPlayerContext;
import com.chunkslab.realms.api.player.objects.DefaultRealmPlayer;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;
import com.chunkslab.realms.api.player.permissions.ranks.players.RankedPlayer;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.api.realm.bank.log.BankAction;
import com.chunkslab.realms.api.realm.bank.log.BankLog;
import com.chunkslab.realms.api.realm.privacy.PrivacyOption;
import com.chunkslab.realms.api.upgrade.Upgrade;
import com.chunkslab.realms.api.util.LocationUtils;
import com.chunkslab.realms.api.util.LogUtils;
import com.chunkslab.realms.realm.DefaultRealm;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            plugin.getWorldManager().setLastLocation(new Location(world, 0, -50, 0));
        }

        File[] files = REALMS_FOLDER.listFiles();
        if (files != null) {
            for (File file : files) {
                UUID realmUUID = UUID.fromString(file.getName().replace(".yml", ""));
                Realm realm = loadRealm(realmUUID);
                plugin.getRealmManager().loadRealm(realm);
            }
        }
    }

    @Override
    public void disable() {
        for (Realm realm : RealmsPlugin.getInstance().getRealmManager().getRealms()) {
            saveRealm(realm);
        }

        for (RealmPlayer player : RealmsPlugin.getInstance().getPlayerManager().getPlayers()) {
            savePlayer(player);
        }

        OTHER_DATA.set("last-realm-location", LocationUtils.getLocation(plugin.getWorldManager().getLastLocation()));
        OTHER_DATA.save();
    }

    @Override
    public Realm loadRealm(UUID realmUUID) {
        YamlData data = new YamlData(REALMS_FOLDER.getPath(), realmUUID.toString());
        data.create();

        Realm realm = new DefaultRealm(realmUUID, data.getLong("creationDate"));
        realm.setBiome(data.getString("biome"));
        realm.setCenterLocation(LocationUtils.getServerLocation(data.getString("centerLocation")));
        realm.setSpawnLocation(LocationUtils.getServerLocation(data.getString("spawnLocation")));
        realm.setPrivacyOption(PrivacyOption.valueOf(data.getString("privacyOption")));

        // members
        for (String uuid : data.getConfigurationSection("membersData").getKeys(false)) {
            RealmPlayer player = loadPlayer(UUID.fromString(uuid));
            Rank rank = plugin.getRankManager().getRank(Rank.Assignment.valueOf(data.getString("membersData." + uuid + ".rank")));
            long joinDate = data.getLong("membersData." + uuid + ".joinDate");
            realm.getMembersController().setMember(player, rank, joinDate, false);
        }

        // banned members
        for (String uuid : data.getConfigurationSection("banData").getKeys(false)) {
            RealmPlayer player = loadPlayer(UUID.fromString(uuid));
            long banDate = data.getLong("banData." + uuid + ".banDate");
            realm.getMembersController().getBans().add(new DefaultBannedPlayer(player.getContext(), banDate));
        }

        // bank
        realm.getRealmBank().setBalance(BigDecimal.valueOf(data.getDouble("bank.balance")));
        List<String> logs = data.getStringList("bank.logs");
        for (String s : logs) {
            String[] log = s.split(",");
            RealmPlayer player = loadPlayer(UUID.fromString(log[0]));
            BankAction action = BankAction.valueOf(log[1]);
            BigDecimal amount = new BigDecimal(log[2]);
            long time = Long.parseLong(log[3]);
            BankLog bankLog = new BankLog(player, action, amount, time);
            realm.getRealmBank().addLog(bankLog);
        }

        // upgrades
        if (data.isSet("upgrades")) {
            for (String s : data.getConfigurationSection("upgrades").getKeys(false)) {
                Upgrade.Type type = Upgrade.Type.valueOf(s);
                int l = data.getInt("upgrades." + s);
                Upgrade upgrade = plugin.getUpgradeManager().getUpgrade(type, l);
                realm.setUpgrade(upgrade);
            }
        }

        // ratings
        if (data.isSet("ratings")) {
            for (String s : data.getConfigurationSection("ratings").getKeys(false)) {
                int rating = data.getInt("ratings." + s, -1);
                realm.addRating(loadPlayer(UUID.fromString(s)), rating);
            }
        }

        return realm;
    }

    @SneakyThrows
    @Override
    public RealmPlayer loadPlayer(UUID playerUUID) {
        RealmPlayer player = plugin.getPlayerManager().getPlayer(playerUUID);
        if (player != null) return player;

        YamlData data = new YamlData(PLAYERS_FOLDER.getPath(), playerUUID.toString());
        data.create();

        player = new DefaultRealmPlayer(RealmPlayerContext.Builder.create(playerUUID).build());
        long lastLogout = data.getLong("lastLogout");
        boolean bypass = data.getBoolean("bypass");
        if (data.isSet("realm")) {
            UUID realmUUID = UUID.fromString(data.getString("realm"));
            player.setRealmId(realmUUID);
        }
        player.getData().setLastLogout(lastLogout);
        //player.getData().setMessagePreference(MessagePreference.valueOf(data.getString("messagePreference")));
        player.getData().setBypass(bypass);

        plugin.getPlayerManager().addPlayer(player);
        return player;
    }

    @Override
    public RealmPlayer loadPlayer(String name) {
        RealmPlayer player = plugin.getPlayerManager().getPlayer(name);
        if (player != null) return player;

        File[] files = PLAYERS_FOLDER.listFiles();
        if (files == null) return null;

        for (File file : files) {
            if (!file.getName().endsWith(".yml")) continue;

            YamlData data = new YamlData(PLAYERS_FOLDER.getPath(), file.getName());
            data.create();

            if (!name.equals(data.getString("name"))) continue;
            player = new DefaultRealmPlayer(RealmPlayerContext.Builder.create().uuid(UUID.fromString(file.getName().replace(".yml", ""))).name(name).build());
            long lastLogout = data.getLong("lastLogout");
            MessagePreference messagePreference = MessagePreference.valueOf(data.getString("messagePreference"));
            boolean bypass = data.getBoolean("bypass");
            if (data.isSet("realm")) {
                UUID realmUUID = UUID.fromString(data.getString("realm"));
                player.setRealmId(realmUUID);
            }
            player.getData().setLastLogout(lastLogout);
            player.getData().setMessagePreference(messagePreference);
            player.getData().setBypass(bypass);

            plugin.getPlayerManager().addPlayer(player);

            return player;
        }

        return null;
    }

    @Override
    public void saveRealm(Realm realm) {
        YamlData data = new YamlData(REALMS_FOLDER.getPath(), realm.getUniqueId().toString());
        data.create();

        data.set("uniqueId", realm.getUniqueId().toString());
        data.set("creationDate", realm.getCreationDate());
        data.set("biome", realm.getBiome());
        data.set("centerLocation", LocationUtils.getServerLocation(realm.getCenterLocation()));
        data.set("spawnLocation", LocationUtils.getServerLocation(realm.getSpawnLocation()));
        data.set("privacyOption", realm.getPrivacyOption().name());

        // members
        for (RankedPlayer member : realm.getMembersController().getMembers()) {
            data.set("membersData." + member.getUniqueId().toString() + ".name", member.getName());
            data.set("membersData." + member.getUniqueId().toString() + ".rank", member.getRank().assignment().name());
            data.set("membersData." + member.getUniqueId().toString() + ".joinDate", member.getJoinDate());
        }

        // banned members
        for (BannedPlayer bannedPlayer : realm.getMembersController().getBans()) {
            data.set("banData." + bannedPlayer.getUniqueId().toString() + ".name", bannedPlayer.getName());
            data.set("banData." + bannedPlayer.getUniqueId().toString() + ".banDate", bannedPlayer.getBanDate());
        }

        // bank
        data.set("bank.balance", realm.getRealmBank().getBalance().doubleValue());
        List<String> logs = new ArrayList<>();
        for (BankLog log : realm.getRealmBank().getLogs()) {
            logs.add(log.player().getUniqueId() + "," + log.action().name() + "," + log.amount() + "," + log.timestamp());
        }
        data.set("bank.logs", logs);

        // upgrades
        for (Upgrade.Type type : Upgrade.Type.VALUES) {
            Upgrade upgrade = realm.getUpgrade(type);
            if (upgrade == null) continue;
            data.set("upgrades." + type.name(), upgrade.level());
        }

        // ratings
        for (Map.Entry<RealmPlayer, Integer> entry : realm.getRatings().entrySet()) {
            data.set("ratings." + entry.getKey().getUniqueId() + ".name", entry.getKey().getName());
            data.set("ratings." + entry.getKey().getUniqueId() + ".rating", entry.getValue());
        }

        data.save();
    }

    @Override
    public void savePlayer(RealmPlayer player) {
        YamlData data = new YamlData(PLAYERS_FOLDER.getPath(), player.getUniqueId().toString());
        data.create();

        data.set("name", player.getName());

        if (player.getRealm() != null)
            data.set("realm", player.getRealm().getUniqueId().toString());
        data.set("lastLogout", System.currentTimeMillis());
        data.set("messagePreference", player.getData().getMessagePreference().name());
        data.set("bypass", player.getData().isBypass());

        data.save();
    }

    @Override
    public void deleteRealm(UUID realmUUID) {

    }
}
