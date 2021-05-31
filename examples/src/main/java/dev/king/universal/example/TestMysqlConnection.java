/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.example;

import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.shared.credential.UniversalCredential;
import dev.king.universal.wrapper.mysql.MySQLProvider;
import dev.king.universal.wrapper.mysql.implementation.credential.MysqlCredential;

public final class TestMysqlConnection {
//
//    /**
//     * Initialize mysql connection, using UniversalWrapper project
//     * Use {@link MySQLProvider#from(UniversalCredential, int)} to connect into mysql-server
//     * you can also, call directly {@link MySQLProvider#from(String, String, String, String, int)}
//     *
//     * @param args program arguments
//     */
//    public static void main(String[] args) {
//        DefaultSQLSupport provider = MySQLProvider.from(
//          new MysqlCredential(
//            "localhost:3306",
//            "universalwrapper",
//            "root",
//            "test"),
//          2
//        );
//
//        //provider = MySQLProvider.from("localhost:3306", "test", "root", "", 2); also can use this method
//        DispatchMethod.dispatchProvider(provider);
//    }
}
