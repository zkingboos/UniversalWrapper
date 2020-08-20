package dev.king.universal;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This class providers basic methods
 *
 * @author zkingboos_
 */
public final class Utility {

    /**
     * Synchronize the objects param in the prepared statement
     *
     * @param ps      is the statement created in universal provider
     * @param objects is the vararg parameter of your sql query
     * @throws SQLException requires in the JDBC provider
     */
    public static void syncObjects(PreparedStatement ps, Object... objects) throws SQLException {
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        for (int i = 1; iterator.hasNext(); i++) {
            final Object obj = iterator.next();
            ps.setObject(i, obj);
        }
    }
}
