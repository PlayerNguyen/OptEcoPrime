package com.playernguyen.optecoprime;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.junit.After;
import org.junit.Before;

public abstract class OptEcoPrimeMockTester {

    public ServerMock server;
    public OptEcoPrime plugin;

    /**
     * Setup before test
     */
    @Before
    public void setup() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(OptEcoPrime.class);
    }

    /**
     * Tear down test
     */
    @After
    public void tearDown() {
        MockBukkit.unmock();
    }

}
