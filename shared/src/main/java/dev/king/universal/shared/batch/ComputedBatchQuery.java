/*
 * Copyright (c) 2021 yking-projects
 */

package dev.king.universal.shared.batch;

import java.sql.SQLException;

public interface ComputedBatchQuery {

    void compute(Object... objects) throws SQLException;

}
