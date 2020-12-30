/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.wrapper.mysql.api;

import com.zaxxer.hikari.HikariDataSource;
import dev.king.universal.shared.api.credential.UniversalCredential;
import lombok.NonNull;

import java.sql.SQLException;

/**
 * The pool of connection, usefully performance to big queries
 */
public interface PoolableProvider {

    /**
     * Gets the mysql connection
     *
     * @param credentials    instance of {@link dev.king.universal.shared.api.credential.UniversalCredential} to login into mysql
     * @param maxConnections passed in MysqlProvider constructor
     * @return returns an {@link com.zaxxer.hikari.HikariDataSource} object, that you can manage it
     * @throws SQLException anything can break here, don't worry.
     */
    HikariDataSource obtainDataSource(@NonNull UniversalCredential credentials, int maxConnections) throws SQLException;
}
