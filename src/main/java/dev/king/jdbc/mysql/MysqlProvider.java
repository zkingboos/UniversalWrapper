package dev.king.jdbc.mysql;

import dev.king.jdbc.KFunction;
import dev.king.jdbc.Utilities;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

/**
 * The provider for mysql for jdbc
 * @author zkingboos_
 */
public class MysqlProvider extends QueryPool {

    private DataSource dataSource;

    /**
     * Create an Provider object
     * @param credentials the credentials to connect on mysql server
     * @param maxConnections the maximum of connections that will be connected
     */
    public MysqlProvider(SqlCredentials credentials, int maxConnections) {
        super(credentials);
        dataSource = obtainDataSource(maxConnections);
    }

    /**
     * Close the all connections of datasource
     */
    @Override
    public void closeConnection() {
        getConnectionPool().close();
    }

    /**
     * Connect the all connections on mysql server
     * @return if has a valid connection
     */
    @Override
    public boolean openConnection() {
        try {
            Connection connection = dataSource.getConnection();
            boolean result = connection != null && !connection.isClosed();
            close(connection);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Uses just in select query
     * @param query the query of mysql
     * @param function if has a valid entry, function will be called and returns a result
     * @param objects the objects that will be putted in the prepared statment
     * @param <K> the generic type, used to return your prefer value
     * @return returns a optional value, applied in function parameter
     */
    @Override
    public <K> Optional<K> query(
            String query,
            KFunction<ResultSet, K> function,
            Object... objects
    ) {
        try {
            Connection connection = dataSource.getConnection();

            //create stm
            PreparedStatement ps = connection.prepareStatement(query);
            Utilities.syncObjects(ps, objects);

            ResultSet set = ps.executeQuery();
            K result = set != null && set.next() ? function.apply(set) : null;

            //close the connections
            close(set, ps, connection);
            return Optional.ofNullable(result);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Uses just in select query
     * @param query the query of mysql
     * @param function if has a valid entry, function will be called and returns a result
     * @param objects the objects that will be putted in the prepared statment
     * @param <K> the generic type, used to return your prefer value
     * @return returns a optional value, applied in function parameter
     */
    public <K> Optional<Stream<K>> map(
            String query,
            KFunction<ResultSet, K> function,
            Object... objects
    ){
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            Utilities.syncObjects(ps, objects);
            ResultSet set = ps.executeQuery();

            List<K> paramList = new ArrayList<>();
            while (set.next()) { paramList.add(function.apply(set)); }

            close(set, ps, connection);
            return Optional.ofNullable(paramList.stream());
        }catch (Exception e){
            return Optional.empty();
        }
    }

    /**
     * Uses just in create, delete, insert and update querys
     * @param query the query of mysql
     * @param objects the objects that will be putted in the prepared statment
     * @return returns a int response, if do appear -1, signify that have an error
     */
    @Override
    public int update(
            String query,
            Object... objects
    ) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareCall(query);
            Utilities.syncObjects(ps, objects);
            int result = ps.executeUpdate();

            //close connections
            close(ps, connection);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Close the all AutoCloseable instances
     * @param closeables the all closeable connections
     */
    @SneakyThrows
    public void close(AutoCloseable... closeables) {
        for (AutoCloseable close : closeables) { close.close(); }
    }

    /*private void status() {
        GenericObjectPool<PoolableConnection> sql = getConnectionPool();
        System.out.println("Max alive connections: " + sql.getNumActive() + "; Max connections: " + sql.getMaxTotal());
        System.out.println("Active: " + sql.getNumActive());
        System.out.println("Idle: " + sql.getNumIdle());
        System.out.println("Waiting: " + sql.getNumWaiters());
    }*/
}