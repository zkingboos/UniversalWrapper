/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.api.mysql;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The credential security login to authenticate with mysql
 * Is used to grants secure system.
 */
@Getter
@Builder
@RequiredArgsConstructor
public final class UniversalCredential {

    private final String hostname;
    private final String database;
    private final String user;
    private final String password;
}