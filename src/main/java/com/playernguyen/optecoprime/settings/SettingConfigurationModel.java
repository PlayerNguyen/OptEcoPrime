package com.playernguyen.optecoprime.settings;

import com.playernguyen.optecoprime.configurations.ConfigurationSectionModel;

public enum SettingConfigurationModel implements ConfigurationSectionModel {

    DEBUG("Debug", false, "This setting section is for developers",
            "Do not touch unless you know what you are doing"
    ),
    DATABASE_TYPE("Database.DatabaseType", "sqlite", "A type of database system that plugin use"),
    DATABASE_SQLITE_FILE_NAME("Database.SQLite.FileName", "data.sqlite", "A SQLite file name which contains data"),
    ;

    private final String path;
    private final Object instance;
    private final String[] comments;

    SettingConfigurationModel(String path,
                              Object instance,
                              String... comments) {
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
