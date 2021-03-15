/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.shared;

import dev.king.universal.shared.batch.ComputedBatchQuery;
import dev.king.universal.shared.extension.ExtensionSupport;
import dev.king.universal.shared.functional.SafetyBiConsumer;
import dev.king.universal.shared.functional.SafetyFunction;
import lombok.NonNull;
import org.intellij.lang.annotations.Language;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

/**
 * Default support provider for SQL & MYSQL drivers.
 */
public interface DefaultSQLSupport extends AutoCloseable {

    /**
     * Connect to mysql server
     *
     * @return if has a valid connection@
     */
    boolean openConnection();

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
     * @param objects the objects that will be put in the prepared statement
     * @return query response
     */
    int update(@Language("MySQL") @NonNull String query, Object... objects);

    /**
     * Uses just in select query
     *
     * @param query    the query of mysql
     * @param function if has a valid entry, functional interface {@link SafetyFunction} will be called and returns a result
     * @param objects  the objects that will be put in the prepared statement
     * @param <K>      the generic type, used to return your prefer value
     * @return returns a optional value, applied in function parameter
     */
    <K> List<K> map(@Language("MySQL") @NonNull String query, @NonNull SafetyFunction<ResultSet, K> function, Object... objects);

    /**
     * Uses just in select query
     *
     * @param query    the query of mysql
     * @param consumer if has a valid entry, functional interface {@link SafetyFunction} will be called and returns a result
     * @param objects  the objects that will be put in the prepared statement
     * @param <K>      the generic type, used to return your prefer value
     * @return returns a optional value, applied in function parameter
     */
    <K> K query(@Language("MySQL") @NonNull String query, @NonNull SafetyFunction<ResultSet, K> consumer, Object... objects);

    /**
     * Execute massive update
     *
     * @param query         the query of mysql
     * @param batchFunction to compute values from collection
     * @param collection    entry of computable objects
     * @param <T>           type of objects
     * @return result of batch
     */
    <T> int[] batch(@Language("MySQL") @NonNull String query, SafetyBiConsumer<T, ComputedBatchQuery> batchFunction, Collection<T> collection);

    /**
     * Closes the connection, but automatically
     */
    @Override
    default void close() {
        closeConnection();
    }

    /**
     * Install extension, for class modifications and others things
     *
     * @return wrapper instance
     */
    default DefaultSQLSupport install(@NonNull ExtensionSupport extensionSupport) {
        return extensionSupport.selfInstall(this);
    }
}