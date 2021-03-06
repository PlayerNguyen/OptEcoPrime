package com.playernguyen.optecoprime.managers;

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
 * <p>
 * Generally, this class has a map which contains persisted data inside. and
 * control data between database and server
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

            // Not existed in map, will not put and create no
            if (persistedPlayer == null) {
                return requestPlayerFromDatabase(uuid);
            }

            // If the data was old, retrieve new data and modify object
            if (System.currentTimeMillis() - persistedPlayer.getLastUpdate() >= plugin.getSettingConfiguration()
                    .get(SettingConfigurationModel.USER_PERSIST_DATA_DURATION).asLong()) {
                // Replace player data
                OptEcoPlayer replacePlayer = requestPlayerFromDatabase(uuid);
                persistedPlayer.setBalance(replacePlayer.getBalance());
                persistedPlayer.setLastUpdate(replacePlayer.getLastUpdate());
            }

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
        return plugin.getUserController().getPlayerByUUID(uuid).orElseThrow(
                () -> new NullPointerException("Player not found in database (requestPlayerFromDatabase)"));
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
        this.add(player);
    }

    /**
     * Set a balance to player in both database and persist storage.
     *
     * @param uuid    an unique id of player to set.
     * @param balance a new balance to set.
     * @throws Exception an exception when cannot receive a value in database.
     */
    public void setPlayerBalance(@NotNull UUID uuid, double balance) throws Exception {
        plugin.getTrackers().describeAsync("set a balance of " + uuid, () -> {
            try {
                OptEcoPlayer persistedPlayer = plugin.getUserController().getPlayerByUUID(uuid)
                        .orElseThrow(NullPointerException::new);
                // If player has not changed its balance, no update
                // Otherwise, change it and update into both storage and database
                if (persistedPlayer.getBalance() != balance) {
                    // Update it into database
                    plugin.getUserController().updatePlayer(uuid, balance);
                    // Update inside storage
                    OptEcoPlayer player = this.map.get(uuid);
                    if (player != null) {
                        this.setBalance(uuid, balance);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Look up and add more balance to player account from database. Whether player
     * not existed in database, create a new player with balance is zero and add new
     * amount.
     *
     * @param uuid   a unique id of that player
     * @param amount an amount to add
     */
    public void addPlayerBalance(UUID uuid, double amount) {
        plugin.getTrackers().describeAsync("add a balance of " + uuid, () -> {
            try {
                OptEcoPlayer persistedPlayer = plugin.getUserController().getPlayerByUUID(uuid)
                        .orElse(new OptEcoPlayerInstance(uuid, plugin.getSettingConfiguration()
                                .get(SettingConfigurationModel.USER_BEGINNING_POINT).asDouble()));

                // If player has not changed its balance, no update
                // Otherwise, change it and update into both storage and database
                if (amount > 0) {
                    // Update it into database
                    plugin.getUserController().updatePlayer(uuid, persistedPlayer.getBalance() + amount);
                    // Update inside storage
                    OptEcoPlayer player = this.map.get(uuid);
                    if (player != null) {
                        this.setBalance(uuid, persistedPlayer.getBalance() + amount);
                    }
                    System.out.println("player = " + player);
                    return;
                }
                // Not match to the criteria, should handle in front of class (request class)
                throw new IllegalStateException("an amount must be greater than 0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Look up and take more balance to player account from database. Whether player
     * not existed in database, create a new player with balance is zero and take it
     * out.
     *
     * @param uuid   an unique id of player
     * @param amount an amount to take
     * @throws Exception             an exception when system cannot looked up
     *                               player.
     * @throws IllegalStateException an amount is not higher than 0
     */
    public void takePlayerBalance(UUID uuid, double amount) throws Exception {
        plugin.getTrackers().describeAsync("take balance of player" + uuid, () -> {
            try {
                OptEcoPlayer persistedPlayer = plugin.getUserController().getPlayerByUUID(uuid)
                        .orElse(new OptEcoPlayerInstance(uuid, 0));

                // If player has not change its balance, no update
                // Otherwise, change it and update into both storage and database
                if (amount > 0) {
                    // Update it into database
                    plugin.getUserController().updatePlayer(uuid, persistedPlayer.getBalance() - amount);
                    // Update inside storage
                    OptEcoPlayer player = this.map.get(uuid);
                    if (player != null) {
                        this.setBalance(uuid, persistedPlayer.getBalance() - amount);
                    }
                    return;
                }
                // Not match to the criteria, should handle in front of class (request class)
                throw new IllegalStateException("an amount must be greater than 0 (gt 0)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Transacts from player to player (pay someone). This method will take a
     * balance from `from` target and add this to `to` target. <br>
     * <p>
     * NOTE: from and to parameter must not be equaled. Otherwise throw an
     * IllegalStateException.
     *
     * @param from   a player from who
     * @param to     a player to who
     * @param amount an amount to transact
     * @throws Exception an exception
     */
    public void transact(UUID from, UUID to, double amount) throws Exception {
        // Cannot send equivalent target
        if (from.equals(to)) {
            throw new IllegalStateException("cannot send equivalent");
        }
        // Take point of from
        takePlayerBalance(from, amount);

        // Add `to` target
        addPlayerBalance(to, amount);
    }

    public boolean containsPlayer(@NotNull UUID uuid) {
        return this.map.containsKey(uuid);
    }

}
