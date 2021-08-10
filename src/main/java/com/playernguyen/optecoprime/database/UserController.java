package com.playernguyen.optecoprime.database;

import java.util.Optional;
import java.util.UUID;

import com.playernguyen.optecoprime.players.OptEcoPlayer;

import org.jetbrains.annotations.NotNull;

/**
 * Controls your data from database via Bukkit server. This class receives all
 * data, push a new item, and even remove something if necessary. However, this
 * controller should be in READ-WRITE (not DELETE) mode.
 */
public interface UserController {

    /**
     * Retrieve a player data by a unique ID.
     *
     * @param uuid a uuid to look up
     * @return nullable optional which contains first value of that player.
     * @throws Exception any exception when querying
     */
    Optional<OptEcoPlayer> getPlayerByUUID(@NotNull UUID uuid) throws Exception;

    /**
     * Insert new player into database by {@link OptEcoPlayer} object.
     *
     * @param uuid an player uuid object to put into database.
     * @param balance a balance to be added, as default
     * @throws Exception any exception when querying
     */
    void addPlayer(@NotNull UUID uuid, double balance) throws Exception;

    /**
     * Look up for the existence of that player.
     *
     * @param uuid a uuid to look up that player are existed
     * @return true when player existed, false otherwise.
     * @throws Exception any exception when querying
     */
    boolean hasPlayer(@NotNull UUID uuid) throws Exception;

    /**
     * Update new persist data of player. If player are not found, throws
     * {@link NullPointerException}.
     *
     * @param uuid a player uuid to update
     * @param balance an amount to update
     * @throws Exception any exception when querying
     * 
     */
    void updatePlayer(UUID uuid, double balance) throws Exception;

    /**
     * Update player ignore the existence of player in database. If player are not
     * found in database, create a new one.
     * 
     * @param uuid a player to update
     * @param balance an amount to update
     * @throws Exception any exception when querying
     */
    void updatePlayerIgnoreNull(UUID uuid, double balance) throws Exception;
}
