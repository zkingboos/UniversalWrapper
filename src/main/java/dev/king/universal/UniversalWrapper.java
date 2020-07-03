package dev.king.universal;

import dev.king.universal.api.JdbcProvider;
import dev.king.universal.api.mysql.UniversalCredentials;
import dev.king.universal.mysql.MysqlProvider;
import dev.king.universal.sql.SqlProvider;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Abstract provider
 *
 * @author zkingboos
 */
public final class UniversalWrapper {

    private final ExecutorService universalExecutor = Executors.newFixedThreadPool(2);

    /**
     * Creates provider to mysql
     *
     * @param credentials    login credentials
     * @param maxConnections number of max connections (idle connections are divided by 2)
     * @return instance of mysql provider
     */
    public JdbcProvider newMysqlProvider(UniversalCredentials credentials, int maxConnections) {
        return new MysqlProvider(
                credentials, maxConnections, universalExecutor
        );
    }

    /**
     * Creates provider to sql
     *
     * @param file path of file
     * @return instance of sql provider
     */
    public JdbcProvider newSqlProvider(File file) {
        return new SqlProvider(
                file, universalExecutor
        );
    }

    /**
     * Close the instance of executor service
     */
    public void closeService() {
        universalExecutor.shutdown();
    }
}
