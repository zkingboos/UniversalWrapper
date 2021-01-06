/*
 * Copyright (c) 2021 yking-projects
 */

package dev.king.universal.shared.api.batch.impl;

import dev.king.universal.shared.UniversalUtil;
import dev.king.universal.shared.api.batch.ComputedBatchQuery;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@RequiredArgsConstructor
public final class UnitComputedBatchQuery implements ComputedBatchQuery {

    private final PreparedStatement statement;

    @Override
    public void compute(Object... objects) throws SQLException {
        UniversalUtil.syncObjects(statement, objects);
        statement.addBatch();
    }
}
