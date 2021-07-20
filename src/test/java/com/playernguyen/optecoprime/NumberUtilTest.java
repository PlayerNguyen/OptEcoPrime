package com.playernguyen.optecoprime;

import com.playernguyen.optecoprime.utils.NumberUtil.FlexibleNumber;
import com.playernguyen.optecoprime.utils.NumberUtil.NumberFilter;

import org.junit.Assert;
import org.junit.Test;

public class NumberUtilTest {

    @Test
    public void shouldReturnAsNumberDouble() {
        // Convert to a number
        NumberFilter filter = new NumberFilter("15.2");
        Assert.assertEquals(15.2, filter.asNumber(), 0.1);
    }

    @Test
    public void shouldIsNumber() {
        // True
        NumberFilter filter = new NumberFilter("150");
        Assert.assertTrue(filter.isNumber());
    }

    @Test
    public void shouldNotANumber() {
        // False
        NumberFilter filter1 = new NumberFilter("150x");
        Assert.assertFalse(filter1.isNumber());
    }

    @Test
    public void shouldReturnFlexibleNumber() {
        // revert a flexible string
        FlexibleNumber number = new FlexibleNumber(15.98);
        Assert.assertEquals("15,98", number.toString());

    }

    @Test
    public void shouldReturnInitInFlexNumber() {
        // int number
        FlexibleNumber intNumber = new FlexibleNumber(16.0000000);
        Assert.assertEquals("16", intNumber.toString());
    }
}
