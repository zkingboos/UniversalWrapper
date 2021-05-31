package dev.king.universal.wrapper.mysql.implementation.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.king.universal.shared.credential.UniversalCredential;
import dev.king.universal.wrapper.mysql.HikariPoolableDriver;
import lombok.NonNull;

public class H2PoolableConnection extends HikariPoolableDriver {

    public H2PoolableConnection() {
        super("jdbc:h2:mem:%s%s", "org.h2.Driver");
    }
}
