package dev.king.universal.api;

import java.sql.ResultSet;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The interface has provide basic methods
 *
 * @author zkingboos_
 */
public interface JdbcProvider {

    boolean openConnection();

    JdbcProvider preOpen();

    void closeConnection();

    boolean hasConnection();

    void update(String query, Object... objects);

    <K> Optional<Stream<K>> map(String query, KFunction<ResultSet, K> function, Object... objects);

    <K> Optional<K> query(String query, KFunction<ResultSet, K> consumer, Object... objects);

    void close(AutoCloseable... closeables);
}