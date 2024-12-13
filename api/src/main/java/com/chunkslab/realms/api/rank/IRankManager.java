package com.chunkslab.realms.api.rank;

import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;

import java.util.List;
import java.util.Set;

public interface IRankManager {

    void enable();

    Set<Rank> getRanks();

    List<Permission> getCommon();

    Rank getRank(Rank.Assignment assignment);
}