/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This class providers basic methods
 */
public final class UniversalUtil {

    /**
     * Synchronize the objects param in the prepared statement
     *
     * @param statement is the statement created in universal provider
     * @param objects   is the vararg parameter of your sql query
     * @throws SQLException requires in the JDBC provider
     */
    public static void syncObjects(PreparedStatement statement, Object... objects) throws SQLException {
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        for (int i = 1; iterator.hasNext(); i++) {
            final Object object = iterator.next();
            statement.setObject(i, object);
        }
    }
}
