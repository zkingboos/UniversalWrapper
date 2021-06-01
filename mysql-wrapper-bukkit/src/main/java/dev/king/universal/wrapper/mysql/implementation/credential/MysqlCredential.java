/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.wrapper.mysql.implementation.credential;

import dev.king.universal.shared.credential.UniversalCredential;
import lombok.*;
import net.md_5.bungee.config.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * The credential security login to authenticate with mysql
 * Is used to grants secure system.
 */
@Data
@Builder
public final class MysqlCredential implements UniversalCredential {

    public static final MysqlCredential EMPTY = new MysqlCredential("", "", "", "");

    private final String hostname;
    private final String database;
    private final String user;
    private final String password;

    /**
     * Create {@link MysqlCredential} from a {@link org.bukkit.configuration.ConfigurationSection}
     *
     * @param section instance of section
     * @return credential security
     */
    public static MysqlCredential fromConfiguration(@NonNull ConfigurationSection section) {
        return builder()
          .hostname(Objects.requireNonNull(section.getString("hostname")))
          .database(Objects.requireNonNull(section.getString("database")))
          .user(Objects.requireNonNull(section.getString("user")))
          .password(Objects.requireNonNull(section.getString("password")))
          .build();
    }

    /**
     * Creates provider to mysql
     *
     * @param plugin {@link JavaPlugin} instance
     * @param path   configuration section path
     * @return instance from desired support provider
     */
    public static MysqlCredential fromPlugin(@NonNull Plugin plugin, @NonNull String path) {
        return fromConfiguration(Objects.requireNonNull(plugin.getConfig().getConfigurationSection(path)));
    }

    /**
     * Create {@link MysqlCredential} from a {@link Configuration}
     *
     * @param configuration instance of bungee configuration
     * @return credential security
     */
    public static MysqlCredential fromConfiguration(@NonNull Configuration configuration) {
        return builder()
          .hostname(configuration.getString("hostname"))
          .database(configuration.getString("database"))
          .user(configuration.getString("user"))
          .password(configuration.getString("password"))
          .build();
    }
}