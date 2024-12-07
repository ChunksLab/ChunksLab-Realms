package com.chunkslab.realms.api.realm.member;

import com.chunkslab.realms.api.player.ban.BannedPlayer;
import com.chunkslab.realms.api.player.contexts.RealmPlayerContext;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;
import com.chunkslab.realms.api.player.permissions.ranks.players.RankedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface MembersController {

    /**
     * Get a list of all {@link RankedPlayer} member of the realm.
     * @return set of members
     */
    @NotNull
    Set<RankedPlayer> getMembers();

    /**
     * Get a list of all {@link RealmPlayer} visitors of the realm.
     * @return set of visitors
     */
    @NotNull
    Set<RealmPlayer> getVisitors();

    /**
     * Get a list of all {@link RankedPlayer} banned players of the realm.
     * @return set of members
     */
    @NotNull
    Set<BannedPlayer> getBans();

    /**
     * Get the number of members of this realm.
     * @return number of members
     */
    int getMembersCount();

    /**
     * Get the number of bans of this realm.
     * @return number of bans
     */
    int getBansCount();

    /**
     * Get the number of visitors of this realm.
     * @return number of visitors
     */
    int getVisitorsCount();

    /**
     * Get the member
     * @return the member ranked player
     */
    RankedPlayer getMember(RealmPlayer realmPlayer);

    /**
     * Get the banned player
     * @return the banned player
     */
    BannedPlayer getBanned(RealmPlayer realmPlayer);

    /**
     * Check if a {@link RealmPlayer} is member of this realm.
     * @param player to check
     * @return true if it's a member
     */
    boolean isMember(@NotNull RealmPlayer player);

    /**
     * Check if a {@link RealmPlayer} is member of this realm.
     * @param playerContext to check
     * @return true if it's a member
     */
    boolean isMember(@NotNull RealmPlayerContext playerContext);

    /**
     * Check if a {@link RealmPlayer} is visitor of this realm.
     * @param player to check
     * @return true if it's a visitor
     */
    boolean isVisitor(@NotNull RealmPlayer player);

    /**
     * Check if a {@link RealmPlayer} is banned of this realm.
     * @param player to check
     * @return true if it's a banned
     */
    boolean isBanned(@NotNull RealmPlayer player);

    /**
     * Set or add a new member inside this realm.
     * @param player to set/add
     * @param rank to set
     * @param onlyExisting true means that if the player is not a member of the
     *                     realm, don't add the player as new member
     */
    void setMember(@NotNull RealmPlayer player, @NotNull Rank rank, boolean onlyExisting);

    /**
     * Set or add a new member inside this realm.
     * @param player to set/add
     * @param rank to set
     * @param joinDate to set
     * @param onlyExisting true means that if the player is not a member of the
     *                     realm, don't add the player as new member
     */
    void setMember(@NotNull RealmPlayer player, @NotNull Rank rank, long joinDate, boolean onlyExisting);

    /**
     * Remove a member from this realm.
     * @param player to remove
     */
    void removeMember(RealmPlayer player);

    /**
     * Get the {@link Rank} of a member of this realm.
     * @param player to find
     * @return rank found, null if the player is not a member
     */
    @Nullable
    Rank getRank(@NotNull RealmPlayer player);

    /**
     * Get the owner of this realm.
     * @return the owner player context
     */
    @NotNull RealmPlayerContext getOwner();

    /**
     * Get the owner of this realm.
     * @return the owner player
     */
    @NotNull RealmPlayer getOwnerPlayer();

    /**
     * Check if a {@link RealmPlayerContext} is the same of the
     * owner of this realm.
     * @param context to check
     * @return true if is the owner
     */
    boolean isOwner(@NotNull RealmPlayerContext context);

    /**
     * Set the owner of this realm.
     * @param player to set
     */
    void setOwner(@NotNull RealmPlayer player);

    /**
     * Execute this method when a new member become part of this realm.
     * @param player to add to the members
     */
    void join(@NotNull RealmPlayer player);

    /**
     * Execute this method when a member is kicked from this realm.
     * @param player to remove from the members
     */
    void kick(@NotNull RealmPlayer player, @Nullable String kicker);
}