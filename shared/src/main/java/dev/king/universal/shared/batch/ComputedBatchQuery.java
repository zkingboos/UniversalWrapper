/*
 * Copyright (c) 2021 yking-projects
 */

package dev.king.universal.shared.batch;

import java.sql.SQLException;

public interface ComputedBatchQuery {

    /**
     * Compute the query objects to statement
     *
     * @param objects for each row result object
     * @throws SQLException something went wrong
     */
    void compute(Object... objects) throws SQLException;

}
