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
        OptEcoPlayer retrievePlayer = plugin.getUserController().getPlayerByUUID(joinedPlayer.getUniqueId())
                .orElse(null);

        // If player has not found on server, initialize a new player
        if (retrievePlayer == null) {
            retrievePlayer = new OptEcoPlayerInstance(joinedPlayer.getUniqueId(),
                    plugin.getSettingConfiguration().get(SettingConfigurationModel.USER_BEGINNING_POINT).asDouble());

            // Add new player into database
            plugin.getUserController().addPlayer(retrievePlayer.getUniqueId(), retrievePlayer.getBalance());
        }

        // Then, put it to the cache storage
        plugin.getPlayerManager().add(retrievePlayer);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player disconnectingPlayer = event.getPlayer();

        // Remove a cache storage
        plugin.getPlayerManager().remove(disconnectingPlayer.getUniqueId());

    }

}
