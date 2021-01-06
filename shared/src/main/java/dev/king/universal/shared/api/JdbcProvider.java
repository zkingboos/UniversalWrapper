/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.shared.api;

import dev.king.universal.shared.api.batch.ComputedBatchQuery;
import dev.king.universal.shared.api.functional.SafetyBiConsumer;
import dev.king.universal.shared.api.functional.SafetyFunction;
import lombok.NonNull;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

/**
 * The interface has provide basic methods
 */
public interface JdbcProvider {

    /**
     * Connect to mysql server
     *
     * @return if has a valid connection
     */
    boolean openConnection();

    /**
     * Used to pre configure instance settings
     *
     * @return instance of current {@link dev.king.universal.shared.api.JdbcProvider}
     */
    @Deprecated
    JdbcProvider preOpen();

    /**
     * Close the all connections of jdbc
     */
    void closeConnection();

    /**
     * Verify if the connections is valid
     *
     * @return if an any valid connection
     */
    boolean hasConnection();

    /**
     * Uses just in create, delete, insert and update queries
     *
     * @param query   the query of mysql
     * @param objects the objects that will be putted in the prepared statement
     */
    void update(@NonNull String query, Object... objects);

    /**
     * Uses just in select query
     *
     * @param query    the query of mysql
     * @param function if has a valid entry, functional interface {@link SafetyFunction} will be called and returns a result
     * @param objects  the objects that will be put in the prepared statement
     * @param <K>      the generic type, used to return your prefer value
     * @return returns a optional value, applied in function parameter
     */
    <K> List<K> map(@NonNull String query, @NonNull SafetyFunction<ResultSet, K> function, Object... objects);

    /**
     * Uses just in select query
     *
     * @param query    the query of mysql
     * @param consumer if has a valid entry, functional interface {@link SafetyFunction} will be called and returns a result
     * @param objects  the objects that will be put in the prepared statement
     * @param <K>      the generic type, used to return your prefer value
     * @return returns a optional value, applied in function parameter
     */
    <K> K query(@NonNull String query, @NonNull SafetyFunction<ResultSet, K> consumer, Object... objects);

    /**
     * Execute massive update
     *
     * @param query         the query of mysql
     * @param batchFunction to compute values from collection
     * @param collection    entry of computable objects
     * @param <T>           type of objects
     * @return result of batch
     */
    <T> int[] batch(@NonNull String query, SafetyBiConsumer<T, ComputedBatchQuery> batchFunction, Collection<T> collection);
}