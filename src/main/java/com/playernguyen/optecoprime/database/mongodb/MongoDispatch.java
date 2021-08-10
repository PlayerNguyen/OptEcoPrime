package com.playernguyen.optecoprime.database.mongodb;

import com.mongodb.MongoClient;
import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;

import java.util.function.Consumer;

/**
 * A custom dispatch for MongoDB
 */
public class MongoDispatch {
    /**
     * Instace plugin class
     */
    private final OptEcoPrime plugin;

    /**
     * Constructor of this class
     *
     * @param plugin a plugin
     */
    public MongoDispatch(OptEcoPrime plugin) {
        this.plugin = plugin;
    }

    /**
     * Retrieves a MongoClient database.
     *
     * @param useCallback a use callback to do something here.
     */
    public void getClient(Consumer<MongoClient> useCallback) {
        try (MongoClient client = new MongoClient(
                plugin.getSettingConfiguration().getString(SettingConfigurationModel.DATABASE_MONGODB_HOST),
                plugin.getSettingConfiguration().get(SettingConfigurationModel.DATABASE_MONGODB_PORT).asInt()
        )) {
            useCallback.accept(client);
        }
    }

}
