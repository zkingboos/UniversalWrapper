package dev.king.jdbc;

import dev.king.jdbc.functional.KFunction;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.Optional;

public interface JdbcProvider {
    DataSource obtainDataSource(int maxConnections);
    boolean openConnection();
    <K> Optional<K> query(String query, KFunction<ResultSet, K> consumer, Object... objects);
    void update(String query, Object... objects);
}