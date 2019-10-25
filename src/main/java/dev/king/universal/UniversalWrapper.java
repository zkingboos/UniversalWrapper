/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal;

import dev.king.universal.api.JdbcProvider;
import dev.king.universal.api.mysql.UniversalCredential;
import dev.king.universal.mysql.MysqlProvider;
import dev.king.universal.sql.SqlProvider;
import lombok.Getter;

import java.io.File;

/**
 * Abstract provider
 */
public final class UniversalWrapper {

    @Getter(lazy = true)
    private static final UniversalWrapper instance = new UniversalWrapper();

    /**
     * Creates provider to mysql
     *
     * @param credentials    login credentials
     * @param maxConnections number of max connections (idle connections are divided by 2)
     * @return instance of mysql provider
     */
    public JdbcProvider newMysqlProvider(UniversalCredential credentials, int maxConnections) {
        return new MysqlProvider(
          credentials, maxConnections
        );
    }

    /**
     * Creates provider to sql
     *
     * @param file path of file
     * @return instance of sql provider
     */
    public JdbcProvider newSqlProvider(File file) {
        return new SqlProvider(
          file
        );
    }
}
