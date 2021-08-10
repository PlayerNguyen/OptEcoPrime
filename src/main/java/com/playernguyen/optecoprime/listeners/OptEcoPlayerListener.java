package com.playernguyen.optecoprime.listeners;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.events.OptEcoBalanceChangeEvent;
import com.playernguyen.optecoprime.languages.LanguageConfigurationModel;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import com.playernguyen.optecoprime.utils.SenderUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * A listener to handle event of Bukkit
 */
public class OptEcoPlayerListener implements Listener {
    /**
     * A plugin instance
     */
    private final OptEcoPrime plugin;

    /**
     * A constructor with plugin instance
     *
     * @param plugin a OptEcoPrime instance
     */
    public OptEcoPlayerListener(OptEcoPrime plugin) {
        this.plugin = plugin;
    }

    /**
     * Invokes API when player join into server.
     *
     * @param event a join event
     * @throws Exception errors
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws Exception {
        // Trying to get player's data from database
        Player joinedPlayer = event.getPlayer();

        // This player has not joined before, initialize new player
        if (!plugin.getUserController().hasPlayer(joinedPlayer.getUniqueId())) {
            // Add new player (initial) bares current begin balance
            plugin.getUserController().addPlayer(joinedPlayer.getUniqueId(),
                    plugin.getSettingConfiguration().get(SettingConfigurationModel.USER_BEGINNING_POINT).asDouble());
        }

        // Add this into a manager
        plugin.getPlayerManager().add(joinedPlayer.getUniqueId());
    }

    /**
     * When players disconnecting to server, remove using storage
     *
     * @param event a quit event
     */
    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player disconnectingPlayer = event.getPlayer();

        // Remove a cache storage
        plugin.getPlayerManager().remove(disconnectingPlayer.getUniqueId());
    }

    @EventHandler
    public void onPlayerBalanceUpdate(OptEcoBalanceChangeEvent event) {
        Player player = Bukkit.getPlayer(event.getPlayer().getUniqueId());

        // Not found a player
        if (player == null) {
            return;
        }

        // Send to a player who was updated
        SenderUtil.Teller.init(player)
                .next(plugin
                        .getLanguageConfiguration()
                        .getWithPrefix(LanguageConfigurationModel.EVENT_UPDATE_BALANCE_MESSAGE)
                        .changeFlex("%amount%", event.getCurrentBalance())
                        .toString()
                );


        // Console print out whether player enable this section
        if (plugin.getSettingConfiguration().get(SettingConfigurationModel.ADMINISTRATOR_LOGGING_UPDATE).asBoolean()) {
            SenderUtil.Teller.init(Bukkit.getConsoleSender())
                    .next(String.format("* &a%s [&7%s&a]", player.getName(), player.getUniqueId()))
                    .next(String.format("  &c%s &7-> &a%s", event.getRecentBalance(), event.getCurrentBalance()));
        }
    }
}
