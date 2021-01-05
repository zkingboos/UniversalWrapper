/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.test;

import dev.king.universal.shared.api.JdbcProvider;
import dev.king.universal.test.util.UniversalMethod;
import dev.king.universal.wrapper.mysql.MysqlProvider;
import dev.king.universal.wrapper.mysql.api.MysqlCredential;

public final class TestMysqlConnection {

    public static void main(String[] args) {
        final JdbcProvider provider = MysqlProvider.from(
          new MysqlCredential(
            "localhost:3306",
            "test",
            "root",
            ""),
          2
        ).preOpen();

        UniversalMethod.dispatchProvider(provider);
    }
}
