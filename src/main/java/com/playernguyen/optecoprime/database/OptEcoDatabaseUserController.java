package com.playernguyen.optecoprime.database;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import com.playernguyen.optecoprime.players.OptEcoPlayerInstance;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import com.playernguyen.pndb.sql.query.CriteriaBuilder;
import com.playernguyen.pndb.sql.query.CriteriaField;
import com.playernguyen.pndb.sql.query.DatabaseQueryBuilder;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * DatabaseUserController represents a controller for user database.
 */
public class OptEcoDatabaseUserController {
    /**
     * A plugin to implement some methods
     */
    private final OptEcoPrime plugin;
    /**
     * A table of users database
     */
    private final String userTableName;

    /**
     * @param plugin a plugin in order to provide some methods
     */
    public OptEcoDatabaseUserController(OptEcoPrime plugin) {
        this.plugin = plugin;
        this.userTableName = plugin.getSettingConfiguration()
                .get(SettingConfigurationModel.DATABASE_TABLE_PREFIX).asString() + "_users";
    }

    /**
     * Deserialize response value from database.
     *
     * @param resultSet a response which was just queried.
     * @return list of user fetch from {@link ResultSet} parameter.
     * @throws SQLException an exception by SQL.
     */
    private List<OptEcoPlayer> deserializePlayersResponse(ResultSet resultSet)
            throws SQLException {
        // Make a new list
        List<OptEcoPlayer> players = new ArrayList<>();

        // Append all into a list
        while (resultSet.next()) {
            UUID uuid = UUID.fromString(resultSet.getString("uuid"));
            double balance = resultSet.getDouble("balance");
            String username = resultSet.getString("username");

            players.add(new OptEcoPlayerInstance(uuid, balance, username));
        }
        // Return a list
        return players;
    }

    /**
     * Retrieve a player data by a unique ID.
     *
     * @param uuid a uuid to look up
     * @return nullable optional which contains first value of that player.
     * @throws ExecutionException   an exception causes when submit Future.
     * @throws InterruptedException an exception causes when the submission is interrupted.
     */
    public Optional<OptEcoPlayer> getPlayerByUUID(@NotNull UUID uuid)
            throws ExecutionException, InterruptedException, SQLException {
        // Request look up user data
        ResultSet responseSet = plugin.getExecutorService().submit(() -> DatabaseQueryBuilder
                .newInstance(plugin.getDatabaseHoster())
                .selectAll(userTableName).criteria(
                        CriteriaBuilder
                                .newInstance()
                                .newField(CriteriaField.equal("uuid"))
                ).executeQuery(uuid.toString())).get();

        // Fetch data as list
        List<OptEcoPlayer> players = deserializePlayersResponse(responseSet);

        // Return first value as optional
        return Optional.ofNullable(players.size() == 0
                ? null
                : players.get(0)
        );
    }

    /**
     * Insert new player into database by {@link OptEcoPlayer} object.
     *
     * @param player an {@link OptEcoPlayer} object to put into database.
     * @return response that system was added or not. (true/false)
     * @throws ExecutionException   an exception causes when submit Future.
     * @throws InterruptedException an exception causes when the submission is interrupted.
     */
    public boolean addPlayer(@NotNull OptEcoPlayer player) throws ExecutionException, InterruptedException {
        return plugin.getExecutorService().submit(() -> DatabaseQueryBuilder
                .newInstance(plugin.getDatabaseHoster())
                .insert(userTableName)
                .values("uuid", "balance", "username")
                .executeUpdate(
                        player.getUniqueId(),
                        player.getBalance(),
                        player.getUsername()
                )).get() == 1;
    }

}
