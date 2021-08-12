package com.playernguyen.optecoprime.database;

import com.playernguyen.optecoprime.players.OptEcoPlayer;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
     * @param uuid    an player uuid object to put into database.
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
     * @param uuid    a player uuid to update
     * @param balance an amount to update
     * @throws Exception any exception when querying
     */
    void updatePlayer(UUID uuid, double balance) throws Exception;

    /**
     * Get a player has the highest point with offset. For example,
     * <br>
     * <ul>
     *     <li>Player 1 - 100</li>
     *     <li>Player 2 - 97</li>
     *     <li>Player 3 - 96</li>
     * </ul>
     *
     * <br>
     * If offset is 1, means take a Player 2 and ignore Player 1 or higher.
     *
     * @param offset an index offset to ignore higher point.
     * @return a player who has the highest point.
     * @throws Exception errors
     */
    Optional<OptEcoPlayer> getHighestBalancePlayer(int offset) throws Exception;

    /**
     * Retrieves and responses a player list that contains highest players balance.
     *
     * @param limit limit a response
     * @return a highest balance player list
     * @throws Exception errors
     */
    List<OptEcoPlayer> getHighestBalancePlayers(int limit) throws Exception;

}
