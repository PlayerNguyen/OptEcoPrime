package com.playernguyen.optecoprime.utils;

import java.util.HashMap;
import java.util.Map;

import com.playernguyen.optecoprime.OptEcoPrime;
import com.playernguyen.optecoprime.languages.LanguageConfigurationModel;
import com.playernguyen.optecoprime.utils.NumberUtil.FlexibleNumber;

/**
 * ReplaceableString represents a string that can replace. Can use with
 * PlaceholderAPI hook, ...
 */
public class ReplaceableString {
    private final String persistedString;
    private final Map<String, String> reflectTable = new HashMap<>();

    public ReplaceableString(OptEcoPrime plugin, String persistedString) {

        this.persistedString = persistedString;

        // Register a global variables
        change("%currency_symbol%",
                plugin.getLanguageConfiguration().get(LanguageConfigurationModel.CURRENCY_SYMBOL).asString());
    }

    /**
     * Add new replacement into reflect table.
     * 
     * @param target a target to replace
     * @param to     a string to replace
     * @return current replaceable string instance
     */
    public ReplaceableString change(String target, String to) {
        reflectTable.put(target, to);
        return this;
    }

    /**
     * Format a number as to parameter after replace.
     * 
     * @param target a target to replace
     * @param to     a double to replace
     * @return current replaceable string instance
     */
    public ReplaceableString changeFlex(String target, double to) {
        reflectTable.put(target, new FlexibleNumber(to).toString());
        return this;
    }

    @Override
    public String toString() {
        String dynamicString = persistedString;

        // Iteratively replace by O(n) with n it length of reflectTable
        for (Map.Entry<String, String> entry : reflectTable.entrySet()) {
            String target = entry.getKey();
            String to = entry.getValue();

            dynamicString = dynamicString.replace(target, to);
        }

        return dynamicString;
    }

}