package com.chunkslab.realms.realm;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.biome.Biome;
import com.chunkslab.realms.api.location.ServerLocation;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;
import com.chunkslab.realms.api.player.permissions.ranks.players.RankedPlayer;
import com.chunkslab.realms.api.realm.IRealmManager;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.api.upgrade.Upgrade;
import com.chunkslab.realms.api.util.LogUtils;
import com.chunkslab.realms.util.ChatUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class RealmManager implements IRealmManager {

    private final RealmsPlugin plugin;

    private final Map<UUID, Realm> realmMap = new ConcurrentHashMap<>();
    private final TreeMap<Double, TreeMap<Double, Realm>> locationMap = new TreeMap<>();

    @Override
    public Collection<Realm> getRealms() {
        return realmMap.values();
    }

    @Override
    public Realm getRealm(UUID uniqueId) {
        return realmMap.get(uniqueId);
    }

    @Override
    public CompletableFuture<Boolean> createRealm(Biome biome, RealmPlayer realmPlayer) {
        Location location = plugin.getWorldManager().getNextLocation(true);
        return plugin.getSchematicManager().getSchematicPaster()
                .paste(location, biome.getSchematics().get(new Random().nextInt(biome.getSchematics().size())))
                .thenApply(result -> {
                    Realm realm = new DefaultRealm(biome.getId());
                    realm.getMembersController().setMember(realmPlayer, plugin.getRankManager().getRank(Rank.Assignment.OWNER), false);
                    realm.setCenterLocation(ServerLocation.Builder.create(result.left()).build());
                    realm.setSpawnLocation(ServerLocation.Builder.create(result.right()).build());

                    realmPlayer.setRealmId(realm.getUniqueId());
                    loadRealm(realm);
                    return true;
                });
    }

    @Override
    public CompletableFuture<Boolean> loadRealm(Realm realm) {
        realmMap.put(realm.getUniqueId(), realm);

        putLocations(realm);

        return CompletableFuture.completedFuture(true);
    }

    private void putLocations(Realm realm) {
        TreeMap<Double, Realm> map = new TreeMap<>();
        double range = realm.getUpgrade(Upgrade.Type.SIZE).value() * 16;
        if (range % 2 == 0)
            range += 1;
        map.put(realm.getCenterLocation().getLocation().getBlockZ() - range, realm);
        locationMap.put(realm.getCenterLocation().getLocation().getBlockX() - range, map);
    }

    @Override
    public void unloadRealm(Realm realm) {
        if (!this.realmMap.containsKey(realm.getUniqueId())) return;

        LogUtils.debug(String.format("Unloading realm... [%s]", realm.getUniqueId().toString()));

        this.realmMap.remove(realm.getUniqueId());

        plugin.getScheduler().runTaskSync(() -> plugin.getDatabase().saveRealm(realm), realm.getSpawnLocation().getLocation());
    }

    @Override
    public void deleteRealm(Realm realm) {
        LogUtils.debug(String.format("Deleting realm... [%s]", realm.getUniqueId().toString()));

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getDatabase().deleteRealm(realm));

        for (RankedPlayer rankedPlayer : realm.getMembersController().getMembers()) {
            RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(rankedPlayer.getUniqueId());
            if (realmPlayer != null && rankedPlayer.getBukkitPlayer() != null) {
                realmPlayer.setRealmId(null);
                ChatUtils.sendMessage(rankedPlayer.getBukkitPlayer(), ChatUtils.format(plugin.getPluginMessages().getRealmDeleted()));
            }
        }

        for (RealmPlayer visitor : realm.getMembersController().getVisitors()) {
            if (visitor.getBukkitPlayer() != null) {
                ChatUtils.sendMessage(visitor.getBukkitPlayer(), ChatUtils.format(plugin.getPluginMessages().getRealmDeletedVisitor()));
                //TODO: teleport the player safe location
            }
        }

        this.realmMap.remove(realm.getUniqueId());
    }

    @Override
    public Realm getRealm(Location location) {
        // idea is from BentoBox, big credit to them.
        Map.Entry<Double, TreeMap<Double, Realm>> entry = locationMap.floorEntry((double) location.getBlockX());
        if (entry == null) return null;

        Map.Entry<Double, Realm> zEntry = entry.getValue().floorEntry((double) location.getBlockZ());
        if (zEntry == null) return null;

        Realm realm = zEntry.getValue();
        if (realm.isInBorder(location))
            return realm;

        return null;
    }
}
