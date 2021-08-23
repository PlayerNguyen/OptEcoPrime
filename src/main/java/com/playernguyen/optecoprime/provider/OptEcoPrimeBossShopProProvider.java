package com.playernguyen.optecoprime.provider;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import org.black_ixx.bossshop.pointsystem.BSPointsPlugin;
import org.bukkit.OfflinePlayer;

public class OptEcoPrimeBossShopProProvider extends BSPointsPlugin {

    private final OptEcoPrime plugin;

    public OptEcoPrimeBossShopProProvider(OptEcoPrime plugin) {
        super(plugin.getName(), "opteco");
        // Plugin instances
        this.plugin = plugin;

        // Register to BossShopPro
        this.register();
    }


    @Override
    public double getPoints(OfflinePlayer offlinePlayer) {
        try {
            // Retrieves player
            OptEcoPlayer player = plugin
                    .getApplicationInterface()
                    .get(offlinePlayer.getUniqueId());
            // Whether to null, returns zero
            if (player == null) {
                return 0;
            }
            // Returns a balance of this player
            return player.getBalance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Undefined error
        return -1;
    }

    @Override
    public double setPoints(OfflinePlayer offlinePlayer, double v) {
        try {
            plugin.getApplicationInterface()
                    .set(offlinePlayer.getUniqueId(), v);
            return v;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public double takePoints(OfflinePlayer offlinePlayer, double v) {
        try {
            plugin.getApplicationInterface().take(offlinePlayer.getUniqueId(), v);
            return v;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public double givePoints(OfflinePlayer offlinePlayer, double v) {
        try {
            plugin.getApplicationInterface().add(offlinePlayer.getUniqueId(), v);
            return v;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean usesDoubleValues() {
        return true;
    }
}
