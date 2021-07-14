package com.playernguyen.optecoprime.configurations;

/**
 * Interface of configuration
 */
public interface ConfigurationSectionModel {

    /**
     * Each configuration section must have a node. It called path, which separated by dots
     *
     * @return a path which separated by dots
     */
    String getPath();

    /**
     * The very first value which set by first init call
     *
     * @return a default value
     */
    Object getInstance();

    /**
     * Comments of the section, each index as line
     *
     * @return a comment list of section
     */
    String[] getComments();

}
