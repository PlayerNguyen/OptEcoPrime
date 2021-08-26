package com.playernguyen.optecoprime;

import com.playernguyen.optecoprime.utils.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;


public class StringUtilTest {

    @Test
    public void shouldValidUUIDType() {
        UUID uuid = UUID.randomUUID();
        Assert.assertTrue(StringUtil.isUUID(uuid.toString()));
    }

}
