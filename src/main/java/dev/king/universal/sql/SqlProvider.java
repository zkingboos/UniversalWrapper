package dev.king.universal.sql;

import dev.king.universal.Utility;
import dev.king.universal.api.JdbcProvider;
import dev.king.universal.api.KFunction;
import dev.king.universal.api.KRunnable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.sqlite.JDBC;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

/**
 * The provider for sqlite for universal
 *
 * @author zkingboos_
 */
@RequiredArgsConstructor
public class SqlProvider implements JdbcProvider {

    private final File output;
    private final ExecutorService executorService;

    private Connection con;

    /**
     * Close the single connection
     */
    @Override
    public void closeConnection() {
        if (!hasConnection()) return;
        close(con);
    }

    /**
     * Verify if the connection is valid
     *
     * @return returns if connection an valid
     */
    @SneakyThrows
    public boolean hasConnection() {
        return con != null && !con.isClosed();
    }

    /**
     * Opens the single connection
     *
     * @return returns if connection is opened
     */
    @Override
    public boolean openConnection() {
        try {
            if (hasConnection()) return true;
            if (!output.exists()) return false;
            DriverManager.registerDriver(new JDBC());
            con = DriverManager.getConnection("jdbc:sqlite:" + output);
            return !con.isClosed();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Dont do nothing here
     *
     * @return actual instance
     */
    @Override
    public JdbcProvider preOpen() {
        return this;
    }

    /**
     * Uses just in select query
     *
     * @param query    the query of mysql
     * @param consumer if has a valid entry, function will be called and returns a result
     * @param objects  the objects that will be putted in the prepared statment
     * @param <K>      the generic type, used to return your prefer value
     * @return returns a optional value, applied in function parameter
     */
    @Override
    public <K> Optional<K> query(
            String query,
            KFunction<ResultSet, K> consumer,
            Object... objects
    ) {
        try {
            PreparedStatement ps = con.prepareStatement(query);
            Utility.syncObjects(ps, objects);
            ResultSet set = ps.executeQuery();
            K result = set != null && set.next() ? consumer.apply(set) : null;
            close(ps, set);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Uses just in create, delete, insert and update querys
     *
     * @param query   the query of mysql
     * @param objects the objects that will be putted in the prepared statment
     */
    @Override
    public void update(
            String query,
            Object... objects
    ) {
        KRunnable runnable = () -> {
            PreparedStatement ps = con.prepareStatement(query);
            Utility.syncObjects(ps, objects);

            ps.executeUpdate();
            close(ps);
        };

        CompletableFuture.runAsync(runnable, executorService);
    }

    /**
     * Uses just in select query
     *
     * @param query    the query of mysql
     * @param function if has a valid entry, function will be called and returns a result
     * @param objects  the objects that will be putted in the prepared statment
     * @param <K>      the generic type, used to return your prefer value
     * @return returns a optional value, applied in function parameter
     */
    @Override
    public <K> Optional<Stream<K>> map(
            String query,
            KFunction<ResultSet, K> function,
            Object... objects
    ) {
        try {
            PreparedStatement ps = con.prepareStatement(query);
            Utility.syncObjects(ps, objects);
            ResultSet set = ps.executeQuery();

            List<K> paramResult = new ArrayList<>();
            while (set.next()) {
                paramResult.add(function.apply(set));
            }

            close(set, ps);
            return Optional.ofNullable(paramResult.stream());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Close the all AutoCloseable instances
     *
     * @param closeables the all closeable connections
     */
    @SneakyThrows
    public void close(AutoCloseable... closeables) {
        for (AutoCloseable close : closeables) {
            close.close();
        }
    }
}
