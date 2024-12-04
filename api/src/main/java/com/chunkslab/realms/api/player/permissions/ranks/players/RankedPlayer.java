package com.chunkslab.realms.api.player.permissions.ranks.players;

import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;

public interface RankedPlayer extends RealmPlayer {
    Rank getRank();

    void setRank(Rank rank);

    long getJoinDate();

    void setJoinDate(long joinDate);
}