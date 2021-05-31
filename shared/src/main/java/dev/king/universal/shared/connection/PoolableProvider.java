/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.shared.connection;

import dev.king.universal.shared.credential.UniversalCredential;
import lombok.Data;
import lombok.NonNull;

/**
 * The pool of connection, usefully performance to big queries
 */
@Data
public abstract class PoolableProvider<Configuration, DataSource> {

    private final @NonNull String authUri;
    private final @NonNull String driverClassName;

    /**
     * Gets the mysql connection
     *
     * @param credential     instance of {@link dev.king.universal.shared.credential.UniversalCredential} to login or connect into mysql
     * @param maxConnections passed in MysqlProvider constructor
     * @return returns an {@link DataSource} manageable object
     */
    public abstract DataSource dataSource(@NonNull UniversalCredential credential, int maxConnections);

    /**
     * Create hikari object configuration
     * Also can be used to override default configurations
     *
     * @param credential     instance of {@link dev.king.universal.shared.credential.UniversalCredential} to login or connect into mysql
     * @param maxConnections passed in MysqlProvider constructor
     * @return configuration object to manipulate driver
     */
    public abstract Configuration configuration(@NonNull UniversalCredential credential, int maxConnections);
}
