package dev.king.universal.mysql;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * The pool of connections
 *
 * @author zkingboos_
 */
@Getter
@RequiredArgsConstructor
public class PoolableConnection {

    /**
     * Gets the mysql connection
     *
     * @param credentials    used to login into mysql
     * @param maxConnections passed in MysqlProvider constructor
     * @return returns an DataSource object, that u can manage him
     */
    @SneakyThrows
    public HikariDataSource obtainDataSource(UniversalCredentials credentials, int maxConnections) {
        final String fullHost = "jdbc:mysql://" +
                credentials.getHostname() + "/" +
                credentials.getDatabase();

        final HikariDataSource source = new HikariDataSource();

        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setJdbcUrl(fullHost);
        source.setUsername(credentials.getUser());
        source.setPassword(credentials.getPassword());

        source.setMinimumIdle(maxConnections % 2);
        source.setMaximumPoolSize(maxConnections);

        source.setAutoCommit(true);
        source.setLoginTimeout(3);

        return source;
    }
}
