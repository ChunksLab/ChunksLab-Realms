package com.chunkslab.realms.api.invite;

import com.chunkslab.realms.api.player.objects.RealmPlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IInviteManager {

    void invitePlayer(RealmPlayer inviter, RealmPlayer target);

    /**
     * @param uuid UUID of the target player
     * @return The Realm UUID player got invited to
     */
    CompletableFuture<UUID> getInvite(UUID uuid);

    void rejectInvite(RealmPlayer player);

    void acceptInvite(RealmPlayer player, UUID realmUniqueId);

}