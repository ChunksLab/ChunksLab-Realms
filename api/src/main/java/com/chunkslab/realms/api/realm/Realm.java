package com.chunkslab.realms.api.realm;

import com.chunkslab.realms.api.location.ServerLocation;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.realm.member.MembersController;
import com.chunkslab.realms.api.realm.member.data.MembersData;
import com.chunkslab.realms.api.realm.privacy.PrivacyOption;
import com.chunkslab.realms.api.upgrade.Upgrade;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;

public interface Realm {

    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm");

    /**
     * Get the unique id of this realm.
     * @return unique id
     */
    UUID getUniqueId();

    /**
     * Get the exact moment that this realm
     * has been created.
     * @return creation milliseconds instant
     */
    long getCreationDate();

    /**
     * Get the creation date formatted.
     * @see Realm#DATE_FORMAT
     * @return formatted date
     */
    default String getCreationDateFormatted() { return DATE_FORMAT.format(getCreationDate()); }

    /**
     * Get the members data containing all the members
     * @see MembersData
     * @return members data
     */
    MembersData getMembersData();

    /**
     * Get an instance of {@link MembersController} for this realm.
     * @see MembersController
     * @return members controller instance
     */
    @NotNull MembersController getMembersController();

    /**
     * Get the biome of this realm.
     * @return biome
     */
    String getBiome();

    /**
     * Get the upgrade of this realm.
     * @param type type of upgrade
     * @return upgrade
     */
    Upgrade getUpgrade(Upgrade.Type type);

    /**
     * Get the ratings of this realm.
     * @return ratings
     */
    Map<RealmPlayer, Integer> getRatings();

    /**
     * Get the rating of this realm.
     * @param player player to get rating
     * @return rating
     */
    int getRating(RealmPlayer player);

    /**
     * Get the center location of this realm.
     * @return center location
     */
    ServerLocation getCenterLocation();

    /**
     * Get the spawn location of this realm.
     * @return spawn location
     */
    ServerLocation getSpawnLocation();

    /**
     * Get the privacy option of this realm.
     * @return privacy option
     */
    PrivacyOption getPrivacyOption();

    boolean isInBorder(Location location);

    /**
     * Set the member data of this realm.
     * @param data new member data
     */
    void setMembersData(@NotNull MembersData data);

    /**
     * Set the biome of this realm.
     * @param biome new biome
     */
    void setBiome(String biome);

    /**
     * Set the upgrade of this realm.
     * @param upgrade new upgrade
     */
    void setUpgrade(Upgrade upgrade);

    /**
     * Add a rating to this realm.
     * @param player player that rated
     * @param rating rating
     */
    void addRating(RealmPlayer player, int rating);

    /**
     * Set the center location of this realm.
     * @param centerLocation new center location
     */
    void setCenterLocation(ServerLocation centerLocation);

    /**
     * Set the spawn location of this realm.
     * @param spawnLocation new spawn location
     */
    void setSpawnLocation(ServerLocation spawnLocation);

    /**
     * Set the privacy option of this realm.
     * @param privacyOption new privacy option
     */
    void setPrivacyOption(PrivacyOption privacyOption);
}