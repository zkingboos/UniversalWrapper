/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.example.bukkit;

import dev.king.universal.example.util.UniversalMethod;
import dev.king.universal.shared.api.JdbcProvider;
import dev.king.universal.shared.api.credential.UniversalCredential;
import dev.king.universal.wrapper.mysql.MysqlProvider;
import dev.king.universal.wrapper.mysql.api.MysqlCredential;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestMysqlBukkitConnection extends JavaPlugin {

    @Override
    public void onEnable() {
        final FileConfiguration configuration = getConfig();
        final ConfigurationSection section = configuration.getConfigurationSection("connection.mysql");

        assert section != null;
        final UniversalCredential universalCredential = MysqlCredential.fromConfiguration(section);
        final JdbcProvider provider = MysqlProvider.from(universalCredential, 2);

        UniversalMethod.dispatchProvider(provider);
    }
}
