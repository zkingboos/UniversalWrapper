package dev.king.universal.mysql;

import com.zaxxer.hikari.HikariDataSource;
import dev.king.universal.Utility;
import dev.king.universal.api.JdbcProvider;
import dev.king.universal.api.KFunction;
import dev.king.universal.api.KRunnable;
import dev.king.universal.api.mysql.UniversalCredentials;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public class MysqlProvider extends PoolableConnection implements JdbcProvider {

    private final UniversalCredentials credentials;
    private final int maxConnections;
    private final ExecutorService executorService;

    @Setter
    private HikariDataSource source;

    @Override
    public void closeConnection() {
        getSource().close();
    }

    @Override
    public boolean hasConnection() {
        return openConnection();
    }

    @Override
    public boolean openConnection() {
        try {
            Connection connection = getSource().getConnection();
            final boolean result = connection != null && !connection.isClosed();
            close(connection);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JdbcProvider preOpen() {
        setSource(obtainDataSource(credentials, maxConnections));
        return this;
    }

    @Override
    public <K> Optional<K> query(
            String query,
            KFunction<ResultSet, K> function,
            Object... objects
    ) {
        try {
            Connection connection = getSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(query);

            Utility.syncObjects(ps, objects);

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
    ) {
        try {
            Connection connection = getSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            Utility.syncObjects(ps, objects);

            ResultSet set = ps.executeQuery();

            List<K> paramList = new ArrayList<>();
            while (set.next()) {
                paramList.add(function.apply(set));
            }

            close(set, ps, connection);
            return Optional.ofNullable(paramList.stream());
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
            Connection connection = getSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(query);

            Utility.syncObjects(ps, objects);
            ps.executeUpdate();

            //close the connections
            close(ps, connection);
        };

        CompletableFuture.runAsync(runnable, executorService);
    }

    @SneakyThrows
    public void close(AutoCloseable... closeables) {
        for (AutoCloseable close : closeables) {
            close.close();
        }
    }
}