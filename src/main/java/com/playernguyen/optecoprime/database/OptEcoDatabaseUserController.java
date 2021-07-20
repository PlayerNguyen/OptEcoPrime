package com.playernguyen.optecoprime.database;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.events.OptEcoBalanceChangeEvent;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import com.playernguyen.optecoprime.players.OptEcoPlayerInstance;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import com.playernguyen.pndb.sql.query.CriteriaBuilder;
import com.playernguyen.pndb.sql.query.CriteriaField;
import com.playernguyen.pndb.sql.query.DatabaseQueryBuilder;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

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
		this.userTableName = plugin.getSettingConfiguration().get(SettingConfigurationModel.DATABASE_TABLE_PREFIX)
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
			// String username = resultSet.getString("username");

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
	 * @throws Exception an exception from describeAsync
	 * @see com.playernguyen.optecoprime.trackers.OptEcoTrackers#describeAsync(String,
	 *      Callable)
	 */
	private List<OptEcoPlayer> lookUpByUUID(@NotNull UUID uuid) throws Exception {
		// Request look up user data
		ResultSet responseSet = DatabaseQueryBuilder.newInstance(plugin.getDatabaseHoster()).selectAll(userTableName)
				.criteria(CriteriaBuilder.newInstance().newField(CriteriaField.equal("uuid")))
				.executeQuery(uuid.toString());

		// Fetch data as list then return
		return deserializePlayersResponse(responseSet);
	}

	/**
	 * Retrieve a player data by a unique ID.
	 *
	 * @param uuid a uuid to look up
	 * @return nullable optional which contains first value of that player.
	 * @throws Exception an exception from describeAsync
	 * @see com.playernguyen.optecoprime.trackers.OptEcoTrackers#describeAsync(String,
	 *      Callable)
	 */
	public Optional<OptEcoPlayer> getPlayerByUUID(@NotNull UUID uuid) throws Exception {
		// Look up a player
		List<OptEcoPlayer> players = lookUpByUUID(uuid);
		// Return first value as optional
		return Optional.ofNullable(players.size() == 0 ? null : players.get(0));
	}

	/**
	 * Insert new player into database by {@link OptEcoPlayer} object.
	 *
	 * @param player an {@link OptEcoPlayer} object to put into database.
	 * @return response that system was added or not. (true/false)
	 */
	public boolean addPlayer(@NotNull UUID uuid, double balance) throws Exception {
		return DatabaseQueryBuilder.newInstance(plugin.getDatabaseHoster()).insert(userTableName)
				.values("uuid", "balance").executeUpdate(uuid.toString(), balance) == 1;
	}

	/**
	 * Look up for the existence of that player.
	 *
	 * @param uuid a uuid to look up that player are existed
	 * @return true when player existed, false otherwise.
	 * @throws Exception an exception from describeAsync
	 * @see com.playernguyen.optecoprime.trackers.OptEcoTrackers#describeAsync(String,
	 *      Callable)
	 */
	public boolean hasPlayer(@NotNull UUID uuid) throws Exception {
		return lookUpByUUID(uuid).size() != 0;
	}

	/**
	 * Update new persist data of player.
	 *
	 * @param player a player to update
	 * @throws Exception an exception from describeAsync
	 * @see com.playernguyen.optecoprime.trackers.OptEcoTrackers#describeAsync(String,
	 *      Callable)
	 */
	public void updatePlayer(UUID uuid, double balance) throws Exception {
		OptEcoPlayer persistedPlayer = this.getPlayerByUUID(uuid).orElseThrow(() -> new NullPointerException(
				"cannot update non-existed database player causes player not found " + uuid));

		// Start to update
		int updateStatus = new DatabaseQueryBuilder(plugin.getDatabaseHoster()).update(userTableName, "balance")
				.criteria("uuid=?").executeUpdate(balance, uuid.toString());

		// Unexpected error
		if (updateStatus == 0) {
			throw new IllegalStateException("cannot update player");
		}

		// Call event after update
		Bukkit.getPluginManager().callEvent(
				new OptEcoBalanceChangeEvent(plugin, persistedPlayer, persistedPlayer.getBalance(), balance));

		plugin.getTrackers().describeNothing(
				String.format("Update &c[%s] &m%s &8-> &e%s", uuid, persistedPlayer.getBalance(), balance));
	}

	/**
	 * Update player ignore the existence of player in database. If player are not
	 * found in database, create a new one.
	 * 
	 * @param player a player data.
	 * @throws Exception an exception
	 */
	public void updatePlayerIgnoreNull(UUID uuid, double balance) throws Exception {
		OptEcoPlayer persistedPlayer = this.getPlayerByUUID(uuid).orElse(null);

		// Whether null, initialize new player
		if (persistedPlayer == null) {
			addPlayer(uuid, balance);
		}

		// Start to update
		int updateStatus = new DatabaseQueryBuilder(plugin.getDatabaseHoster()).update(userTableName, "balance")
				.criteria("uuid=?").executeUpdate(balance, uuid.toString());

		// Unexpected error
		if (updateStatus == 0) {
			throw new IllegalStateException("cannot update player");
		}

		// Call event after update
		Bukkit.getPluginManager().callEvent(
				new OptEcoBalanceChangeEvent(plugin, persistedPlayer, persistedPlayer.getBalance(), balance));

		plugin.getTrackers().describeNothing(
				String.format("Update &c[%s] &m%s &8-> &e%s", uuid, persistedPlayer.getBalance(), balance));
	}

}
