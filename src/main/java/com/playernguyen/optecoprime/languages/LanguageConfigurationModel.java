package com.playernguyen.optecoprime.languages;

import com.playernguyen.optecoprime.configurations.ConfigurationSectionModel;

/**
 * Language configuration model.
 */
public enum LanguageConfigurationModel implements ConfigurationSectionModel {

    ;

    private final String path;
    private final Object instance;
    private final String[] comments;

    LanguageConfigurationModel(String path, Object instance, String... comments) {
        this.path = path;
        this.instance = instance;
        this.comments = comments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getComments() {
        return comments;
    }
}
