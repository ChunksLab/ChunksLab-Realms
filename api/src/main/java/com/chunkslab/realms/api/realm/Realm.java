package com.chunkslab.realms.api.realm;

import net.kyori.adventure.text.Component;

import java.text.SimpleDateFormat;
import java.util.UUID;

public interface Realm {

    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm");

    /**
     * Get the unique id of this realm.
     * @return unique id
     */
    UUID getUniqueId();

    /**
     * Get the name of the realm.
     * @return string name
     */
    Component getName();

    /**
     * Get a string that represents
     * the description of this realm.
     * @return realm's description
     */
    Component getDescription();

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
}