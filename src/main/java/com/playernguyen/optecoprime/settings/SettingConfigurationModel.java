package com.playernguyen.optecoprime.settings;

import com.playernguyen.optecoprime.configurations.ConfigurationSectionModel;

public enum SettingConfigurationModel implements ConfigurationSectionModel {

    ADMINISTRATOR_LOGGING_UPDATE("Administrator.LoggingUpdate", true,
            "Logging when user balance was modified"),
    DATABASE_MONGODB_COLLECTION_USER("MongoDB.Collection.User",
            "users",
            "A name of users collection"),
    DATABASE_MONGODB_DATABASE("MongoDB.Database",
            "optecoprime",
            "A mongodb server database name"),
    DATABASE_MONGODB_URI("MongoDB.URI",
            "mongodb+srv://localhost:27017",
            "A mongodb server uri to connect to database"),

    DATABASE_MYSQL_DATABASE("MySQL.Database", "optecoprime",
            "A database, you must create a database before using"),

    DATABASE_MYSQL_HOST("MySQL.Host",
            "localhost",
            "A host address for OptEcoPrime to connect to database"),

    DATABASE_MYSQL_OPTIONS("MySQL.Options", "useSSL=false",
            "An option as url parameter to config your database connection."),
    DATABASE_MYSQL_PASSWORD("MySQL.Password",
            "",
            "Credential password, to connect to MySQL server"),
    DATABASE_MYSQL_PORT("MySQL.Port",
            "3306",
            "A port from database for OptEcoPrime to connect to database"),
    DATABASE_MYSQL_USERNAME("MySQL.Username",
            "root",
            "Credential username, to connect to MySQL server"),
    DATABASE_SQLITE_FILE_NAME("SQLite.FileName",
            "data.sqlite",
            "A SQLite file name which contains data"),
    DATABASE_TABLE_PREFIX("TablePrefix", "opteco",
            "Prefix name of database, i.e: prefix_users, prefix_transactions..."),

    DATABASE_TYPE("DatabaseType",
            "sqlite",
            "A type of database system that plugin use",
            "Available: sqlite, mysql, mongodb"),

    DEBUG("Debug",
            false,
            "This setting section is for developers",
            "Do not touch unless you know what you are doing"),
    LEADERBOARD_LIMIT_AMOUNT("Leaderboard.LimitAmount",
            5,
            "Limit a number of players perform in leaderboard"),
    USER_BEGINNING_POINT("User.InceptionBalance",
            0.0,
            "A start point when new player first join the server"),

    USER_PERSIST_DATA_DURATION("User.PersistDataDuration", 100,
            "A duration to check whether the player information was old or not. ",
            "Calculate in millisecond with 1000ms = 1s");

    private final String path;
    private final Object instance;
    private final String[] comments;

    SettingConfigurationModel(String path, Object instance, String... comments) {
        this.path = path;
        this.instance = instance;
        this.comments = comments;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public Object getInstance() {
        return this.instance;
    }

    @Override
    public String[] getComments() {
        return comments;
    }
}
