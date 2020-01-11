package dev.king.jdbc;

import java.sql.ResultSet;
import java.util.Optional;
import java.util.stream.Stream;

public interface JdbcProvider {
    boolean openConnection();
    void closeConnection();
    int update(String query, Object... objects);

    <K> Optional<Stream<K>> map(String query, KFunction<ResultSet, K> function, Object... objects);
    <K> Optional<K> query(String query, KFunction<ResultSet, K> consumer, Object... objects);

    void close(AutoCloseable... closeables);
}