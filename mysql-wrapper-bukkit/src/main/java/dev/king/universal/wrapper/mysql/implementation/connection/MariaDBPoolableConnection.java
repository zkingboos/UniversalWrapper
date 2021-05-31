package dev.king.universal.wrapper.mysql.implementation.connection;

import dev.king.universal.wrapper.mysql.HikariPoolableDriver;

public class MariaDBPoolableConnection extends HikariPoolableDriver {

    public MariaDBPoolableConnection() {
        super("jdbc:mariadb://%s/%s", "org.mariadb.jdbc.Driver");
    }
}
