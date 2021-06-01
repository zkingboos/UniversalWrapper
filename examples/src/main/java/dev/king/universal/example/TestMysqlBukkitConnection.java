/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.example;

import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.shared.credential.UniversalCredential;
import dev.king.universal.wrapper.mysql.MySQLProvider;
import dev.king.universal.wrapper.mysql.implementation.credential.MysqlCredential;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestMysqlBukkitConnection extends JavaPlugin {

    /**
     * Initialize mysql connection, using UniversalWrapper project on bukkit environment
     * <p>
     * Create {@link UniversalCredential} from {@link ConfigurationSection}, using {@link MysqlCredential#fromConfiguration(ConfigurationSection)}
     * you can also use {@link dev.king.universal.wrapper.mysql.MySQLProviderBuilder#fromPlugin(Plugin, String)} to compact inline-method.
     */
    @Override
    public void onEnable() {
        saveDefaultConfig();

        final ConfigurationSection section = getConfig().getConfigurationSection("connection.mysql");
        assert section != null;

        final DefaultSQLSupport defaultSQLSupport = MySQLProvider
          .builder()
          .fromPlugin(this, "connection.mysql")
          .build();

        DispatchMethod.dispatchProvider(defaultSQLSupport);
    }
}
