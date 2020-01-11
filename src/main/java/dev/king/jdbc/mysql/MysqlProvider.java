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

public class MysqlProvider extends QueryPool {

    private DataSource dataSource;

    public MysqlProvider(SqlCredentials credentials, int maxConnections) {
        super(credentials);
        dataSource = obtainDataSource(maxConnections);
    }

    @Override
    public void closeConnection() {
        getConnectionPool().close();
    }

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