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
import dev.king.universal.shared.functional.SafetyFunction;
import dev.king.universal.shared.implementation.batch.UnitComputedBatchQuery;
import dev.king.universal.wrapper.mysql.credential.MysqlCredential;
import dev.king.universal.wrapper.mysql.implementation.connection.DefaultPoolableConnection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Data
public class MySQLProvider extends DefaultSQLSupport {

    private final UniversalCredential credential;
    private final int maxConnections;
    private final PoolableProvider poolableProvider;
    private final HikariDataSource source;

    public MySQLProvider(@NonNull UniversalCredential credential, @NonNull PoolableProvider defaultPoolableConnection, int maxConnections) {
        this.poolableProvider = defaultPoolableConnection;
        this.credential = credential;
        this.maxConnections = maxConnections;
        this.source = poolableProvider.obtainDataSource(credential, maxConnections);
    }

    public MySQLProvider(@NonNull UniversalCredential credential, int maxConnections) {
        this(credential, new DefaultPoolableConnection(), maxConnections);
    }

    /**
     * Creates provider to mysql
     *
     * @param universalCredential login credentials from {@link dev.king.universal.shared.credential.UniversalCredential}
     * @param maxConnections      number of max connections (idle connections are divided by 2)
     * @return instance from desired support provider
     */
    public static DefaultSQLSupport from(@NonNull UniversalCredential universalCredential, int maxConnections) {
        return new MySQLProvider(
          universalCredential, maxConnections
        );
    }

    /**
     * Creates provider to mysql
     *
     * @param section        {@link JavaPlugin#getConfig()} method
     * @param maxConnections number of max connections (idle connections are divided by 2)
     * @return instance from desired support provider
     */
    public static DefaultSQLSupport fromConfiguration(@NonNull ConfigurationSection section, int maxConnections) {
        return from(MysqlCredential.fromConfiguration(section), maxConnections);
    }

    /**
     * Creates provider to mysql
     *
     * @param plugin         {@link JavaPlugin} instance
     * @param path           configuration section path
     * @param maxConnections number of max connections (idle connections are divided by 2)
     * @return instance from desired support provider
     */
    public static DefaultSQLSupport fromPlugin(@NonNull Plugin plugin, @NonNull String path, int maxConnections) {
        return fromConfiguration(Objects.requireNonNull(plugin.getConfig().getConfigurationSection(path)), maxConnections);
    }

    /**
     * Creates provider to mysql
     *
     * @param hostname       target
     * @param database       target
     * @param user           target
     * @param password       target
     * @param maxConnections number of max connections (idle connections are divided by 2)
     * @return instance from desired support provider
     */
    public static DefaultSQLSupport from(@NonNull String hostname, @NonNull String database, @NonNull String user, @NonNull String password, int maxConnections) {
        return from(MysqlCredential.builder()
            .hostname(hostname)
            .database(database)
            .user(user)
            .password(password)
            .build(),
          maxConnections);
    }

    /**
     * Creates provider to mysql
     *
     * @param universalCredential login credentials from {@link dev.king.universal.shared.credential.UniversalCredential}
     * @return instance from desired support provider
     */
    public static DefaultSQLSupport from(@NonNull UniversalCredential universalCredential) {
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
    public <T> int[] batch(@NonNull String query, SafetyBiConsumer<T, ComputedBatchQuery> batchFunction, Collection<T> collection) {
        try (Connection connection = source.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                final ComputedBatchQuery batchQuery = new UnitComputedBatchQuery(statement);
                for (T object : collection) {
                    batchFunction.accept(object, batchQuery);
                }
                return statement.executeBatch();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return new int[0];
        }
    }
}