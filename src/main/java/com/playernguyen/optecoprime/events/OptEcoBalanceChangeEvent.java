package com.playernguyen.optecoprime.events;

import com.playernguyen.optecoprime.OptEcoPrime;

/**
 * An event that triggers when player balance is increased or decreased.
 */
public class OptEcoModifyEvent extends OptEcoEventAbstract {
    /**
     * The default constructor is defined for cleaner code. This constructor
     * assumes the event is synchronous.
     *
     * @param plugin an OptEcoPrime plugin
     */
    public OptEcoModifyEvent(OptEcoPrime plugin) {
        super(plugin);
    }
}
