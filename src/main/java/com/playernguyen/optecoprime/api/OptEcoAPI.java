package com.playernguyen.optecoprime.api;

import com.playernguyen.optecoprime.players.OptEcoPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Get an interface, simple to use for developers.
 */
public interface OptEcoAPI {

    /**
     * Checks player if existed in database by using it unqiue id.
     *
     * @param uuid an unique id of that player to find.
     * @return true whether existed, false otherwise.
     * @throws Exception an exception to any error
     */
    boolean hasPlayer(@NotNull UUID uuid) throws Exception;

    /**
     * Retrieves a player contains in server, null whether to be empty.
     *
     * @param uuid a unique id of that player
     * @return a player in server; otherwise null.
     * @throws Exception an exception to any error
     */
    @Nullable
    OptEcoPlayer get(@NotNull UUID uuid) throws Exception;

    /**
     * Takes points of user.
     *
     * @param unique a unique id of that player
     * @param value  a value to take
     * @throws Exception an exception to any error
     */
    void take(@NotNull UUID unique, double value) throws Exception;

    /**
     * Adds points to player.
     *
     * @param unique a unique id of that player
     * @param value  a value to add
     * @throws Exception an exception to any error
     */
    void add(@NotNull UUID unique, double value) throws Exception;

    /**
     * Sets points to player
     *
     * @param unique a unique id of that player
     * @param value  a value to set
     * @throws Exception an exception to any error
     */
    void set(@NotNull UUID unique, double value) throws Exception;

    /**
     * Retrieves a currency symbol which configured
     *
     * @return a currency symbol which configured in Setting.yml
     */
    String currencySymbol();

}
