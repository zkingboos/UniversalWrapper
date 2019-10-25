/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.sql;

import dev.king.universal.UniversalUtil;
import dev.king.universal.api.JdbcProvider;
import dev.king.universal.api.KFunction;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.sqlite.JDBC;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public final class SqlProvider implements JdbcProvider {

    private final File output;
    private Connection connection;

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

            DriverManager.registerDriver(new JDBC());
            connection = DriverManager.getConnection(
              "jdbc:sqlite:" + output
            );

            return !connection.isClosed();
        } catch (SQLException $) {
            $.printStackTrace();
            return false;
        }
    }

    @Override
    public JdbcProvider preOpen() {
        //TODO: doesn't nothing
        return this;
    }

    @Override
    public <K> K query(String query, KFunction<ResultSet, K> consumer, Object... objects) {
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
    public void update(String query, Object... objects) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            UniversalUtil.syncObjects(statement, objects);

            statement.executeUpdate();
        } catch (SQLException $) {
            $.printStackTrace();
        }
    }

    @Override
    public <K> List<K> map(String query, KFunction<ResultSet, K> function, Object... objects) {
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
}
