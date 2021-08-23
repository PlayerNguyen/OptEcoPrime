package com.playernguyen.optecoprime.dependencies;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.languages.LanguageConfigurationModel;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import com.playernguyen.optecoprime.utils.NumberUtil.FlexibleNumber;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Imports PlaceholderAPI plugin to register a plugin
 */
public class OptEcoPlaceholder extends PlaceholderExpansion {
    private final OptEcoPrime plugin;

    public OptEcoPlaceholder(OptEcoPrime plugin) {

        this.plugin = plugin;
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

        // optecoprime_balance: return a current balance of player a balance is flexible
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

        // optecoprime_currency_symbol: return a currency symbol which configured in Setting.yml
        if (params.equalsIgnoreCase("currency_symbol")) {
            return plugin.getLanguageConfiguration().get(LanguageConfigurationModel.CURRENCY_SYMBOL).asString();
        }
        // optecoprime_leaderboard_n_option with n is index.
        //  and option = balance or name or uuid
        if (params.contains("leaderboard_")) {
            String[] pieces = params.split("_");

            // Index not found
            if (pieces.length < 3) {
                return "Index not found";
            }
            // Extract and receives
            try {
                int index = Integer.parseInt(pieces[1]);

                // Positive value
                if (index < 0) {
                    return "n/a";
                }

                // Find the player has highest point
                Optional<OptEcoPlayer> highestBalancePlayer = plugin
                        .getUserController()
                        .getHighestBalancePlayer(index);

                // Not found player
                if (!highestBalancePlayer.isPresent()) {
                    return "n/a";
                }

                // Options switch
                // UUID
                if (pieces[2].equalsIgnoreCase("uuid")) {
                    return highestBalancePlayer.get().getUniqueId().toString();
                }

                // username
                if (pieces[2].equalsIgnoreCase("username")) {
                    return Bukkit.getOfflinePlayer(highestBalancePlayer.get().getUniqueId()).getName();
                }

                // Return a point
                return new FlexibleNumber(highestBalancePlayer.get().getBalance()).toString();

            } catch (Exception e) {
                // Print stack trance
                e.printStackTrace();
                // Invalid index
                return "Invalid index " + pieces[2];
            }

        }

        return super.onRequest(player, params);
    }

    /**
     * Requests and transforms a string includes replacement placeholder by
     * {@link PlaceholderAPI#setPlaceholders(OfflinePlayer, String)}. Return data
     * whether a plugin is not registered
     *
     * @param player a target to set
     * @param data   a data as string to transform
     * @return a transformed text
     */
    public static String requestPlaceholder(@NotNull OfflinePlayer player, @NotNull String data) {
        if (Bukkit.getPluginManager().getPlugin(OptEcoPrime.PLUGIN_PLACEHOLDER_API_NAME) == null) {
            return data;
        }
        return PlaceholderAPI.setPlaceholders(player, data);
    }

}
