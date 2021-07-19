package com.playernguyen.optecoprime;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.playernguyen.optecoprime.commands.ExecutorOptEco;
import com.playernguyen.optecoprime.commands.core.CommandRegistryManager;
import com.playernguyen.optecoprime.database.OptEcoDatabaseUserController;
import com.playernguyen.optecoprime.languages.LanguageConfiguration;
import com.playernguyen.optecoprime.listeners.OptEcoPlayerListener;
import com.playernguyen.optecoprime.settings.SettingConfiguration;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import com.playernguyen.optecoprime.tankers.OptEcoPlayerManager;
import com.playernguyen.optecoprime.trackers.OptEcoTrackers;
import com.playernguyen.pndb.sql.hoster.DatabaseHoster;
import com.playernguyen.pndb.sql.query.DatabaseQueryBuilder;
import com.playernguyen.pndb.sql.sqlite.DatabaseHosterSQLite;
import com.playernguyen.pndb.sql.sqlite.DatabaseOptionsSQLite;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class OptEcoPrime extends JavaPlugin {

    private static final int THREAD_POOL_SIZE = 3;

    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    private SettingConfiguration settingConfiguration;
    private OptEcoTrackers trackers;
    private DatabaseHoster databaseHoster;
    private OptEcoDatabaseUserController databaseUserController;
    private OptEcoPlayerManager userStorage;
    private LanguageConfiguration languageConfiguration;
    private CommandRegistryManager commandRegistryManager;

    @Override
    public void onEnable() {
        try {
            setupConfiguration();
            setupTrackers();
            setupLanguage();
            setupDatabase();
            setupController();
            setupStorages();
            setupListener();
            setupCommands();
        } catch (Exception e) {
            // Handle error. Of course, disable the plugin
            e.printStackTrace();
        }
    }

    private void setupCommands()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        if (this.commandRegistryManager == null) {
            this.commandRegistryManager = new CommandRegistryManager();
        } else {
            this.commandRegistryManager.clearAll();
        }

        // Add new command here
        this.commandRegistryManager.addCommand(new ExecutorOptEco(this));

        // Load all commands
        this.commandRegistryManager.load();
    }

    private void setupLanguage() throws Exception {
        if (this.languageConfiguration == null) {
            this.languageConfiguration = new LanguageConfiguration(this);
        } else {
            this.languageConfiguration.getDreamYaml().load();
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

    private void setupStorages() throws NullPointerException, Exception {
        if (this.userStorage == null) {
            this.userStorage = new OptEcoPlayerManager(this);
        } else {
            this.userStorage.clear();

            // Load a player joined game
            for (Player player : Bukkit.getOnlinePlayers()) {
                this.userStorage.add(player.getUniqueId());
            }
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
        String persistDatabaseType = this.getSettingConfiguration().get(SettingConfigurationModel.DATABASE_TYPE)
                .asString();

        if (persistDatabaseType.equalsIgnoreCase("sqlite")) {
            File sqliteFile = new File(this.getDataFolder(),
                    this.getSettingConfiguration().get(SettingConfigurationModel.DATABASE_SQLITE_FILE_NAME).asString());
            DatabaseOptionsSQLite optionsSQLite = new DatabaseOptionsSQLite(sqliteFile.getAbsolutePath());
            this.databaseHoster = new DatabaseHosterSQLite(optionsSQLite);

            // Set up tables by run async task
            Bukkit.getScheduler().runTaskAsynchronously(this,
                    () -> trackers.describeDatabase("Access and create users table", () -> {
                        try {
                            // Set up user table
                            DatabaseQueryBuilder.newInstance(getDatabaseHoster())
                                    .executeCustomUpdate(String.format("CREATE TABLE IF NOT EXISTS %s_users ("
                                            + "uuid varchar UNIQUE PRIMARY KEY NOT NULL" + ",balance double NOT NULL"
                            // + ",username varchar(255) NOT NULL"
                                            + ")",
                                            this.getSettingConfiguration()
                                                    .get(SettingConfigurationModel.DATABASE_TABLE_PREFIX).asString()));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }));

            return;
        }
        // Unsupported type
        throw new IllegalStateException(String.format("cannot found database type %s", persistDatabaseType));
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
     * Database user controller to control player information from database to
     * server
     *
     * @return a database user controller instance
     */
    public OptEcoDatabaseUserController getDatabaseUserController() {
        return databaseUserController;
    }

    /**
     * An executor service to maintain a multi thread process.
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
    public OptEcoPlayerManager getPlayerManager() {
        return userStorage;
    }

    /**
     * 
     * @return a language configuration instance
     */
    public LanguageConfiguration getLanguageConfiguration() {
        return languageConfiguration;
    }
}
