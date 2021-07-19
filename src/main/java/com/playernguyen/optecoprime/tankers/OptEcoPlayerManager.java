package com.playernguyen.optecoprime.tankers;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import com.playernguyen.optecoprime.players.OptEcoPlayerInstance;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * A storage that enhances function such as add, remove, ... As a class, this
 * class is easy to modify, manager and extensible for many features. <br>
 * 
 * Generally, this class has a map which contains persisted data inside. and
 * control data between database and server
 *
 */
public class OptEcoPlayerManager {
    private final Map<UUID, OptEcoPlayer> map = new HashMap<>();
    /**
     * Plugin field
     */
    private final OptEcoPrime plugin;

    /**
     * A user tanker constructor
     *
     * @param plugin plugin object to extend some methods
     */
    public OptEcoPlayerManager(OptEcoPrime plugin) {
        this.plugin = plugin;
    }

    /**
     * Add player into tanker
     *
     * @param player a player to add
     */
    public void add(@NotNull OptEcoPlayer player) {
        plugin.getTrackers().describeTanker("add player " + player.getUniqueId(), () -> {
            this.map.put(player.getUniqueId(), player);
        });
    }

    /**
     * Remove player out of collection in tanker
     *
     * @param uuid a uuid of that player
     */
    public void remove(@NotNull UUID uuid) {
        plugin.getTrackers().describeTanker("remove player by uuid " + uuid, () -> {
            this.map.remove(uuid);
        });
    }

    /**
     * Empty a current storage map.
     */
    public void clear() {
        plugin.getTrackers().describeTanker("clear map", this.map::clear);
    }

    /**
     * Set new balance value and update a lastUpdate time.
     *
     * @param uuid    an uuid of player to set
     * @param balance a new balance
     */
    private void setBalance(@NotNull UUID uuid, double balance) {
        plugin.getTrackers().describeTanker("update new balance " + uuid + " -> " + balance, () -> {
            OptEcoPlayer player = map.get(uuid);
            if (player == null) {
                throw new NullPointerException("Player not exist in map " + uuid);
            }
            player.setBalance(balance);
            player.setLastUpdate(System.currentTimeMillis());
        });
    }

    /**
     * Get a player information object which persisted in map. Whether the data was
     * out of date, request a new one and replace balance data.
     *
     * @param uuid an uuid of player
     * @return a request player data
     * @throws ExecutionException   causes by describeTankerAsync
     * @throws InterruptedException causes by describeTankerAsync
     */
    @NotNull
    public OptEcoPlayer getPlayer(@NotNull UUID uuid) throws ExecutionException, InterruptedException {
        return plugin.getTrackers().describeTankerAsync("get player from storage by id " + uuid, () -> {
            OptEcoPlayer persistedPlayer = map.get(uuid);

            // Not existed in map
            if (persistedPlayer == null) {
                return requestPlayerFromDatabase(uuid);
            }

            // If the data was old, retrieve new data and modify object
            if (System.currentTimeMillis() - persistedPlayer.getLastUpdate() >= plugin.getSettingConfiguration()
                    .get(SettingConfigurationModel.USER_PERSIST_DATA_DURATION).asLong()) {

            }

            // Replace player data
            OptEcoPlayer replacePlayer = requestPlayerFromDatabase(uuid);
            persistedPlayer.setBalance(replacePlayer.getBalance());
            persistedPlayer.setLastUpdate(replacePlayer.getLastUpdate());

            // Then return an object to user
            return persistedPlayer;
        });
    }

    /**
     * Request player information from database.
     * 
     * @param uuid an unique id of that player
     * @return a player class
     * @throws NullPointerException not found player
     * @throws Exception            an exception
     */
    private OptEcoPlayer requestPlayerFromDatabase(UUID uuid) throws NullPointerException, Exception {
        return plugin.getDatabaseUserController().getPlayerByUUID(uuid)
                .orElseThrow(() -> new NullPointerException("user not found in database " + uuid));
    }

    /**
     * Add new player by the unique id by request player in database.
     * 
     * @param uuid an uuid.
     * @throws Exception            exception
     * @throws NullPointerException not found player in database
     */
    public void add(UUID uuid) throws NullPointerException, Exception {
        // Request from database
        OptEcoPlayer player = requestPlayerFromDatabase(uuid);
        // Put into map
        this.add(player);// Fixme: here I am
    }

    public void setPlayerBalance(UUID uuid, double balance) throws Exception {
        OptEcoPlayer persistedPlayer = plugin.getDatabaseUserController().getPlayerByUUID(uuid)
                .orElse(new OptEcoPlayerInstance(uuid, balance));

        // If player has not change its balance, no update
        // Otherwise, change it and update into both storage and database
        if (persistedPlayer.getBalance() != balance) {
            // Update it into database
            plugin.getDatabaseUserController().updatePlayer(uuid, balance);
            // Update inside storage
            OptEcoPlayer player = this.map.get(uuid);
            if (player != null) {
                this.setBalance(uuid, balance);
            }
        }
    }

}