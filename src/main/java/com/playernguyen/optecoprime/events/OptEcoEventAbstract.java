package com.playernguyen.optecoprime.events;

import com.playernguyen.optecoprime.OptEcoPrime;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class OptEcoEventAbstract extends Event implements Cancellable {

    private final OptEcoPrime plugin;
    private boolean cancelled = false;

    /**
     * The default constructor is defined for cleaner code. This constructor
     * assumes the event is synchronous.
     *
     * @param plugin an OptEcoPrime plugin
     */
    public OptEcoEventAbstract(OptEcoPrime plugin) {
        this.plugin = plugin;
    }

    public OptEcoEventAbstract(boolean isAsync, OptEcoPrime plugin) {
        super(isAsync);
        this.plugin = plugin;
    }

    private static final HandlerList handerList = new HandlerList();

    /**
     * Compulsory method for event
     *
     * @return a handler list
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handerList;
    }

    /**
     * Compulsory method for event
     *
     * @return a handler list
     */
    public static HandlerList getHandlerList() {
        return handerList;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * An instance of OptEco plugin, to get some methods to execute.
     *
     * @return OptEco plugin
     */
    public OptEcoPrime getPlugin() {
        return plugin;
    }
}
