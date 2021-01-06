/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.wrapper.mysql;

import com.zaxxer.hikari.HikariDataSource;
import dev.king.universal.shared.UniversalUtil;
import dev.king.universal.shared.api.JdbcProvider;
import dev.king.universal.shared.api.batch.ComputedBatchQuery;
import dev.king.universal.shared.api.batch.impl.UnitComputedBatchQuery;
import dev.king.universal.shared.api.credential.UniversalCredential;
import dev.king.universal.shared.api.functional.SafetyBiConsumer;
import dev.king.universal.shared.api.functional.SafetyFunction;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public final class MysqlProvider extends PoolableConnection implements JdbcProvider {

    private final UniversalCredential credential;
    private final int maxConnections;
    private final HikariDataSource source;

    @SneakyThrows
    public MysqlProvider(@NonNull UniversalCredential credential, int maxConnections) {
        this.credential = credential;
        this.maxConnections = maxConnections;
        this.source = obtainDataSource(credential, maxConnections);
    }

    /**
     * Creates provider to mysql
     *
     * @param universalCredential login credentials from {@link dev.king.universal.shared.api.credential.UniversalCredential}
     * @param maxConnections      number of max connections (idle connections are divided by 2)
     * @return instance of mysql provider
     */
    public static JdbcProvider from(@NonNull UniversalCredential universalCredential, int maxConnections) {
        return new MysqlProvider(
          universalCredential, maxConnections
        );
    }

    public static JdbcProvider from(@NonNull UniversalCredential universalCredential) {
        return from(universalCredential, 4);
    }

    @Override
    public void closeConnection() {
        source.close();
    }

    @Override
    public boolean hasConnection() {
        return openConnection();
    }

    @Override
    public boolean openConnection() {
        try (Connection connection = source.getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException $) {
            $.printStackTrace();
            return false;
        }
    }

    @Override
    public JdbcProvider preOpen() {
        return this;
    }

    @Override
    public <K> K query(@NonNull String query, @NonNull SafetyFunction<ResultSet, K> function, Object... objects) {
        try (Connection connection = source.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                UniversalUtil.syncObjects(statement, objects);

                try (ResultSet set = statement.executeQuery()) {
                    return set != null && set.next()
                      ? function.apply(set)
                      : null;
                }
            }
        } catch (SQLException $) {
            $.printStackTrace();
            return null;
        }
    }

    public <K> List<K> map(@NonNull String query, @NonNull SafetyFunction<ResultSet, K> function, Object... objects) {
        try (Connection connection = source.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                UniversalUtil.syncObjects(statement, objects);

                try (ResultSet set = statement.executeQuery()) {
                    List<K> paramList = new ArrayList<>();

                    while (set.next()) paramList.add(function.apply(set));
                    return paramList;
                }
            }
        } catch (SQLException $) {
            $.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(@NonNull String query, Object... objects) {
        try (Connection connection = source.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                UniversalUtil.syncObjects(statement, objects);
                statement.executeUpdate();
            }
        } catch (SQLException $) {
            $.printStackTrace();
        }
    }

    @Override
    public <T> int[] batch(@NonNull String query, SafetyBiConsumer<T, ComputedBatchQuery> batchFunction, Collection<T> collection) {
        try (Connection connection = source.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                final ComputedBatchQuery batchQuery = new UnitComputedBatchQuery(statement);
                for (T object : collection) {
                    batchFunction.accept(object, batchQuery);
                }
                return statement.executeBatch();
            }
        } catch (SQLException $) {
            $.printStackTrace();
            return new int[0];
        }
    }
}