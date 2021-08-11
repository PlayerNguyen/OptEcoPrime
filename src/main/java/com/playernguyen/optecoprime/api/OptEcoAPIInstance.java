package com.playernguyen.optecoprime.api;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.exceptions.PlayerNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class OptEcoAPIInstance implements OptEcoAPI {
    private final OptEcoPrime plugin;

    public OptEcoAPIInstance(OptEcoPrime plugin) {
        this.plugin = plugin;
    }

    /**
     * {@inheritDoc}
     *
     * @param uuid an unique id to check
     * @return true whether existed, false otherwise.
     */
    @Override
    public boolean hasPlayer(@NotNull UUID uuid) throws Exception {
        return plugin.getPlayerManager().containsPlayer(uuid)
                || plugin.getUserController().hasPlayer(uuid);
    }

    /**
     * {@inheritDoc}
     *
     * @param unique an unique id of that player
     * @param value  a value to take
     * @throws java.sql.SQLException   sql errors
     * @throws PlayerNotFoundException player not found error
     */
    @Override
    public void take(@NotNull UUID unique, double value) throws Exception {
        if (!hasPlayer(unique)) {
            throw new PlayerNotFoundException(unique);
        }

        this.plugin.getPlayerManager().takePlayerBalance(unique, value);
    }

    /**
     * {@inheritDoc}
     *
     * @param unique an unique id of that player
     * @param value  a value to add
     * @throws java.sql.SQLException   sql errors
     * @throws PlayerNotFoundException player not found error
     */
    @Override
    public void add(@NotNull UUID unique, double value) throws Exception {
        if (!hasPlayer(unique)) {
            throw new PlayerNotFoundException(unique);
        }

        this.plugin.getPlayerManager().addPlayerBalance(unique, value);
    }

    /**
     * {@inheritDoc}
     *
     * @param unique an unique id of that player
     * @param value  a value to set
     * @throws java.sql.SQLException   sql errors
     * @throws PlayerNotFoundException player not found error
     */
    @Override
    public void set(@NotNull UUID unique, double value) throws Exception {
        if (!hasPlayer(unique)) {
            throw new PlayerNotFoundException(unique);
        }

        this.plugin.getPlayerManager().setPlayerBalance(unique, value);
    }


}
