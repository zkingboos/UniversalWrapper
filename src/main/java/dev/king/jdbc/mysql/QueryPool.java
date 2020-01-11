package dev.king.jdbc.mysql;

import dev.king.jdbc.JdbcProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.sql.DataSource;

@Getter
@RequiredArgsConstructor
public abstract class QueryPool implements JdbcProvider {

    private final SqlCredentials credentials;
    private GenericObjectPool<PoolableConnection> connectionPool;

    public DataSource obtainDataSource(int maxConnections){

        String fullHost = "jdbc:mysql://"+credentials.getHostname()+"/"+credentials.getDatabase();

        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
                fullHost, credentials.getUser(), credentials.getPassword()
        );

        PoolableConnectionFactory factory = new PoolableConnectionFactory(connectionFactory, null);
        factory.setValidationQuery("SELECT 1");

        connectionPool = new GenericObjectPool<>(factory);
        connectionPool.setMaxTotal(maxConnections);
        factory.setPool(connectionPool);

        return new PoolingDataSource<>(connectionPool);
    }
}
