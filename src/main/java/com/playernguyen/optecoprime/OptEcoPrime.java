package com.playernguyen.optecoprime;

import com.playernguyen.dbcollective.Dispatch;
import com.playernguyen.dbcollective.mysql.MySQLHikariDispatch;
import com.playernguyen.dbcollective.sqlite.SQLiteDispatch;
import com.playernguyen.optecoprime.api.OptEcoAPI;
import com.playernguyen.optecoprime.api.OptEcoAPIInstance;
import com.playernguyen.optecoprime.commands.ExecutorOptEco;
import com.playernguyen.optecoprime.commands.core.CommandRegistryManager;
import com.playernguyen.optecoprime.database.UserController;
import com.playernguyen.optecoprime.database.UserControllerMongodb;
import com.playernguyen.optecoprime.database.UserControllerSQL;
import com.playernguyen.optecoprime.database.mongodb.MongoDispatch;
import com.playernguyen.optecoprime.languages.LanguageConfiguration;
import com.playernguyen.optecoprime.listeners.OptEcoPlayerListener;
import com.playernguyen.optecoprime.loggers.ConsoleTeller;
import com.playernguyen.optecoprime.managers.OptEcoPlayerManager;
import com.playernguyen.optecoprime.placeholder.OptEcoPlaceholder;
import com.playernguyen.optecoprime.settings.SettingConfiguration;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import com.playernguyen.optecoprime.trackers.OptEcoTrackers;
import com.playernguyen.optecoprime.updater.OptEcoUpdater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A core for OptEcoPrime plugin. This class registers and handles anything.
 */
public final class OptEcoPrime extends JavaPlugin {
    /**
     * A default PlaceholderAPI name to implements
     */
    public static final String PLUGIN_PLACEHOLDER_API_NAME = "PlaceholderAPI";
    /**
     * Pool size of ExecutorService
     */
    private static final int THREAD_POOL_SIZE = 3;
    /**
     * Statistic id of bStats
     */
    private static final int BSTATS_ID = 12394;
    /**
     * Executor to execute multi thread services.
     */
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    /**
     * A setting configuration, contains many config inside as Config.yml
     */
    private SettingConfiguration settingConfiguration;
    /**
     * A trackers, follows and logs out
     */
    private OptEcoTrackers trackers;
    /**
     * A dispatch class of database, {@link Dispatch} of dbcollective
     */
    private Dispatch dispatch;
    /**
     * User controller uses to control a user information
     */
    private UserController userController;
    /**
     * User storage, contains and caches data information, queries when is out of
     * date
     */
    private OptEcoPlayerManager userStorage;
    /**
     * Language configuration contains language file
     */
    private LanguageConfiguration languageConfiguration;
    /**
     * Registry of executor
     */
    private CommandRegistryManager commandRegistryManager;
    /**
     * Console teller sends a colorful message to console
     */
    private ConsoleTeller consoleTeller;
    /**
     * Check for update service class
     */
    private OptEcoUpdater updater;
    /**
     * Application interface, for developers to interact with OptEcoPrime
     */
    private OptEcoAPI applicationInterface;
    /**
     * Metrics for statistic
     */
    private Metrics metrics;

    /**
     * When plugin enable or start, loading up anything. Any error will stop loading
     * this plugin and disabling this plugin.
     */
    @Override
    public void onEnable() {
        try {
            setupTeller();
            setupConfiguration();
            setupTrackers();
            setupLanguage();
            setupDatabase();
            setupStorages();
            setupListener();
            setupCommands();
            setupHook();
            setupUpdater();
            setupAPI();
            setupStatistic();
            createWatermark();
        } catch (Exception e) {
            // Handle error and then, disable plugin
            e.printStackTrace();

            // Disable plugin
            this.getPluginLoader().disablePlugin(this);
        }
    }

    /**
     * Statistic integrates
     */
    private void setupStatistic() {
        if (this.metrics == null) {
            this.metrics = new Metrics(this, BSTATS_ID);
        }
    }

    /**
     * An API for developers
     */
    private void setupAPI() {
        if (this.applicationInterface == null) {
            this.applicationInterface = new OptEcoAPIInstance(this);
        }
    }

