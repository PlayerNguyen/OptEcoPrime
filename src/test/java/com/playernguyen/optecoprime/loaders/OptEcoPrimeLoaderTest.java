package com.playernguyen.optecoprime.loaders;

import com.playernguyen.optecoprime.OptEcoPrimeMockTester;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class OptEcoPrimeLoaderTest extends OptEcoPrimeMockTester {


    /**
     * Check whether the configuration file was created
     */
    @Test
    public void containsConfigurationFileWhenInitialized() {
        Assert.assertTrue("Database folder existence", this.plugin.getDataFolder().exists());
//        System.out.println(Arrays.toString(plugin.getDataFolder().list()));
        Assert.assertTrue("Config.yml folder existence", plugin
                .getSettingConfiguration()
                .getConfigurationFile()
                .exists());
    }

    /**
     * Check whether the language file is existed
     */
    @Test
    public void containsLanguageFileWhenInitialized() {
        Assert.assertTrue("Database folder existence", plugin.getDataFolder().exists());
        Assert.assertTrue(
                "Language.yml existence",
                plugin.getLanguageConfiguration().getConfigurationFile().exists()
        );
    }

    /**
     * Check whether contains SQLite files after setup
     *
     * @throws Exception an exception when app crashed
     */
    @Test
    public void containsSQLiteFileWhenInitialized() throws Exception {
        // Set settings to sqlite database system
        plugin.getSettingConfiguration()
                .get(SettingConfigurationModel.DATABASE_TYPE)
                .setValues("sqlite");
        plugin.getSettingConfiguration().getDreamYaml().save();

        // Assertions
        Assert.assertTrue("Database folder existence", plugin.getDataFolder().exists());

        File sqliteFile = new File(plugin.getDataFolder(), plugin.getSettingConfiguration()
                .getString(SettingConfigurationModel.DATABASE_SQLITE_FILE_NAME)
        );

        Assert.assertTrue(
                "data.sqlite existence",
                sqliteFile.exists()
        );
    }


}
