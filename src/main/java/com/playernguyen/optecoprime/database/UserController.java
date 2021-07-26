package com.playernguyen.optecoprime.database;

import java.util.Optional;
import java.util.UUID;

import com.playernguyen.optecoprime.players.OptEcoPlayer;

import org.jetbrains.annotations.NotNull;

/**
 * Controls your data from database via Bukkit server. This class receives all
 * data, push a new item, and even remove something if necessary .However, this
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
     * @param player an {@link OptEcoPlayer} object to put into database.
     * @return response that system was added or not. (true/false)
     * @throws Exception any exception when querying
     */
    public boolean addPlayer(@NotNull UUID uuid, double balance) throws Exception;

    /**
     * Look up for the existence of that player.
     *
     * @param uuid a uuid to look up that player are existed
     * @return true when player existed, false otherwise.
     * @throws Exception any exception when querying
     */
    public boolean hasPlayer(@NotNull UUID uuid) throws Exception;

    /**
     * Update new persist data of player. If player are not found, throws
     * {@link NullPointerException}.
     *
     * @param player a player to update
     * @throws Exception any exception when querying
     * 
     */
    public void updatePlayer(UUID uuid, double balance) throws Exception;

    /**
     * Update player ignore the existence of player in database. If player are not
     * found in database, create a new one.
     * 
     * @param player a player data.
     * @throws Exception an exception
     */
    public void updatePlayerIgnoreNull(UUID uuid, double balance) throws Exception;
}
