package com.playernguyen.optecoprime.points;

import com.playernguyen.optecoprime.OptEcoPrimeMockTester;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import org.bukkit.entity.Player;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class OptEcoPrimePointsTest extends OptEcoPrimeMockTester {

    @Test
    public void containsPlayerInPlayerManager() throws ExecutionException, InterruptedException {
        Player player = server.addPlayer("Notch");
        Assert.assertNotNull(plugin.getPlayerManager().getPlayer(player.getUniqueId()));
    }

    @Test
    public void shouldThrowNotNull() throws ExecutionException, InterruptedException {
        Assert.assertThrows(NullPointerException.class,
                () -> plugin.getPlayerManager().getPlayer(UUID.randomUUID()));
    }


    @Test
    @Ignore("not ready yet")
    public void onPlayerJoinWithStartedBalance() throws ExecutionException, InterruptedException {
        // Create a fake player
        Player player = server.addPlayer("Notch");
        // Get balance
        double balance = plugin.getPlayerManager().getPlayer(player.getUniqueId()).getBalance();
        // Whether balance is equal to the started point with delta (offset) 0
        Assert.assertEquals(balance,
                plugin.getSettingConfiguration().get(SettingConfigurationModel.USER_BEGINNING_POINT).asDouble(),
                0
        );
    }


}
