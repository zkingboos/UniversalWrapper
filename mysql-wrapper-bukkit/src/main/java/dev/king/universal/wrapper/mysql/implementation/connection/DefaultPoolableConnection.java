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

    private final String authUri;
    private final String driverClassName;

    public DefaultPoolableConnection(@NonNull String authUri, @NonNull String driverClassName) {
        this.authUri = authUri;
        this.driverClassName = driverClassName;
    }

    public DefaultPoolableConnection() {
        this("jdbc:mysql://%s/%s", "com.mysql.jdbc.Driver");
    }

    public HikariDataSource obtainDataSource(@NonNull UniversalCredential credential, int maxConnections) {
        try {
            final HikariDataSource dataSource = new HikariDataSource(getHikariConfiguration(credential, maxConnections));
            dataSource.setLoginTimeout(3);

            return dataSource;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public HikariConfig getHikariConfiguration(@NonNull UniversalCredential credential, int maxConnections) {
        final HikariConfig configuration = new HikariConfig();
        final String fullHost = String.format(authUri,
          credential.getHostname(),
          credential.getDatabase()
        );

        configuration.setDriverClassName(driverClassName);
        configuration.setJdbcUrl(fullHost);
        configuration.setUsername(credential.getUser());
        configuration.setPassword(credential.getPassword());

        configuration.setMinimumIdle(maxConnections / 2);
        configuration.setMaximumPoolSize(maxConnections);

        configuration.setAutoCommit(true);
        configuration.addDataSourceProperty("characterEncoding", "utf8");
        configuration.addDataSourceProperty("autoReconnect", "true");
        configuration.addDataSourceProperty("cachePrepStmts", "true");
        configuration.addDataSourceProperty("useServerPrepStmts", "true");
        configuration.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        configuration.addDataSourceProperty("rewriteBatchedStatements", "true");

        return configuration;
    }
}
