package com.playernguyen.optecoprime.placeholder;

import java.util.concurrent.ExecutionException;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import com.playernguyen.optecoprime.utils.NumberUtil.FlexibleNumber;
import com.playernguyen.optecoprime.utils.SenderUtil.Teller;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

/**
 * Imports PlaceholderAPI plugin to register a plugin
 */
public class OptEcoPlaceholder extends PlaceholderExpansion {
    private OptEcoPrime plugin;

    public OptEcoPlaceholder(OptEcoPrime plugin) {

        this.plugin = plugin;
        Teller.init(Bukkit.getConsoleSender()).next("[OptEcoPrime] &aHooking with &6PlaceholderAPI"); 
        // Register this placeholder
        this.register();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getIdentifier() {
        return plugin.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {

        /**
         * optecoprime_balance: return a current balance of player a balance is flexible
         */
        if (params.equalsIgnoreCase("balance")) {
            OptEcoPlayer _player;
            try {
                _player = plugin.getPlayerManager().getPlayer(player.getUniqueId());
                return new FlexibleNumber(_player.getBalance()).toString();
            } catch (ExecutionException | InterruptedException e) {

                e.printStackTrace();
            }
            return null;
        }

        return super.onRequest(player, params);
    }

}
