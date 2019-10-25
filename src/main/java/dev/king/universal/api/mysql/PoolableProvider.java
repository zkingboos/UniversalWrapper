/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.api.mysql;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

/**
 * The pool of connection, usefully performance to big queries
 */
public interface PoolableProvider {

    /**
     * Gets the mysql connection
     *
     * @param credentials    used to login into mysql
     * @param maxConnections passed in MysqlProvider constructor
     * @return returns an DataSource object, that you can manage it
     * @throws SQLException anything can break here, don't worry.
     */
    HikariDataSource obtainDataSource(UniversalCredential credentials, int maxConnections) throws SQLException;
}
