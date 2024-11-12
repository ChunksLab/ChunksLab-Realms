package com.chunkslab.realms.realm;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.biome.Biome;
import com.chunkslab.realms.api.location.ServerLocation;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;
import com.chunkslab.realms.api.realm.IRealmManager;
import com.chunkslab.realms.api.realm.Realm;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class RealmManager implements IRealmManager {

    private final RealmsPlugin plugin;

    private final Map<UUID, Realm> realmMap = new ConcurrentHashMap<>();

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
                    realmMap.put(realm.getUniqueId(), realm);
                    return true;
                });
    }

    @Override
    public CompletableFuture<Boolean> loadRealm(Realm realm) {
        realmMap.put(realm.getUniqueId(), realm);
        return CompletableFuture.completedFuture(true);
    }
}
