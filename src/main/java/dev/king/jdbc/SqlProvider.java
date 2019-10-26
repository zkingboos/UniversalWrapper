package dev.king.jdbc;

import dev.king.jdbc.functional.KFunction;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

public class SqlProvider extends QueryPool {

    private DataSource dataSource;

    public SqlProvider(SqlCredentials credentials, int maxConnections) {
        super(credentials);
        dataSource = obtainDataSource(maxConnections);
    }

    @Override
    public boolean openConnection() {
        try {
            Connection connection = dataSource.getConnection();
            return connection != null && !connection.isClosed();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public <K> Optional<K> query(String query, KFunction<ResultSet, K> function, Object... objects) {
        try {
            Connection connection = dataSource.getConnection();

            //create stm
            PreparedStatement ps = connection.prepareStatement(query);
            syncObjects(ps, objects);

            ResultSet set = ps.executeQuery();
            K result = set != null && set.next() ? function.apply(set) : null;

            //close the connections
            set.close(); ps.close(); connection.close();
            return Optional.ofNullable(result);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public int update(String query, Object... objects) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareCall(query);
            syncObjects(ps, objects);
            int result = ps.executeUpdate();

            //close connections
            ps.close();connection.close();
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    private void syncObjects(PreparedStatement ps, Object... objects) throws SQLException {
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        for (int i = 1; iterator.hasNext(); i++) {
            ps.setObject(i, iterator.next());
        }
    }

    private void status(){
        GenericObjectPool<PoolableConnection> sql = getConnectionPool();
        System.out.println("Max alive connections: "+sql.getNumActive()+"; Max connections: "+sql.getMaxTotal());
        System.out.println("Active: "+sql.getNumActive());
        System.out.println("Idle: "+sql.getNumIdle());
        System.out.println("Waiting: "+sql.getNumWaiters());
    }
}