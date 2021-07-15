package com.playernguyen.optecoprime.languages;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.configurations.ConfigurationAbstract;

/**
 * Language configuration object.
 */
public class LanguageConfiguration extends ConfigurationAbstract<LanguageConfigurationModel> {
    private static final String FILE_NAME = "Language.yml";
    private static final String DIRECTORY_FILE = "Language";

    public LanguageConfiguration(OptEcoPrime plugin) throws Exception {
        super(plugin,
                FILE_NAME,
                LanguageConfigurationModel.class,
                DIRECTORY_FILE);
    }
}
