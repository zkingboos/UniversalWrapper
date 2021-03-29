/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.wrapper.mysql.implementation.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.king.universal.shared.credential.UniversalCredential;
import dev.king.universal.wrapper.mysql.PoolableProvider;
import lombok.Data;
import lombok.NonNull;

import java.sql.SQLException;

@Data
public class DefaultPoolableConnection implements PoolableProvider {

    private static final String MYSQL_AUTH_URL = "jdbc:mysql://%s/%s";

    /**
     * Get connection pool from hikari that offers more availability
     *
     * @param credentials    instance of {@link dev.king.universal.shared.credential.UniversalCredential} to login into mysql
     * @param maxConnections passed in MysqlProvider constructor
     * @return data source
     */
    public HikariDataSource obtainDataSource(@NonNull UniversalCredential credentials, int maxConnections) {
        try {
            final HikariConfig configuration = new HikariConfig();
            final String fullHost = String.format(MYSQL_AUTH_URL,
              credentials.getHostname(),
              credentials.getDatabase()
            );

            configuration.setDriverClassName("com.mysql.jdbc.Driver");
            configuration.setJdbcUrl(fullHost);
            configuration.setUsername(credentials.getUser());
            configuration.setPassword(credentials.getPassword());

            configuration.setMinimumIdle(maxConnections / 2);
            configuration.setMaximumPoolSize(maxConnections);

            configuration.setAutoCommit(true);
            configuration.addDataSourceProperty("characterEncoding", "utf8");
            configuration.addDataSourceProperty("autoReconnect", "true");
            configuration.addDataSourceProperty("cachePrepStmts", "true");
            configuration.addDataSourceProperty("useServerPrepStmts", "true");
            configuration.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            configuration.addDataSourceProperty("rewriteBatchedStatements", "true");

            final HikariDataSource dataSource = new HikariDataSource(configuration);
            dataSource.setLoginTimeout(3);

            return dataSource;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
