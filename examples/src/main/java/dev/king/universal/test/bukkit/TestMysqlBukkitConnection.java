/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.test.bukkit;

import dev.king.universal.shared.api.JdbcProvider;
import dev.king.universal.shared.api.credential.UniversalCredential;
import dev.king.universal.test.util.UniversalMethod;
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
        final JdbcProvider provider = MysqlProvider
          .from(universalCredential, 2)
          .preOpen();

        UniversalMethod.dispatchProvider(provider);
    }
}
