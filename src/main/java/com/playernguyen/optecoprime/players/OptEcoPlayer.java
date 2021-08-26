package com.playernguyen.optecoprime.players;

import java.util.UUID;

/**
 * An interface represents player of OptEcoPrime plugin
 */
public interface OptEcoPlayer {

    /**
     * An unique id reference to player unique id
     *
     * @return a unique id of player
     */
    UUID getUniqueId();

    /**
     * Balance of player, or currency of it.
     *
     * @return a current point that player had.
     */
    double getBalance();

    /**
     * Set new balance of player.
     *
     * @param balance a new balance of that player.
     */
    void setBalance(double balance);

    /**
     * An expires time of this player object, to check a player
     * object is out-of-date or not.
     *
     * @return a last update long
     */
    long getLastUpdate();

    /**
     * Set a new expires for player
     *
     * @param lastUpdate new expires time for
     */
    void setLastUpdate(long lastUpdate);

}
