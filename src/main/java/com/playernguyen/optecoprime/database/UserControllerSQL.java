package com.playernguyen.optecoprime.database;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.events.OptEcoBalanceChangeEvent;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import com.playernguyen.optecoprime.players.OptEcoPlayerInstance;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * DatabaseUserController represents a controller for user database.
 */
public class UserControllerSQL implements UserController {
    /**
     * A plugin to implement some methods
     */
    private final OptEcoPrime plugin;
    /**
     * A table of users database
     */
    private String userTableName;

    /**
     * @param plugin a plugin in order to provide some methods
     */
    public UserControllerSQL(OptEcoPrime plugin) {
        this.plugin = plugin;
        load(plugin);
    }

    public void load(OptEcoPrime plugin) {
        this.userTableName = plugin
                .getSettingConfiguration()
                .get(SettingConfigurationModel.DATABASE_TABLE_PREFIX)
                .asString() + "_users";
    }

    /**
     * Deserialize response value from database.
     *
     * @param resultSet a response which was just queried.
     * @return list of user fetch from {@link ResultSet} parameter.
     * @throws SQLException an exception by SQL.
     */
    private List<OptEcoPlayer> deserializePlayersResponse(ResultSet resultSet) throws SQLException {
        // Make a new list
        List<OptEcoPlayer> players = new ArrayList<>();

        // Append all into a list
        while (resultSet.next()) {
            UUID uuid = UUID.fromString(resultSet.getString("uuid"));
            double balance = resultSet.getDouble("balance");

            players.add(new OptEcoPlayerInstance(uuid, balance));
        }
        // Return a list
        return players;
    }

    /**
     * Look up users data in database.
     *
     * @param uuid a uuid to look up
     * @return a list of players which was found.
     * @throws SQLException an exception that server cannot querying
     */
    private List<OptEcoPlayer> findByUUID(@NotNull UUID uuid) throws SQLException {
        // Request look up user data
        List<OptEcoPlayer> list = new ArrayList<>();
        plugin.getDispatch().executeQuery(resultSet ->
                        list.addAll(deserializePlayersResponse(resultSet)),
                String.format("SELECT * FROM %s WHERE uuid=?", userTableName),
                uuid.toString()
        );
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<OptEcoPlayer> getPlayerByUUID(@NotNull UUID uuid) throws SQLException {
        // Look up a player by their uuid
        List<OptEcoPlayer> players = this.findByUUID(uuid);
        if (players.size() == 0) {
            return Optional.empty();
        }
        // Return first value as optional
        return Optional.of(players.get(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPlayer(@NotNull UUID uuid, double balance) throws SQLException {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        AtomicBoolean status = new AtomicBoolean();
        plugin.getDispatch().executeUpdate((updatedRows) -> {
                    status.set(updatedRows == 1);
                }, String.format("INSERT INTO %s (uuid, balance, username) VALUES (?, ?, ?)", userTableName),
                uuid.toString(),
                balance,
                player.getName()
        );
        status.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPlayer(@NotNull UUID uuid) throws SQLException {
        return findByUUID(uuid).size() != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePlayer(UUID uuid, double balance) throws NullPointerException, SQLException {
        OptEcoPlayer persistedPlayer = this.getPlayerByUUID(uuid).orElseThrow(() -> new NullPointerException(
                "cannot update non-existed database player causes player not found " + uuid));

        // Start to update
        updateFromUUID(uuid, balance, persistedPlayer);
    }

    /**
     * An update method from uuid
     *
     * @param uuid            an uuid of player to update
     * @param balance         a balance to update (amount)
     * @param persistedPlayer a player to update
     * @throws SQLException sql errors
     */
    private void updateFromUUID(UUID uuid, double balance, OptEcoPlayer persistedPlayer) throws SQLException {
        plugin.getDispatch().executeUpdate(updateStatus -> {
            // Unexpected error
            if (updateStatus == 0) {
                throw new IllegalStateException("cannot update player");
            }

            // Call event after update
            Bukkit.getPluginManager().callEvent(
                    new OptEcoBalanceChangeEvent(plugin, persistedPlayer, persistedPlayer.getBalance(), balance));

            plugin.getTrackers().describeNothing(
                    String.format("Update &c[%s] &m%s &8-> &e%s", uuid, persistedPlayer.getBalance(), balance));
        }, String.format("UPDATE %s SET balance=? WHERE uuid=?", userTableName), balance, uuid.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<OptEcoPlayer> getHighestBalancePlayer(int offset) throws SQLException {
        // Fetch list initialize
        List<OptEcoPlayer> players = new ArrayList<>();
        // Execute a query
        plugin.getDispatch().executeQuery(resultSet -> {
            // Retrieves a player list
            players.addAll(deserializePlayersResponse(resultSet));
        }, String.format("SELECT * FROM %s ORDER BY balance DESC LIMIT %d, 1", userTableName, offset));
        // Can be null
        return Optional.ofNullable(players.size() > 0 ? players.get(0) : null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OptEcoPlayer> getHighestBalancePlayers(int limit) throws SQLException {
        // Fetch list initialize
        List<OptEcoPlayer> players = new ArrayList<>();
        // Execute a query
        plugin.getDispatch().executeQuery(resultSet -> {
            // Retrieves a player list and append it into
            players.addAll(deserializePlayersResponse(resultSet));
        }, String.format("SELECT * FROM %s ORDER BY balance DESC LIMIT %d", userTableName, limit));
        return players;
    }
}
