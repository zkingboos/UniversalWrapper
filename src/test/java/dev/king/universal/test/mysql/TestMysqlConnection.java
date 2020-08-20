/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.test.mysql;

import dev.king.universal.UniversalWrapper;
import dev.king.universal.api.JdbcProvider;
import dev.king.universal.api.mysql.UniversalCredential;
import dev.king.universal.test.common.UniversalMethod;

public final class TestMysqlConnection {

    public static void main(String[] args) {
        final UniversalWrapper wrapper = UniversalWrapper.getInstance();
        JdbcProvider provider = wrapper.newMysqlProvider(
          new UniversalCredential(
            "localhost:3306",
            "test",
            "root",
            ""),
          2
        ).preOpen();

        UniversalMethod.dispatchProvider(provider);
    }
}
