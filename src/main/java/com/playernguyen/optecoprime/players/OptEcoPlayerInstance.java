package com.playernguyen.optecoprime.players;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Default instance of player persist player data
 */
public class OptEcoPlayerInstance implements OptEcoPlayer {
    private final UUID uniqueId;
    // private final String username;
    private long lastUpdate;
    private double balance;

    public OptEcoPlayerInstance(UUID uniqueId, double balance) {
        this.uniqueId = uniqueId;
        this.balance = balance;
        this.lastUpdate = System.currentTimeMillis();
    }

    public OptEcoPlayerInstance(@NotNull OptEcoPlayer player) {
        this.uniqueId = player.getUniqueId();
        this.balance = player.getBalance();
        this.lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void setBalance(double balance) {
        this.balance = balance;
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
    public long getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
