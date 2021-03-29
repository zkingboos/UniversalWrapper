package dev.king.universal.wrapper.mariadb;

import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.shared.credential.UniversalCredential;
import dev.king.universal.wrapper.mariadb.implementation.connection.MariaDBPoolableConnection;
import dev.king.universal.wrapper.mysql.MySQLProvider;
import dev.king.universal.wrapper.mysql.credential.MysqlCredential;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class MariaDBProvider extends MySQLProvider {

    public MariaDBProvider(@NonNull UniversalCredential credential, int maxConnections) {
        super(credential, new MariaDBPoolableConnection(), maxConnections);
    }

    /**
     * Creates provider to mysql
     *
     * @param universalCredential login credentials from {@link dev.king.universal.shared.credential.UniversalCredential}
     * @param maxConnections      number of max connections (idle connections are divided by 2)
     * @return instance of mysql provider
     */
    public static DefaultSQLSupport from(@NonNull UniversalCredential universalCredential, int maxConnections) {
        return new MariaDBProvider(
          universalCredential, maxConnections
        );
    }

    /**
     * Creates provider to mysql
     *
     * @param section        {@link JavaPlugin#getConfig()} method
     * @param maxConnections number of max connections (idle connections are divided by 2)
     * @return instance of mysql provider
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
     * @return instance of mysql provider
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
     * @return instance of mysql provider
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
     * @return instance of mysql provider
     */
    public static DefaultSQLSupport from(@NonNull UniversalCredential universalCredential) {
        return from(universalCredential, 4);
    }
}
