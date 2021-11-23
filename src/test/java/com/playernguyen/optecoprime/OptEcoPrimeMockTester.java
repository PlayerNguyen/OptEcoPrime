package com.playernguyen.optecoprime;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.osiris.dyml.DYValue;
import com.osiris.dyml.exceptions.DYReaderException;
import com.osiris.dyml.exceptions.DYWriterException;
import com.osiris.dyml.exceptions.DuplicateKeyException;
import com.osiris.dyml.exceptions.IllegalListException;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;

public abstract class OptEcoPrimeMockTester {

    public ServerMock server;
    public OptEcoPrime plugin;

    /**
     * Setup before test
     */
    @Before
    public void setup() throws DYWriterException, IOException, DuplicateKeyException, DYReaderException, IllegalListException {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(OptEcoPrime.class);
        // Set debug mode
        plugin.getSettingConfiguration().get(SettingConfigurationModel.DEBUG).setValues(new DYValue(true));
        plugin.getSettingConfiguration().getDreamYaml().save();
    }

    /**
     * Tear down test
     */
    @After
    public void tearDown() {
        MockBukkit.unmock();
    }

}
