package com.chunkslab.realms.api.player.ban;

import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;

public interface BannedPlayer extends RealmPlayer {
    long getBanDate();

    void setBanDate(long banDate);
}