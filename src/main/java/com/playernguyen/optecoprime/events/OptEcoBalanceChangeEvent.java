package com.playernguyen.optecoprime.events;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import com.playernguyen.optecoprime.utils.SenderUtil;
import org.bukkit.Bukkit;

/**
 * An event that triggers when player balance is increased or decreased.
 * <b>Cannot set unchange balance (recent and current balance are equivalent) </b>
 */
public class OptEcoBalanceChangeEvent extends OptEcoEventAbstract {
    /**
     * An old player balance before change
     */
    private final double recentBalance;
    /**
     * A new player balance afterwards.
     */
    private final double currentBalance;

    /**
     * A target of this changing.
     */
    private final OptEcoPlayer player;

    /**
     * The default constructor is defined for cleaner code. This constructor
     * assumes the event is synchronous.
     *
     * @param plugin an OptEcoPrime plugin
     * @param player a new player, after modified
     * @param recentBalance old player balance before change
     * @param currentBalance new player balance afterwards.
     */
    public OptEcoBalanceChangeEvent(OptEcoPrime plugin,
                                    OptEcoPlayer player,
                                    double recentBalance,
                                    double currentBalance) {
        super(true, plugin);
        this.player = player;
        this.recentBalance = recentBalance;
        this.currentBalance = currentBalance;

        // Recent and current balance cannot be equivalent
        if (recentBalance == currentBalance) {
            SenderUtil.Teller.init(Bukkit.getConsoleSender()).next(String.valueOf(recentBalance)).next(String.valueOf(currentBalance));
            throw new IllegalStateException("recent and current balance cannot be equivalent");
        }
    }

    /**
     * A player who has changed.
     *
     * @return a player who change their balance
     */
    public OptEcoPlayer getPlayer() {
        return player;
    }

    /**
     * A recent balance, before change balance.
     *
     * @return a recent balance
     */
    public double getRecentBalance() {
        return recentBalance;
    }

    /**
     * A current balance, after changing balance.
     *
     * @return a current balance that player is having.
     */
    public double getCurrentBalance() {
        return currentBalance;
    }

    /**
     * Check if after change the status is increased by compare recent
     * balance is lower than current balance.
     *
     * @return true if is increase, false otherwise.
     */
    public boolean isIncrease() {
        return (recentBalance < currentBalance);
    }

    /**
     * Check if after change the status is increased by compare recent
     * balance is higher than current balance.
     *
     * Invertion of {@link #isIncrease()}
     *
     * @return true if is decrease, false otherwise.
     */
    public boolean isDecrease() {
        return !isIncrease();
    }

}
