package dev.king.jdbc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public class SqlCredentials {
    private final String hostname, user, password, database;
}