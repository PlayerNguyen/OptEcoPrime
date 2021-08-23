package com.playernguyen.optecoprime.commands.core;

import org.jetbrains.annotations.NotNull;

/**
 * A parameters of the command, declares by name
 * and requirements (is require or not). Parameters class
 * shows whenever command sender send a failed command or missing parameters.
 */
public interface CommandParameter {

    /**
     * A name of current parameter.
     *
     * @return a name of current parameter.
     */
    String getName();

    /**
     * Do sender need to put this parameter in their command.
     *
     * @return true whether required, false otherwise.
     */
    boolean isRequire();

    /**
     * Create new default parameter.
     *
     * @param name    a name of parameter
     * @param require is required or not
     * @return new instance of default parameter.
     */
    static CommandParameter newParameter(@NotNull String name,
                                         boolean require) {
        return new CommandDefaultParameter(name, require);
    }

}
