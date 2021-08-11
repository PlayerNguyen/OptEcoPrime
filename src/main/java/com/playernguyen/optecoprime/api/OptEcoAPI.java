package com.playernguyen.optecoprime.api;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Get an interface, simple to use for your guys (developers).
 */
public interface OptEcoAPI {

    /**
     * Checks player if existed in database by using it unqiue id.
     *
     * @param uuid an unique id of that player to find.
     * @return true whether existed, false otherwise.
     */
    boolean hasPlayer(@NotNull UUID uuid) throws Exception;

    /**
     * Take points of user.
     *
     * @param unique an unique id of that player
     * @param value  a value to take
     * @throws Exception errors
     */
    void take(@NotNull UUID unique, double value) throws Exception;

    /**
     * Add points to player.
     *
     * @param unique an unique id of that player
     * @param value  a value to add
     * @throws Exception errors
     */
    void add(@NotNull UUID unique, double value) throws Exception;

    /**
     * Set points to player
     *
     * @param unique an unique id of that player
     * @param value  a value to set
     * @throws Exception errors
     */
    void set(@NotNull UUID unique, double value) throws Exception;

}
