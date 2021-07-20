package com.playernguyen.optecoprime;

import java.util.UUID;

import com.playernguyen.optecoprime.utils.StringUtil;

import org.junit.Assert;
import org.junit.Test;


public class StringUtilTest {
    
    @Test
    public void shouldValidUUIDType() {
        UUID uuid = UUID.randomUUID();
        Assert.assertTrue(StringUtil.isUUID(uuid.toString()));
    }

}
