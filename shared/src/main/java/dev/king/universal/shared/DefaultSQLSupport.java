/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.shared;

import dev.king.universal.shared.batch.ComputedBatchQuery;
import dev.king.universal.shared.functional.SafetyBiConsumer;
import dev.king.universal.shared.functional.SafetyFunction;
import dev.king.universal.shared.properties.PropertiesSupport;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

/**
 * Default support provider for SQL & MYSQL drivers.
 */
@Data
@Accessors(fluent = true)
public abstract class DefaultSQLSupport implements AutoCloseable {

    private final PropertiesSupport properties;

    public DefaultSQLSupport() {
        this.properties = new PropertiesSupport(this);
    }

    /**
     * Connect to mysql server
     *
     * @return if has a valid connection
     */
    public abstract boolean openConnection();

    /**
     * Close the all connections of jdbc
     */
    public abstract void closeConnection();

    /**
     * Verify if the connections is valid
     *
     * @return if an any valid connection
     */
    public abstract boolean hasConnection();

    /**
     * Uses just in create, delete, insert and update queries
     *
     * @param query   the query of mysql
     * @param objects the objects that will be put in the prepared statement
     * @return query response
     */
    public abstract int update(@NonNull String query, Object... objects);

    /**
     * Uses just in select query
     *
     * @param query    the query of mysql
     * @param function if has a valid entry, functional interface {@link SafetyFunction} will be called and returns a result
     * @param objects  the objects that will be put in the prepared statement
     * @param <K>      the generic type, used to return your prefer value
     * @return returns a optional value, applied in function parameter
     */
    public abstract <K> List<K> map(@NonNull String query, @NonNull SafetyFunction<ResultSet, K> function, Object... objects);

    /**
     * Uses just in select query
     *
     * @param query    the query of mysql
     * @param consumer if has a valid entry, functional interface {@link SafetyFunction} will be called and returns a result
     * @param objects  the objects that will be put in the prepared statement
     * @param <K>      the generic type, used to return your prefer value
     * @return returns a optional value, applied in function parameter
     */
    public abstract <K> K query(@NonNull String query, @NonNull SafetyFunction<ResultSet, K> consumer, Object... objects);

    /**
     * Execute massive update
     *
     * @param query         the query of mysql
     * @param batchFunction to compute values from collection
     * @param collection    entry of computable objects
     * @param <K>           type of objects
     * @return result of batch
     */
    public abstract <K> int[] batch(@NonNull String query, SafetyBiConsumer<K, ComputedBatchQuery> batchFunction, Collection<K> collection);

    /**
     * Closes the connection, but automatically
     */
    @Override
    public void close() {
        closeConnection();
    }
}