package dev.king.universal.wrapper.mysql;

import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.wrapper.mysql.implementation.connection.MySQLPoolableConnection;
import dev.king.universal.wrapper.mysql.implementation.credential.MysqlCredential;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import net.md_5.bungee.config.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

@Data
@Accessors(fluent = true)
public class MySQLProviderBuilder {

    private int maxConnections = 4;
    private MysqlCredential credential;
    private HikariPoolableDriver driver = new MySQLPoolableConnection();

    public MySQLProviderBuilder fromConfiguration(@NonNull ConfigurationSection section) {
        this.credential = MysqlCredential.fromConfiguration(section);
        return this;
    }

    public MySQLProviderBuilder fromPlugin(@NonNull Plugin plugin, @NonNull String path) {
        this.credential = MysqlCredential.fromPlugin(plugin, path);
        return this;
    }

    public MySQLProviderBuilder fromConfiguration(@NonNull Configuration configuration) {
        this.credential = MysqlCredential.fromConfiguration(configuration);
        return this;
    }

    public DefaultSQLSupport build() {
        return new MySQLProvider(credential, driver, maxConnections);
    }
}
