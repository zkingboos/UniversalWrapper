/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.api.mysql;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

/**
 * The credential security login to authenticate with mysql
 * Is used to grants secure system.
 */
@Getter
@Builder
@RequiredArgsConstructor
public final class UniversalCredential {

    private final @NonNull String hostname;
    private final @NonNull String database;
    private final @NonNull String user;
    private final @NonNull String password;

    public static UniversalCredential fromConfiguration(@NonNull ConfigurationSection section) {
        return builder()
          .hostname(section.getString("hostname"))
          .database(section.getString("database"))
          .user(section.getString("user"))
          .password(section.getString("password"))
          .build();
    }
}