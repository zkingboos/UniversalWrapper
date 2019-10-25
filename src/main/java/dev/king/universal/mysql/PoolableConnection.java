/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.king.universal.api.mysql.PoolableProvider;
import dev.king.universal.api.mysql.UniversalCredential;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@Getter
@RequiredArgsConstructor
public class PoolableConnection implements PoolableProvider {

    private static final String MYSQL_AUTH_URL = "jdbc:mysql://%s/%s";

    public HikariDataSource obtainDataSource(UniversalCredential credentials, int maxConnections) throws SQLException {
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

        final HikariDataSource dataSource = new HikariDataSource(config);
        dataSource.setLoginTimeout(3);

        return dataSource;
    }
}
