package dev.king.jdbc.mysql;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public class SqlCredentials {
    private final String hostname, user, password, database;
}