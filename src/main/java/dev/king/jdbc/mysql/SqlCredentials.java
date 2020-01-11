package dev.king.jdbc.mysql;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The credentials of mysql
 * @author zkingboos_
 */
@RequiredArgsConstructor @Getter
public class SqlCredentials {
    private final String hostname, user, password, database;
}