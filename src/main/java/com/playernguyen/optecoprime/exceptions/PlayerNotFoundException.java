package com.playernguyen.optecoprime.exceptions;

import java.util.UUID;

public class PlayerNotFoundException extends Exception {

    private final UUID uuid;

    public PlayerNotFoundException(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUniqueId() {
        return uuid;
    }
}
