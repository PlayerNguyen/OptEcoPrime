package com.playernguyen.optecoprime.database.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        // Set a logger
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(
                plugin.getSettingConfiguration().get(SettingConfigurationModel.DEBUG)
                        .asBoolean()
                        ? Level.WARNING
                        : Level.SEVERE
        );
    }

    /**
     * Retrieves a MongoClient database.
     *
     * @param useCallback a use callback to do something here.
     */
    public void getClient(Consumer<MongoClient> useCallback) {
        try (MongoClient client = new MongoClient(
                new MongoClientURI(plugin.getSettingConfiguration()
                        .getString(SettingConfigurationModel.DATABASE_MONGODB_URI)
                )
        )) {
            useCallback.accept(client);
        }
    }

}
