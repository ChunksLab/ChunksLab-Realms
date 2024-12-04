package com.chunkslab.realms.api.invite;

import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IInviteManager {

    void invitePlayer(RealmPlayer inviter, RealmPlayer target, Rank rank);

    CompletableFuture<Invite> getInvite(UUID uuid);

    void rejectInvite(RealmPlayer player);

    void acceptInvite(RealmPlayer player, Invite invite);

}