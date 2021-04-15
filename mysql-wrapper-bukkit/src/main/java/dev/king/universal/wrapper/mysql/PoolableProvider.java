/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.wrapper.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.king.universal.shared.credential.UniversalCredential;
import lombok.NonNull;

/**
 * The pool of connection, usefully performance to big queries
 */
public interface PoolableProvider {

    /**
     * Gets the mysql connection
     *
     * @param credential     instance of {@link dev.king.universal.shared.credential.UniversalCredential} to login or connect into mysql
     * @param maxConnections passed in MysqlProvider constructor
     * @return returns an {@link com.zaxxer.hikari.HikariDataSource} object, that you can manage it
     */
    HikariDataSource obtainDataSource(@NonNull UniversalCredential credential, int maxConnections);

    /**
     * Create hikari object configuration
     * Also can be used to override default configurations
     *
     * @param credential     instance of {@link dev.king.universal.shared.credential.UniversalCredential} to login or connect into mysql
     * @param maxConnections passed in MysqlProvider constructor
     * @return hikari object configuration pool
     */
    HikariConfig getHikariConfiguration(@NonNull UniversalCredential credential, int maxConnections);
}
