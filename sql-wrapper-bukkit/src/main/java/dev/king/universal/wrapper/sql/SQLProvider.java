/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.wrapper.sql;

import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.shared.SQLUtil;
import dev.king.universal.shared.batch.ComputedBatchQuery;
import dev.king.universal.shared.functional.SafetyBiConsumer;
import dev.king.universal.shared.functional.SafetyFunction;
import dev.king.universal.shared.implementation.batch.UnitComputedBatchQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public final class SQLProvider extends DefaultSQLSupport {

    private final File output;
    private Connection connection;

    /**
     * Creates provider to sql
     *
     * @param file path of file
     * @return instance of sql provider
     */
    public static DefaultSQLSupport from(@NonNull File file) {
        return new SQLProvider(file);
    }

    @Override
    public void closeConnection() {
        if (!hasConnection()) return;
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public boolean hasConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean openConnection() {
        try {
            if (hasConnection()) return true;
            if (!output.exists()) return false;

            Class.forName("org.sqlite.JDBC");
            //DriverManager.registerDriver(new JDBC()); //removed for logistic heavy package
            connection = DriverManager.getConnection("jdbc:sqlite:" + output);
            return !connection.isClosed();
        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public <K> K query(@NonNull String query, @NonNull SafetyFunction<ResultSet, K> consumer, Object... objects) {
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            SQLUtil.syncObjects(ps, objects);

            try (ResultSet set = ps.executeQuery()) {
                return set != null && set.next()
                  ? consumer.apply(set)
                  : null;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public int update(@NonNull String query, Object... objects) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            SQLUtil.syncObjects(statement, objects);
            return statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    @Override
    public <K> List<K> map(@NonNull String query, @NonNull SafetyFunction<ResultSet, K> function, Object... objects) {
        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            SQLUtil.syncObjects(statement, objects);
            try (ResultSet set = statement.executeQuery()) {
                List<K> paramResult = new ArrayList<>();
                while (set.next()) {
                    paramResult.add(function.apply(set));
                }

                return paramResult;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> int[] batch(@NonNull String query, SafetyBiConsumer<T, ComputedBatchQuery> batchFunction, Collection<T> collection) {
        try {
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
