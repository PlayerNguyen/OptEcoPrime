package com.playernguyen.optecoprime.settings;

import com.playernguyen.optecoprime.configurations.ConfigurationAbstract;
import org.bukkit.plugin.Plugin;

public class SettingConfiguration extends ConfigurationAbstract<SettingConfigurationModel> {

    public static final String FILE_NAME = "Config.yml";
    public static final String PARENT_FOLDER_NAME = "Settings";

    public SettingConfiguration(Plugin plugin) throws Exception {
        super(plugin,
                FILE_NAME,
                SettingConfigurationModel.class,
                PARENT_FOLDER_NAME);
    }
}
