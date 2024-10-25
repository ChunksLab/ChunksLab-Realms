package com.chunkslab.realms.api.player.contexts;

import com.chunkslab.realms.api.player.exceptions.UnexistingPlayerException;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface RealmPlayerContext {

    String getName();

    UUID getUniqueId();

    boolean equals(RealmPlayerContext context);

    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class Builder {
        private String name;
        private UUID uniqueId;

        public static RealmPlayerContext.Builder create() {
            return new RealmPlayerContext.Builder();
        }

        public static RealmPlayerContext.Builder create(OfflinePlayer player) throws UnexistingPlayerException {
            if (!player.isOnline() && !player.hasPlayedBefore())
                throw new UnexistingPlayerException();
            return new RealmPlayerContext.Builder(player.getName(), player.getUniqueId());
        }

        public static RealmPlayerContext.Builder create(String name) throws UnexistingPlayerException {
            OfflinePlayer player = Bukkit.getPlayer(name);
            if (player == null)
                player = Bukkit.getOfflinePlayer(name);
            return create(player);
        }

        public static RealmPlayerContext.Builder create(UUID uuid) throws UnexistingPlayerException {
            OfflinePlayer player = Bukkit.getPlayer(uuid);
            if (player == null)
                player = Bukkit.getOfflinePlayer(uuid);
            return create(player);
        }

        public RealmPlayerContext.Builder name(String name) {
            this.name = name;
            return this;
        }

        public RealmPlayerContext.Builder uuid(UUID uniqueId) {
            this.uniqueId = uniqueId;
            return this;
        }

        public RealmPlayerContext build() {
            Preconditions.checkNotNull(name, "Specify a valid name to create a new instance of RealmPlayerContext");
            Preconditions.checkNotNull(uniqueId, "Specify a valid UUID to create a new instance of RealmPlayerContext");
            return new DefaultRealmPlayerContext(name, uniqueId);
        }
    }
}