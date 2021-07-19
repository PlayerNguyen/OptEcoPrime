package com.playernguyen.optecoprime.settings;

import com.playernguyen.optecoprime.configurations.ConfigurationSectionModel;

public enum SettingConfigurationModel implements ConfigurationSectionModel {

    DEBUG("Debug", false, "This setting section is for developers", "Do not touch unless you know what you are doing"),
    DATABASE_TYPE("Database.DatabaseType", "sqlite", "A type of database system that plugin use"),
    DATABASE_TABLE_PREFIX("Database.TablePrefix", "opteco",
            "Prefix name of database, i.e: prefix_users, prefix_transactions..."),
    DATABASE_SQLITE_FILE_NAME("Database.SQLite.FileName", "data.sqlite", "A SQLite file name which contains data"),

    USER_BEGINNING_POINT("User.BeginningPoint", 0.0, "The beginning point when new player first join the server"),

    USER_PERSIST_DATA_DURATION("User.PersistDataDuration", 100,
            "A duration to check whether the player information was old or not. ",
            "Calculate in millisecond with 1000ms = 1s"),

    ;

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
