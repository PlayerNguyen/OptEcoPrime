package com.playernguyen.optecoprime.players;

import java.util.UUID;

/**
 * Default instance of player persist player data
 */
public class OptEcoPlayerInstance implements OptEcoPlayer {
    private final UUID uniqueId;
    private final double balance;
    private final String username;

    public OptEcoPlayerInstance(UUID uniqueId, double balance, String username) {
        this.uniqueId = uniqueId;
        this.balance = balance;
        this.username = username;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
