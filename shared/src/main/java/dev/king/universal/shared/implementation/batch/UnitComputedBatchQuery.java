/*
 * Copyright (c) 2021 yking-projects
 */

package dev.king.universal.shared.implementation.batch;

import dev.king.universal.shared.SQLUtil;
import dev.king.universal.shared.batch.ComputedBatchQuery;
import lombok.Data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Data
public final class UnitComputedBatchQuery implements ComputedBatchQuery {

    private final PreparedStatement statement;

    @Override
    public void compute(Object... objects) throws SQLException {
        SQLUtil.syncObjects(statement, objects);
        statement.addBatch();
    }
}
