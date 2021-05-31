/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.wrapper.mysql;

import com.zaxxer.hikari.HikariDataSource;
import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.shared.SQLUtil;
import dev.king.universal.shared.batch.ComputedBatchQuery;
import dev.king.universal.shared.credential.UniversalCredential;
import dev.king.universal.shared.functional.SafetyBiConsumer;
import dev.king.universal.shared.functional.SafetyConsumer;
import dev.king.universal.shared.functional.SafetyFunction;
import dev.king.universal.shared.implementation.batch.UnitComputedBatchQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class MySQLProvider extends DefaultSQLSupport {

    private final static int RETURN_GENERATED_KEYS = Statement.RETURN_GENERATED_KEYS;

    public static MySQLProviderBuilder builder() {
        return new MySQLProviderBuilder();
    }

    private final UniversalCredential credential;
    private final int maxConnections;
    private final HikariPoolableDriver poolableProvider;
    private final HikariDataSource source;

    public MySQLProvider(@NonNull UniversalCredential credential, @NonNull HikariPoolableDriver defaultPoolableConnection, int maxConnections) {
        this.poolableProvider = defaultPoolableConnection;
        this.credential = credential;
        this.maxConnections = maxConnections;
        this.source = poolableProvider.dataSource(credential, maxConnections);
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
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public <K> K query(@NonNull String query, @NonNull SafetyFunction<ResultSet, K> function, Object... objects) {
        try (Connection connection = source.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                SQLUtil.syncObjects(statement, objects);

                try (ResultSet set = statement.executeQuery()) {
                    return set != null && set.next()
                      ? function.apply(set)
                      : null;
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public <K> List<K> map(@NonNull String query, @NonNull SafetyFunction<ResultSet, K> function, Object... objects) {
        try (Connection connection = source.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                SQLUtil.syncObjects(statement, objects);

                try (ResultSet set = statement.executeQuery()) {
                    List<K> paramList = new LinkedList<>();
                    while (set.next()) paramList.add(function.apply(set));
                    return paramList;
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public int update(@NonNull String query, Object... objects) {
        try (Connection connection = source.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                SQLUtil.syncObjects(statement, objects);
                return statement.executeUpdate();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    @Override
    public int update(@NonNull String query, @NonNull SafetyConsumer<ResultSet> safetyConsumer, Object... objects) {
        try (Connection connection = source.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query, RETURN_GENERATED_KEYS)) {
                SQLUtil.syncObjects(statement, objects);
                int result = statement.executeUpdate();
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    safetyConsumer.accept(resultSet);
                }
                return result;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    @Override
    public <T> int[] batch(@NonNull String query, SafetyBiConsumer<T, ComputedBatchQuery> batchFunction, Collection<T> collection) {
        try (Connection connection = source.getConnection()) {
            final boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                final ComputedBatchQuery batchQuery = new UnitComputedBatchQuery(statement);
                for (T object : collection) {
                    batchFunction.accept(object, batchQuery);
                }
                final int[] results = statement.executeBatch();
                connection.commit();
                connection.setAutoCommit(autoCommit);
                return results;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return new int[0];
        }
    }
}