    /**
     * A reload function to reload this plugin
     */
    public void reload() {
        try {
            setupConfiguration();
            setupLanguage();
            setupDatabase();
            setupHook();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a watermark or logo. Making my plugin prettier.
     */
    private void createWatermark() {
        consoleTeller
                .send("&7|-------------------------|")
                .send("&7   &cOptEco&6Prime ")
                .send("&7     &7version&c " + this.getDescription().getVersion())
                .send("&7     &7by &cPlayer_Nguyen ")
                .send("&7|-------------------------|");
    }

    /**
     * Check for new updated version via Github
     *
     * @throws Exception an exception
     */
    private void setupUpdater() throws Exception {
        // Not found an updater, update
        if (updater == null) {
            this.updater = new OptEcoUpdater(this);
        }

        // Check for update
        updater.checkForUpdate((version) -> {
            getConsoleTeller().send("&aNew update released: " + version);
        });
    }

    /**
     * Set up a hook with more plugins
     */
    private void setupHook() {
        this.getConsoleTeller().send("Initializing plugin-hook with other plugins");
        // Placeholder api register
        if (Bukkit.getPluginManager().getPlugin(PLUGIN_PLACEHOLDER_API_NAME) != null)
            new OptEcoPlaceholder(this);
    }

    /**
     * Set up an executor for this plugin
     *
     * @throws IllegalAccessException   illegal access when setup
     * @throws SecurityException        no private access to register Bukkit API
     * @throws NoSuchFieldException     no field in Bukkit API class to register
     * @throws IllegalArgumentException no argument set
     */
    private void setupCommands()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        this.getConsoleTeller().send("Initializing commands and executors");
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

    /**
     * A configuration language
     *
     * @throws Exception any exception will throw
     */
    private void setupLanguage() throws Exception {
        this.getConsoleTeller().send("Initializing language configuration");
        if (this.languageConfiguration == null) {
            this.languageConfiguration = new LanguageConfiguration(this);
        } else {
            this.languageConfiguration.getDreamYaml().load();
        }
    }

    /**
     * Listener for Bukkit
     */
    private void setupListener() {
        List<Listener> listenerList = new ArrayList<>();
        // Append here
        listenerList.add(new OptEcoPlayerListener(this));

        // Register all listeners
        listenerList.forEach(e -> {
            Bukkit.getPluginManager().registerEvents(e, this);
        });
    }

    /**
     * A storage contains data of player as cache
     *
     * @throws NullPointerException not found a class
     * @throws Exception            any exception that will throw
     */
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

    /**
     * A database user controller (to control a database)
     *
     * @param controller a controller
     */
    private void setupUserController(UserController controller) {
        if (this.userController == null) {
            this.userController = controller;
        }
    }

    /**
     * A teller can tell a story for you
     */
    private void setupTeller() {
        if (this.consoleTeller == null) {
            this.consoleTeller = new ConsoleTeller(this);
        }
    }

    /**
     * Set up a tracker which console as debugger tool
     */
    private void setupTrackers() {
        if (this.trackers == null) {
            this.trackers = new OptEcoTrackers(this);
        }
    }

    /**
     * Set up and initialize a database
     *
     * @throws ClassNotFoundException database driver not found
     */
    private void setupDatabase() throws ClassNotFoundException {
        this.getConsoleTeller().send("Invoking configured database type");
        String persistDatabaseType = this.getSettingConfiguration().get(SettingConfigurationModel.DATABASE_TYPE)
                .asString();

        // SQLite initialize
        if (persistDatabaseType.equalsIgnoreCase("sqlite")) {
            this.getConsoleTeller().send("&6Detecting sqlite database type, loading files");
            this.dispatch = new SQLiteDispatch(
                    this.getSettingConfiguration().get(SettingConfigurationModel.DATABASE_SQLITE_FILE_NAME).asString());
            this.dispatch.setVerbose(getSettingConfiguration().get(SettingConfigurationModel.DEBUG).asBoolean());
            this.dispatch.setLogger(this.getLogger());

            // Set up tables by run async task
            Bukkit.getScheduler().runTaskAsynchronously(this,
                    () -> trackers.describeDatabase("Access and create users table", () -> {
                        try {
                            this.getConsoleTeller().send("&rInitializing database stuffs");
                            // Set up user table
                            dispatch.executeUpdate(updatedRow -> {
                            }, String.format(
                                    "CREATE TABLE IF NOT EXISTS %s_users (" + "uuid varchar UNIQUE PRIMARY KEY NOT NULL"
                                            + ",balance double NOT NULL" + ", username varchar )",
                                    this.getSettingConfiguration().get(SettingConfigurationModel.DATABASE_TABLE_PREFIX)
                                            .asString()));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }));

            // setup controller
            setupUserController(new UserControllerSQL(this));
            return;
        }
        // MySQL initialize
        if (persistDatabaseType.equalsIgnoreCase("mysql")) {
            this.getConsoleTeller().send("&6Detecting MySQL database type, initializing connection");

            this.dispatch = new MySQLHikariDispatch(
                    this.getSettingConfiguration().getString(SettingConfigurationModel.DATABASE_MYSQL_HOST),
                    this.getSettingConfiguration().getString(SettingConfigurationModel.DATABASE_MYSQL_PORT),
                    this.getSettingConfiguration().getString(SettingConfigurationModel.DATABASE_MYSQL_USERNAME),
                    this.getSettingConfiguration().getString(SettingConfigurationModel.DATABASE_MYSQL_PASSWORD),
                    this.getSettingConfiguration().getString(SettingConfigurationModel.DATABASE_MYSQL_DATABASE),
                    this.getSettingConfiguration().getString(SettingConfigurationModel.DATABASE_MYSQL_OPTIONS));
            this.dispatch.setVerbose(getSettingConfiguration().get(SettingConfigurationModel.DEBUG).asBoolean());
            this.dispatch.setLogger(this.getLogger());

            // Set up tables by run async task
            Bukkit.getScheduler().runTaskAsynchronously(this,
                    () -> trackers.describeDatabase("Access and create users table", () -> {
                        try {
                            // Set up user table
                            this.getConsoleTeller().send("&rInitializing database stuffs");
                            this.dispatch.executeUpdate((rows) -> {
                            }, String.format(
                                    "CREATE TABLE IF NOT EXISTS %s_users (" + "uuid varchar(255) NOT NULL PRIMARY KEY "
                                            + ",balance double NOT NULL" + ", username varchar(255))",
                                    this.getSettingConfiguration().get(SettingConfigurationModel.DATABASE_TABLE_PREFIX)
                                            .asString()));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }));

            // Setup controller
            setupUserController(new UserControllerSQL(this));
            return;
        }

        // Mongodb
        if (persistDatabaseType.equalsIgnoreCase("mongodb")) {
            // Register local dispatch
            MongoDispatch dispatch = new MongoDispatch(this);
            // Connection test
            dispatch.getClient(client -> { /* Doing nothing here */ });
            // Set it to controller
            setupUserController(new UserControllerMongodb(this, dispatch));

            // Return nothing
            return;
        }

        // Unsupported type
        throw new IllegalStateException(String.format("cannot found database type %s", persistDatabaseType));
    }

    /**
     * Set up a configuration file (Config.yml)
     *
     * @throws Exception any exception
     */
    private void setupConfiguration() throws Exception {
        this.getConsoleTeller().send("Invoking setting configuration");
        if (settingConfiguration == null) {
            this.settingConfiguration = new SettingConfiguration(this);
        } else {
            this.settingConfiguration.getDreamYaml().saveAndLoad();
        }
    }

    @Override
    public void onDisable() {
        disableDatabaseSource();
    }

    private void disableDatabaseSource() {
        this.getConsoleTeller().send("Invoking configured database type");
        String persistDatabaseType = this.getSettingConfiguration().get(SettingConfigurationModel.DATABASE_TYPE)
                .asString();

        if (persistDatabaseType.equals("mysql")) {
            // Close a data source in order not to cause Too many connections
            MySQLHikariDispatch hikariDispatch = (MySQLHikariDispatch) this.getDispatch();
            if (hikariDispatch.getDataSource() != null) {
                hikariDispatch.getDataSource().close();
            }
        }
    }

    /**
     * @return a configuration of settings
     */
    public SettingConfiguration getSettingConfiguration() {
        return settingConfiguration;
    }

    /**
     * Dispatch a database
     *
     * @return a database establish object
     */
    public Dispatch getDispatch() {
        return dispatch;
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
    public UserController getUserController() {
        return userController;
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
     * @return a user tanker caches many users information
     */
    public OptEcoPlayerManager getPlayerManager() {
        return userStorage;
    }

    /**
     * @return a language configuration instance
     */
    public LanguageConfiguration getLanguageConfiguration() {
        return languageConfiguration;
    }

    /**
     * A console teller bares some method to prettier the history log
     *
     * @return a console teller class instance
     */
    public ConsoleTeller getConsoleTeller() {
        return consoleTeller;
    }

    /**
     * An application inteface instance, to interact with this plugin
     * @return an OptEcoAPI class
     */
    public OptEcoAPI getApplicationInterface() {
        return applicationInterface;
    }
}
