package dev.king.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Has the mysql utilities
 * @author zkingboos_
 */
public class Utilities {

    /**
     * Synchronize the objects param in the prepared statment
     * @param ps is the statment created in jdbc provider
     * @param objects is the vararg parameter of your sql query
     * @throws SQLException requires in the JDBC provider
     */
    public static void syncObjects(
            PreparedStatement ps,
            Object... objects
    ) throws SQLException {
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        for (int i = 1; iterator.hasNext(); i++) {
            ps.setObject(i, iterator.next());
        }
    }
}
