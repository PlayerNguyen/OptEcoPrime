package com.playernguyen.optecoprime.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.events.OptEcoBalanceChangeEvent;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import com.playernguyen.optecoprime.players.OptEcoPlayerInstance;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

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
	private final String userTableName;

	/**
	 * @param plugin a plugin in order to provide some methods
	 */
	public UserControllerSQL(OptEcoPrime plugin) {
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
		plugin.getDispatch().executeQuery(resultSet -> {
			list.addAll(deserializePlayersResponse(resultSet));
		}, String.format("SELECT * FROM %s WHERE uuid=?", userTableName), uuid.toString());
		return list;
	}

	/**
	 * Retrieve a player data by a unique ID.
	 *
	 * @param uuid a uuid to look up
	 * @return nullable optional which contains first value of that player.
	 * @throws SQLException an exception that server cannot querying
	 * @see com.playernguyen.optecoprime.trackers.OptEcoTrackers#describeAsync(String,
	 *      Callable)
	 */
	public Optional<OptEcoPlayer> getPlayerByUUID(@NotNull UUID uuid) throws SQLException {
		// Look up a player by their uuid
		List<OptEcoPlayer> players = this.findByUUID(uuid);
		if (players.size() == 0) {
			throw new IllegalStateException("Not found the first player: size " + players.size());
		}
		// Return first value as optional
		return Optional.of(players.get(0));
	}

	/**
	 * Insert new player into database by {@link OptEcoPlayer} object.
	 *
	 * @param player an {@link OptEcoPlayer} object to put into database.
	 * @return response that system was added or not. (true/false)
	 * @throws SQLException an exception that server cannot querying
	 */
	public boolean addPlayer(@NotNull UUID uuid, double balance) throws SQLException {
		AtomicBoolean status = new AtomicBoolean();
		plugin.getDispatch().executeUpdate((updatedRows) -> {
			status.set(updatedRows == 1);
		}, String.format("INSERT INTO %s (uuid, balance) VALUES (?, ?)", userTableName), uuid.toString(), balance);
		return status.get();
	}

	/**
	 * Look up for the existence of that player.
	 *
	 * @param uuid a uuid to look up that player are existed
	 * @return true when player existed, false otherwise.
	 * @throws SQLException an exception that server cannot querying
	 * 
	 * @see com.playernguyen.optecoprime.trackers.OptEcoTrackers#describeAsync(String,
	 *      Callable)
	 */
	public boolean hasPlayer(@NotNull UUID uuid) throws SQLException {
		return findByUUID(uuid).size() != 0;
	}

	/**
	 * Update new persist data of player.
	 *
	 * @param player a player to update
	 * @throws SQLException         an exception that server cannot querying
	 * @throws NullPointerException not found a player in database
	 * 
	 * @see com.playernguyen.optecoprime.trackers.OptEcoTrackers#describeAsync(String,
	 *      Callable)
	 */
	public void updatePlayer(UUID uuid, double balance) throws NullPointerException, SQLException {
		OptEcoPlayer persistedPlayer = this.getPlayerByUUID(uuid).orElseThrow(() -> new NullPointerException(
				"cannot update non-existed database player causes player not found " + uuid));

		// Start to update
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

}
