package com.playernguyen.optecoprime.tankers;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.players.OptEcoPlayer;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * A user tanker caches users' information such as UUID, balance,
 * ... which retrieved from database
 */
public class OptEcoUserTanker extends OptEcoTanker<OptEcoPlayer> {
    /**
     * Plugin field
     */
    private final OptEcoPrime plugin;

    /**
     * A user tanker constructor
     *
     * @param plugin plugin object to extend some methods
     */
    public OptEcoUserTanker(OptEcoPrime plugin) {
        this.plugin = plugin;
    }

    /**
     * Add player into tanker
     *
     * @param player a player to add
     */
    public void add(OptEcoPlayer player) {
        plugin.getTrackers().describeTanker("add player " + player.getUniqueId(), () -> {
            this.getCollection().add(player);
        });
    }

    /**
     * Remove player out of collection in tanker
     *
     * @param player a player to remove
     * @return a status of removed or not
     * @throws Exception an exceptions causes when trackers cannot returns value
     */
    public boolean remove(OptEcoPlayer player) throws Exception {
        return plugin.getTrackers().describeTanker("remove player by predicate ",
                () -> this.getCollection().remove(player));
    }

    /**
     * Remove items by using predicate.
     *
     * @param predicate a predicate object, as a logic condition to remove.
     */
    public void removeIf(Predicate<OptEcoPlayer> predicate) {
        plugin.getTrackers().describeTanker("remove player by predicate ", () -> {
            this.getCollection().removeIf(predicate);
        });
    }

    /**
     * Get player from tanker who was stored in the collection.
     *
     * @param predicate a predicate to get
     * @return an optional contains player or null.
     * @throws Exception an exceptions causes when trackers cannot returns value
     */
    public Optional<OptEcoPlayer> get(Predicate<OptEcoPlayer> predicate) throws Exception {
        return plugin.getTrackers().describeTanker("get player data from user tanker",
                () -> this.getCollection().stream().filter(predicate).findFirst());
    }
}
