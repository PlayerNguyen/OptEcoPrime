package com.playernguyen.optecoprime.api;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.exceptions.PlayerNotFoundException;
import com.playernguyen.optecoprime.languages.LanguageConfigurationModel;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * @param unique a unique id of that player
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
     * @param unique a unique id of that player
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
     * @param unique a unique id of that player
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

    /**
     * {@inheritDoc}
     *
     * @return a currency symbol which configured in Settings.yml
     */
    @Override
    public String currencySymbol() {
        return ChatColor.translateAlternateColorCodes(
                '&',
                this
                        .plugin
                        .getLanguageConfiguration()
                        .get(LanguageConfigurationModel.CURRENCY_SYMBOL)
                        .asString()
        );
    }

    /**
     * {@inheritDoc}
     *
     * @param uuid a unique id of that player
     * @return a player in server; otherwise null.
     * @throws Exception an exception to any error
     */
    @Override
    public @Nullable OptEcoPlayer get(@NotNull UUID uuid) throws Exception {
        // Return null whenever not found
        if (!hasPlayer(uuid)) {
            return null;
        }
        // Otherwise, return this player from player manager
        return this.plugin.getPlayerManager().getPlayer(uuid);
    }
}
