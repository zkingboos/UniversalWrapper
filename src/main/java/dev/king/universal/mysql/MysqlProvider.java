/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.mysql;

import com.zaxxer.hikari.HikariDataSource;
import dev.king.universal.UniversalUtil;
import dev.king.universal.api.JdbcProvider;
import dev.king.universal.api.KFunction;
import dev.king.universal.api.mysql.UniversalCredential;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public final class MysqlProvider extends PoolableConnection implements JdbcProvider {

    private final UniversalCredential credentials;
    private final int maxConnections;

    private HikariDataSource source;

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
        try {
            source = obtainDataSource(credentials, maxConnections);
        } catch (SQLException $) {
            $.printStackTrace();
        }
        return this;
    }

    @Override
    public <K> K query(String query, KFunction<ResultSet, K> function, Object... objects) {
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

    public <K> List<K> map(String query, KFunction<ResultSet, K> function, Object... objects) {
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
    public void update(String query, Object... objects) {
        try (Connection connection = source.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                UniversalUtil.syncObjects(statement, objects);
                statement.executeUpdate();
            }
        } catch (SQLException $) {
            $.printStackTrace();
        }
    }
}