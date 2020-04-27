package dev.king.universal;

import dev.king.universal.api.JdbcProvider;
import dev.king.universal.mysql.MysqlProvider;
import dev.king.universal.mysql.UniversalCredentials;
import dev.king.universal.sql.SqlProvider;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class UniversalWrapper {

    private static final ExecutorService UNIVERSAL_EXECUTOR = Executors.newFixedThreadPool(2);

    public JdbcProvider newMysqlProvider(UniversalCredentials credentials, int maxConnections) {
        return new MysqlProvider(
                credentials, maxConnections, UNIVERSAL_EXECUTOR
        );
    }

    public JdbcProvider newSqlProvider(File file) {
        return new SqlProvider(
                file, UNIVERSAL_EXECUTOR
        );
    }
}
