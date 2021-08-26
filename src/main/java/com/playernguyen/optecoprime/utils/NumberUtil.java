package com.playernguyen.optecoprime.utils;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.function.Consumer;

public class NumberUtil {

    /**
     * Number filter was created to analyze a number from string
     */
    public static class NumberFilter {

        /**
         * A field to hold data inside.
         */
        private final String persistData;

        /**
         * A constructor with data argument.
         *
         * @param data a data to analyze as number.
         * @throws NumberFormatException a data is not a number.
         */
        public NumberFilter(@NotNull String data) {
            /**
             * Without number, an exception will be called
             */
            this.persistData = data;
        }

        /**
         * Convert to a double.
         *
         * @return a double value.
         * @throws NumberFormatException the current value is not a number.
         */
        public double asNumber() throws NumberFormatException {
            return Double.parseDouble(persistData);
        }

        /**
         * Check whether a current value is any type of number.
         *
         * @return true whether a current value is number, false otherwise.
         */
        public boolean isNumber() {
            try {
                asNumber();
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        /**
         * Check whether the number is negative or not.
         *
         * @return true whether the persist data is negative, false otherwise.
         */
        public boolean isNegative() {
            return asNumber() < 0;
        }

        /**
         * Check whether the current number is zero.
         *
         * @return true whether a current number is zero, false otherwise
         */
        public boolean isZero() {
            return asNumber() == 0;
        }

        /**
         * Catch whether a current value is not a number.
         *
         * @param consumer a consumer to handle
         * @return a current instance of NumberFilter class
         */
        public NumberFilter onNotNumber(Consumer<String> consumer) {
            if (!isNumber()) {
                consumer.accept(persistData);
            }
            return this;
        }

        /**
         * Call a consumer whenever a current value is negative.
         *
         * @param consumer a consumer to use
         * @return current instance of NumberFilter
         */
        public NumberFilter onNegative(Consumer<Double> consumer) {
            if (isNegative()) {
                consumer.accept(asNumber());
            }
            return this;
        }
    }

    /**
     * Contains a number and formats it when to string method is calling.
     */
    public static class FlexibleNumber {
        private final double persistNumber;

        /**
         * construct new flexible number
         *
         * @param persistNumber a number to construct
         */
        public FlexibleNumber(double persistNumber) {
            this.persistNumber = persistNumber;
        }

        /**
         * Using DecimalFormatter to format a number
         *
         * @param formattedPattern a pattern to format
         * @return a formatted number
         */
        public String toFormattedString(String formattedPattern) {
            NumberFormat format = new DecimalFormat(formattedPattern);
            return format.format(persistNumber);
        }

        @Override
        public String toString() {
            if (persistNumber % 1 == 0) {
                return this.toFormattedString("#0");
            }
            return this.toFormattedString("#0.00");
        }
    }

}
