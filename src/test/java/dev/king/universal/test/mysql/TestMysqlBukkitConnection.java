/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.test.mysql;

import dev.king.universal.UniversalWrapper;
import dev.king.universal.api.JdbcProvider;
import dev.king.universal.api.mysql.UniversalCredential;
import dev.king.universal.test.common.UniversalMethod;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestMysqlBukkitConnection extends JavaPlugin {

    @Override
    public void onEnable() {
        final FileConfiguration configuration = getConfig();
        final ConfigurationSection section = configuration.getConfigurationSection("connection.mysql");

        assert section != null;
        final UniversalCredential universalCredential = UniversalCredential.fromConfiguration(section);
        final JdbcProvider provider = UniversalWrapper
          .newMysqlProvider(universalCredential, 2)
          .preOpen();

        UniversalMethod.dispatchProvider(provider);
    }
}
