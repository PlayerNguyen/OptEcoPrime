package com.playernguyen.optecoprime.utils;

import java.util.UUID;

public class StringUtil {

    /**
     * Test whether input value is uuid or not.
     * @param input an input value
     * @return true whether is uuid, false otherwise
     */
    public static boolean isUUID(String input) {
        try {
            UUID.fromString(input);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

}
