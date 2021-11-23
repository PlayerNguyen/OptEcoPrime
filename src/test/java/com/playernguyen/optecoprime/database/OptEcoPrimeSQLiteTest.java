package com.playernguyen.optecoprime.database;

import com.playernguyen.optecoprime.OptEcoPrimeMockTester;
import com.playernguyen.optecoprime.settings.SettingConfigurationModel;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OptEcoPrimeSQLiteTest extends OptEcoPrimeMockTester {

    @Test
    public void successfullyConnectToDatabase() throws SQLException {
        plugin.getDispatch().openConnection(Assert::assertNotNull);
    }

    /**
     * Check whether system contains user table after pre-load
     */
    @Test
    public void containsUserTableSQLite() throws SQLException {
//        plugin.getDispatch().preparedStatement((statement) -> {
//                    ResultSet resultSet = statement.executeQuery();
//                    while (resultSet.next()) {
//                        Assert.assertEquals(resultSet.getString(1), String.format(
//                                "%s_users",
//                                plugin.getSettingConfiguration()
//                                        .get(SettingConfigurationModel.DATABASE_TABLE_PREFIX)
//                                        .asString()
//                        ));
//                    }
//                }, "SELECT name FROM sqlite_master WHERE type='table' AND name=?;",
//                String.format("%s_users", plugin.getSettingConfiguration()
//                        .get(SettingConfigurationModel.DATABASE_TABLE_PREFIX)
//                        .asString()));
        String userTableName = String.format("%s_users", plugin.getSettingConfiguration()
                .get(SettingConfigurationModel.DATABASE_TABLE_PREFIX)
                .asString());
        List<String> list = new ArrayList<>();
        plugin.getDispatch().executeQuery((resultSet) -> {
                    while (resultSet.next()) {
                        list.add(resultSet.getString(1));
                    }
                }, "SELECT name FROM sqlite_master WHERE type='table' AND name=?;",
                userTableName
        );
        System.out.println(list);

        Assert.assertTrue(list.contains(userTableName));
    }

}
