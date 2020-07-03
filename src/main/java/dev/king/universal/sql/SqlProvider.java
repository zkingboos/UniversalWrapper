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

@RequiredArgsConstructor
public final class SqlProvider implements JdbcProvider {

    private final File output;
    private final ExecutorService executorService;

    private Connection con;

    @Override
    public void closeConnection() {
        if (hasConnection())
            close(con);
    }

    @SneakyThrows
    public boolean hasConnection() {
        return con != null && !con.isClosed();
    }

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

    @Override
    public JdbcProvider preOpen() {
        //TODO: doesn't nothing
        return this;
    }

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

    @SneakyThrows
    public void close(AutoCloseable... closeables) {
        for (AutoCloseable close : closeables) {
            close.close();
        }
    }
}
