package dev.king.universal.wrapper.mariadb.implementation.connection;

import dev.king.universal.wrapper.mysql.implementation.connection.DefaultPoolableConnection;

public class MariaDBPoolableConnection extends DefaultPoolableConnection {

    public MariaDBPoolableConnection() {
        super("jdbc:mariadb://%s/%s", "org.mariadb.jdbc.Driver");
    }
}
