package dev.king.universal.wrapper.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.king.universal.shared.connection.PoolableProvider;
import dev.king.universal.shared.credential.UniversalCredential;
import lombok.NonNull;

import java.sql.SQLException;

public abstract class HikariPoolableDriver extends PoolableProvider<HikariConfig, HikariDataSource> {

    public HikariPoolableDriver(@NonNull String authUri, @NonNull String driverClassName) {
        super(authUri, driverClassName);
    }

    public HikariDataSource dataSource(@NonNull UniversalCredential credential, int maxConnections) {
        try {
            final HikariDataSource dataSource = new HikariDataSource(configuration(credential, maxConnections));
            dataSource.setLoginTimeout(3);
            return dataSource;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public HikariConfig configuration(@NonNull UniversalCredential credential, int maxConnections) {
        final HikariConfig configuration = new HikariConfig();
        final String fullHost = String.format(getAuthUri(), credential.getHostname(), credential.getDatabase());

        configuration.setDriverClassName(getDriverClassName());
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
