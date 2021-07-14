package com.playernguyen.optecoprime;

import com.playernguyen.optecoprime.settings.SettingConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class OptEcoPrime extends JavaPlugin {

    private SettingConfiguration settingConfiguration;

    @Override
    public void onEnable() {
        try {
            setupConfiguration();
        } catch (Exception e) {
            // Handle error. Of course, disable the plugin
            e.printStackTrace();
        }
    }

    private void setupConfiguration() throws Exception {
        if (settingConfiguration == null) {
            this.settingConfiguration = new SettingConfiguration(this);
        } else {
            this.settingConfiguration.getDreamYaml().saveAndLoad();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
