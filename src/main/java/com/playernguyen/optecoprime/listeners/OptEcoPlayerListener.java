package com.playernguyen.optecoprime.listeners;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import com.playernguyen.optecoprime.players.OptEcoPlayerInstance;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class OptEcoPlayerListener implements Listener {

    private final OptEcoPrime plugin;

    public OptEcoPlayerListener(OptEcoPrime plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws Exception {
        // Trying to get player's data from database
        Player joinedPlayer = event.getPlayer();

        if (!plugin.getUserController().hasPlayer(joinedPlayer.getUniqueId())) {
            // Add new player (initial) bares current begin balance
            plugin.getUserController().addPlayer(joinedPlayer.getUniqueId(),
                    plugin.getSettingConfiguration().get(SettingConfigurationModel.USER_BEGINNING_POINT).asDouble());
        }

        // Add this into a manager
        plugin.getPlayerManager().add(joinedPlayer.getUniqueId());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player disconnectingPlayer = event.getPlayer();

        // Remove a cache storage
        plugin.getPlayerManager().remove(disconnectingPlayer.getUniqueId());

    }

}
