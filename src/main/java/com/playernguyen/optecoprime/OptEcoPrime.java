package com.playernguyen.optecoprime;

import com.playernguyen.optecoprime.settings.SettingConfiguration;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import com.playernguyen.pndb.sql.hoster.DatabaseHoster;
import com.playernguyen.pndb.sql.sqlite.DatabaseHosterSQLite;
import com.playernguyen.pndb.sql.sqlite.DatabaseOptionsSQLite;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class OptEcoPrime extends JavaPlugin {

    private SettingConfiguration settingConfiguration;
    private DatabaseHoster databaseHoster;

    @Override
    public void onEnable() {
        try {
            setupConfiguration();
            setupDatabase();
        } catch (Exception e) {
            // Handle error. Of course, disable the plugin
            e.printStackTrace();
        }
    }

    private void setupDatabase() {
        String persistDatabaseType = this.getSettingConfiguration().get(SettingConfigurationModel.DATABASE_TYPE).asString();
        if (persistDatabaseType.equalsIgnoreCase("sqlite")) {
            File sqliteFile = new File(this.getDataFolder(), this
                    .getSettingConfiguration()
                    .get(SettingConfigurationModel.DATABASE_SQLITE_FILE_NAME)
                    .asString());
            DatabaseOptionsSQLite optionsSQLite = new DatabaseOptionsSQLite(sqliteFile.getAbsolutePath());
            this.databaseHoster = new DatabaseHosterSQLite(
                    optionsSQLite
            );


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

    /**
     * @return a configuration of settings
     */
    public SettingConfiguration getSettingConfiguration() {
        return settingConfiguration;
    }
}
