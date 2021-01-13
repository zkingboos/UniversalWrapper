/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.wrapper.sql;

import dev.king.universal.shared.UniversalUtil;
import dev.king.universal.shared.api.JdbcProvider;
import dev.king.universal.shared.api.batch.ComputedBatchQuery;
import dev.king.universal.shared.api.batch.impl.UnitComputedBatchQuery;
import dev.king.universal.shared.api.functional.SafetyBiConsumer;
import dev.king.universal.shared.api.functional.SafetyFunction;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public final class SqlProvider implements JdbcProvider {

    private final File output;
    private Connection connection;

    /**
     * Creates provider to sql
     *
     * @param file path of file
     * @return instance of sql provider
     */
    public static JdbcProvider from(@NonNull File file) {
        return new SqlProvider(
          file
        );
    }

    @Override
    public void closeConnection() {
        if (!hasConnection()) return;

        try {
            connection.close();
        } catch (SQLException $) {
            $.printStackTrace();
        }
    }

    @SneakyThrows
    public boolean hasConnection() {
        return connection != null && !connection.isClosed();
    }

    @Override
    public boolean openConnection() {
        try {
            if (hasConnection()) return true;
            if (!output.exists()) return false;

            Class.forName("org.sqlite.JDBC");
            //DriverManager.registerDriver(new JDBC()); //removed for logistic heavy package
            connection = DriverManager.getConnection(
              "jdbc:sqlite:" + output
            );

            return !connection.isClosed();
        } catch (SQLException | ClassNotFoundException $) {
            $.printStackTrace();
            return false;
        }
    }

    @Override
    public JdbcProvider preOpen() {
        //TODO: don't do nothing cuz this is unnecessary
        return this;
    }

    @Override
    public <K> K query(@NonNull String query, @NonNull SafetyFunction<ResultSet, K> consumer, Object... objects) {
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            UniversalUtil.syncObjects(ps, objects);

            try (ResultSet set = ps.executeQuery()) {
                return set != null && set.next()
                  ? consumer.apply(set)
                  : null;
            }
        } catch (SQLException $) {
            $.printStackTrace();
            return null;
        }
    }

    @Override
    public int update(@NonNull String query, Object... objects) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            UniversalUtil.syncObjects(statement, objects);
            return statement.executeUpdate();
        } catch (SQLException $) {
            $.printStackTrace();
            return -1;
        }
    }

    @Override
    public <K> List<K> map(@NonNull String query, @NonNull SafetyFunction<ResultSet, K> function, Object... objects) {
        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            UniversalUtil.syncObjects(statement, objects);
            try (ResultSet set = statement.executeQuery()) {
                List<K> paramResult = new ArrayList<>();
                while (set.next()) {
                    paramResult.add(function.apply(set));
                }

                return paramResult;
            }
        } catch (SQLException $) {
            $.printStackTrace();
            return null;
        }
    }

    @Override
    @SneakyThrows
    public <T> int[] batch(@NonNull String query, SafetyBiConsumer<T, ComputedBatchQuery> batchFunction, Collection<T> collection) {
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
        } catch (SQLException $) {
            $.printStackTrace();
            return new int[0];
        }
    }
}
