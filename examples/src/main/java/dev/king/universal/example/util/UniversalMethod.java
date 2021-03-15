/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.example.util;

import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.shared.functional.SafetyFunction;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log
@UtilityClass
public final class UniversalMethod {

    /**
     * Example from "unreal" connection step (just for test)
     *
     * @param provider instance of support sql provider
     */
    public void dispatchProvider(@NonNull DefaultSQLSupport provider) {
        if (!provider.openConnection()) {
            log.severe("No database connection has been established");
            return;
        }

        /*
         * The method update is used to send query that you don't needs returns
         */
        provider.update("create table if not exists king(name varchar(255))");

        /*
         * Tho insert single value on mysql
         */
        provider.update("insert into king (name) values (?)", "y");

        final List<TestEntity> testEntities = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            testEntities.add(new TestEntity(UUID.randomUUID().toString()));
        }

        /*
         * To insert a collection object
         */
        final int[] result = provider.batch("insert into king(name) values (?)", (entity, batchQuery) -> {
            batchQuery.compute(entity.getName());
        }, testEntities);

        System.out.println("Batched " + result.length);

        /*
         * In the bellow example, we used an simply query to select one user from table, following these instructions:
         * String query: query that will sent to driver connector </br>
         * SafetyFunction<ResultSet, K> consumer: {@link java.util.stream.Stream}({@link FunctionalInterface}) class, currently used to parse result set to a object </br>
         * Object... objects: their respectively fields are set in order, represented by a '?'
         */
        final TestEntity entity = provider.query(
          "select name from king where name = ?",
          set -> new TestEntity(set.getString("name")),
          "y"
        );

        /*
         * Map a collection's object, returned from {@link DefaultSQLSupport#map(String, SafetyFunction, Object...)}
         */
        final List<TestEntity> entities = provider.map(
          "select name from king",
          set -> new TestEntity(set.getString("name"))
        );

        log.info(String.format("Single object: %s", entity));
        log.info(String.format("Object collection: %s", entities));
    }
}
