/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.shared;

import lombok.Getter;
import lombok.experimental.UtilityClass;

/**
 * Abstract provider
 *
 * @author ykingboos
 */
@UtilityClass
@Deprecated(since = "4.0.0-SNAPSHOT")
public final class UniversalWrapper {

    @Deprecated
    @Getter(lazy = true)
    private static final UniversalWrapper instance = new UniversalWrapper();

    /**
     * Creates provider to mysql
     *
     * @param credentials    login credentials
     * @param maxConnections number of max connections (idle connections are divided by 2)
     * @return instance of mysql provider
     */
//    @Deprecated
//    public JdbcProvider newMysqlProvider(@NonNull UniversalCredential credentials, int maxConnections) {
//        return new MysqlProvider(
//          credentials, maxConnections
//        );
//    }

    /**
     * Creates provider to sql
     *
     * @param file path of file
     * @return instance of sql provider
     */
//    public JdbcProvider newSqlProvider(@NonNull File file) {
//        return new SqlProvider(
//          file
//        );
//    }
}
