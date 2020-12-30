/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.test.util;

import dev.king.universal.shared.api.JdbcProvider;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;

import java.util.List;

@Log
@UtilityClass
public final class UniversalMethod {

    public void dispatchProvider(@NonNull JdbcProvider provider) {
        if (!provider.openConnection()) {
            log.severe("No database connection has been established");
            return;
        }

        /*
         * The method update is used to send query that you don't need returns
         */
        provider.update("create table if not exists king(name varchar(1))");
        provider.update("insert into king (name) values (?)", "y");

        /*
         * In the bellow example, we used an simply query to select one user from table
         * The first field is the query, the second field is the lambda set, on third field
         * Is the query objects
         */
        final TestEntity entity = provider.query(
          "select * from king where name = ?",
          set -> new TestEntity(set.getString("name")),
          "y"
        );

        /*
         * Get a map of objects
         */
        List<TestEntity> entities = provider.map(
          "select * from king",
          set -> new TestEntity(set.getString("name"))
        );

        log.info(String.format("Single object: %s", entity));
        log.info(String.format("Object collection: %s", entities));
    }
}
