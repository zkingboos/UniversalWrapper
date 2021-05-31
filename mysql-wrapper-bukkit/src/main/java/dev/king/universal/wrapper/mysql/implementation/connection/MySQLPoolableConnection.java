/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.wrapper.mysql.implementation.connection;

import dev.king.universal.wrapper.mysql.HikariPoolableDriver;

public class MySQLPoolableConnection extends HikariPoolableDriver {
    public MySQLPoolableConnection() {
        super("jdbc:mysql://%s/%s", "com.mysql.jdbc.Driver");
    }
}
