package com.playernguyen.optecoprime;

import com.playernguyen.optecoprime.database.OptEcoDatabaseUserController;
import com.playernguyen.optecoprime.listeners.OptEcoPlayerListener;
import com.playernguyen.optecoprime.settings.SettingConfiguration;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import com.playernguyen.optecoprime.tankers.OptEcoUserTanker;
import com.playernguyen.optecoprime.trackers.OptEcoTrackers;
import com.playernguyen.pndb.sql.hoster.DatabaseHoster;
import com.playernguyen.pndb.sql.query.DatabaseQueryBuilder;
import com.playernguyen.pndb.sql.sqlite.DatabaseHosterSQLite;
import com.playernguyen.pndb.sql.sqlite.DatabaseOptionsSQLite;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class OptEcoPrime extends JavaPlugin {

    private static final int THREAD_POOL_SIZE = 3;

    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    private SettingConfiguration settingConfiguration;
    private OptEcoTrackers trackers;
    private DatabaseHoster databaseHoster;
    private OptEcoDatabaseUserController databaseUserController;
    private OptEcoUserTanker userTanker;

    @Override
    public void onEnable() {
        try {
            setupConfiguration();
            setupTrackers();
            setupDatabase();
            setupController();
            setupTankers();
            setupListener();
        } catch (Exception e) {
            // Handle error. Of course, disable the plugin
            e.printStackTrace();
        }
    }

    private void setupListener() {
        List<Listener> listenerList = new ArrayList<>();
        // Append here
        listenerList.add(new OptEcoPlayerListener(this));

        // Register all listeners
        listenerList.forEach(e -> {
            Bukkit.getPluginManager().registerEvents(e, this);
        });
    }

    private void setupTankers() {
        if (this.userTanker == null) {
            this.userTanker = new OptEcoUserTanker(this);
        } else {
            this.userTanker.getCollection().clear();
        }
    }

    private void setupController() {
        if (this.databaseUserController == null) {
            this.databaseUserController = new OptEcoDatabaseUserController(this);
        }
    }

    private void setupTrackers() {
        if (this.trackers == null) {
            this.trackers = new OptEcoTrackers(this);
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

            // Set up tables by run async task
            Bukkit.getScheduler().runTaskAsynchronously(this, () ->
                    trackers.describeDatabase("Access and create users table", () -> {
                        try {
                            // Set up user table
                            DatabaseQueryBuilder.newInstance(getDatabaseHoster())
                                    .executeCustomUpdate(String.format("CREATE TABLE IF NOT EXISTS %s_users (" +
                                                    "uuid varchar UNIQUE PRIMARY KEY NOT NULL," +
                                                    "balance double NOT NULL," +
                                                    "username varchar(255) NOT NULL)",
                                            this
                                                    .getSettingConfiguration()
                                                    .get(SettingConfigurationModel.DATABASE_TABLE_PREFIX)
                                                    .asString()
                                            )
                                    );
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }));

            return;
        }
        // Unsupport type
        throw new IllegalStateException(String.format(
                "cannot found database type %s",
                persistDatabaseType
        ));
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

    /**
     * Database controller
     *
     * @return a database controller object
     */
    public DatabaseHoster getDatabaseHoster() {
        return databaseHoster;
    }

    /**
     * Tracker, a debug tools for OptEcoPrime
     *
     * @return a tracker instanced object
     */
    public OptEcoTrackers getTrackers() {
        return trackers;
    }

    /**
     * Database user controller to control player information from database to server
     *
     * @return a database user controller instance
     */
    public OptEcoDatabaseUserController getDatabaseUserController() {
        return databaseUserController;
    }

    /**
     * An executor service to maintain a multithread process.
     *
     * @return an executor service (thread pool)
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     *
     * @return a user tanker caches many users information
     */
    public OptEcoUserTanker getUserTanker() {
        return userTanker;
    }
}
