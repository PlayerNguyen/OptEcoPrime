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
     * Representing Minecraft username, for developer who want to build no-uuid API
     *
     * @return a stored username provid
     */
    String getUsername();

}
