/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.shared;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This class providers basic methods
 */
@UtilityClass
public final class UniversalUtil {

    /**
     * Synchronize the objects param in the prepared statement
     *
     * @param statement is the statement created in universal provider
     * @param objects   is the vararg parameter of your sql query
     * @throws SQLException requires in the JDBC provider
     */
    public void syncObjects(@NonNull PreparedStatement statement, Object... objects) throws SQLException {
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        for (int i = 1; iterator.hasNext(); i++) {
            statement.setObject(i, iterator.next());
        }
    }
}
