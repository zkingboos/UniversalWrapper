/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.wrapper.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.king.universal.shared.api.credential.UniversalCredential;
import dev.king.universal.wrapper.mysql.api.PoolableProvider;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@Getter
@RequiredArgsConstructor
public class PoolableConnection implements PoolableProvider {

    private static final String MYSQL_AUTH_URL = "jdbc:mysql://%s/%s";

    /**
     * Get connection pool from hikari that offers more availability
     *
     * @param credentials    instance of {@link dev.king.universal.shared.api.credential.UniversalCredential} to login into mysql
     * @param maxConnections passed in MysqlProvider constructor
     * @return data source
     * @throws SQLException if driver class doesn't exists
     */
    public HikariDataSource obtainDataSource(@NonNull UniversalCredential credentials, int maxConnections) throws SQLException {
        final HikariConfig config = new HikariConfig();
        final String fullHost = String.format(MYSQL_AUTH_URL,
          credentials.getHostname(),
          credentials.getDatabase()
        );

        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl(fullHost);
        config.setUsername(credentials.getUser());
        config.setPassword(credentials.getPassword());

        config.setMinimumIdle(maxConnections / 2);
        config.setMaximumPoolSize(maxConnections);

        config.setAutoCommit(true);
        config.addDataSourceProperty("characterEncoding", "utf8");
        config.addDataSourceProperty("autoReconnect", true);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        final HikariDataSource dataSource = new HikariDataSource(config);
        dataSource.setLoginTimeout(3);

        return dataSource;
    }
}
