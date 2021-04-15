/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.wrapper.mysql.implementation.credential;

import dev.king.universal.shared.credential.UniversalCredential;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.config.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

/**
 * The credential security login to authenticate with mysql
 * Is used to grants secure system.
 */
@Getter
@Builder
@RequiredArgsConstructor
public final class MysqlCredential implements UniversalCredential {

    private final @NonNull String hostname;
    private final @NonNull String database;
    private final @NonNull String user;
    private final @NonNull String password;

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