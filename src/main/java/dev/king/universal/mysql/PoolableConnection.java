package dev.king.universal.mysql;

import com.zaxxer.hikari.HikariDataSource;
import dev.king.universal.api.mysql.PoolableProvider;
import dev.king.universal.api.mysql.UniversalCredentials;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@Getter
@RequiredArgsConstructor
public class PoolableConnection implements PoolableProvider {

    public HikariDataSource obtainDataSource(UniversalCredentials credentials, int maxConnections) throws SQLException {
        final String fullHost = String.format(
          "jdbc:mysql://%s/%s",
          credentials.getHostname(),
          credentials.getDatabase()
        );

        final HikariDataSource source = new HikariDataSource();

        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setJdbcUrl(fullHost);
        source.setUsername(credentials.getUser());
        source.setPassword(credentials.getPassword());

        source.setMinimumIdle(maxConnections % 2);
        source.setMaximumPoolSize(maxConnections);

        source.setAutoCommit(true);
        source.setLoginTimeout(3);

        source.addDataSourceProperty("characterEncoding", "utf8");
        return source;
    }
}
