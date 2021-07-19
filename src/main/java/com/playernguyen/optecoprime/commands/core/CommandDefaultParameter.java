package com.playernguyen.optecoprime.commands.core;

public class CommandDefaultParameter implements CommandParameter {
    private final String name;
    private final boolean require;

    public CommandDefaultParameter(String name, boolean require) {
        this.name = name;
        this.require = require;
    }

    /**
     * A name of current parameter.
     *
     * @return a name of current parameter.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Do sender need to put this parameter in their command.
     *
     * @return true whether required, false otherwise.
     */
    @Override
    public boolean isRequire() {
        return require;
    }
}
