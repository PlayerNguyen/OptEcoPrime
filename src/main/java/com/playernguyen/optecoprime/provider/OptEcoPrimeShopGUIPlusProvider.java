package com.playernguyen.optecoprime.provider;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.players.OptEcoPlayer;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.provider.economy.EconomyProvider;
import org.bukkit.entity.Player;

public class OptEcoPrimeShopGUIPlusProvider extends EconomyProvider {

    private final OptEcoPrime plugin;

    public OptEcoPrimeShopGUIPlusProvider(OptEcoPrime plugin, ShopGUIPlusCurrencyPosition position) {
        this.plugin = plugin;
        // Set suffix/prefix for ShopGUIPlus
        if (position == ShopGUIPlusCurrencyPosition.SUFFIX) {
            this.currencySuffix = plugin.getApplicationInterface().currencySymbol();
            return;
        }
        this.currencyPrefix = plugin.getApplicationInterface().currencySymbol();

        // Then register this provider
        ShopGuiPlusApi.registerEconomyProvider(this);
    }

    @Override
    public String getName() {
        return this.plugin.getName();
    }

    @Override
    public double getBalance(Player player) {
        try {
            OptEcoPlayer primePlayer = this
                    .plugin
                    .getApplicationInterface()
                    .get(player.getUniqueId());

            // Not found means zero
            if (primePlayer == null) {
                return 0;
            }
            // Returns balance of player
            return primePlayer.getBalance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Error means -1
        return -1;
    }

    @Override
    public void deposit(Player player, double amount) {
        try {
            // Calls API method
            plugin.getApplicationInterface().add(player.getUniqueId(), amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void withdraw(Player player, double amount) {
        try {
            plugin.getApplicationInterface().take(player.getUniqueId(), amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
