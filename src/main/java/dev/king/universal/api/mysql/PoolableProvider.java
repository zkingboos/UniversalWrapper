package dev.king.universal.api.mysql;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

/**
 * The pool of connections
 *
 * @author zkingboos_
 */
public interface PoolableProvider {

    /**
     * Gets the mysql connection
     *
     * @param credentials    used to login into mysql
     * @param maxConnections passed in MysqlProvider constructor
     * @return returns an DataSource object, that u can manage him
     */
    HikariDataSource obtainDataSource(UniversalCredentials credentials, int maxConnections) throws SQLException;
}
