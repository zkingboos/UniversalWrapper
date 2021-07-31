/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.example;

import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.shared.credential.UniversalCredential;
import dev.king.universal.wrapper.mysql.MySQLProvider;
import dev.king.universal.wrapper.mysql.implementation.credential.MysqlCredential;

public final class TestMysqlConnection {

    /**
     * Initialize mysql connection, using UniversalWrapper project
     * Use {@link dev.king.universal.wrapper.mysql.MySQLProviderBuilder#credential(UniversalCredential)}
     * to set the credential used to connect to mysql-server
     * you can also, call directly {@link dev.king.universal.wrapper.mysql.MySQLProviderBuilder#from(String, String, String, String)}
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        final DefaultSQLSupport defaultSQLSupport = MySQLProvider
          .builder()
          .from("localhost:3306", "universalwrapper", "king", "application_dev")
          .build();

        DispatchMethod.dispatchProvider(defaultSQLSupport);
    }
}
