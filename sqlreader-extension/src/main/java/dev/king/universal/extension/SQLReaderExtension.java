package dev.king.universal.extension;

import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.shared.batch.ComputedBatchQuery;
import dev.king.universal.shared.extension.BaseExtension;
import dev.king.universal.shared.functional.SafetyBiConsumer;
import dev.king.universal.shared.functional.SafetyFunction;
import lombok.Getter;
import lombok.NonNull;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

public class SQLReaderExtension implements BaseExtension {

    @Getter
    private DefaultSQLSupport defaultSQLSupport;

    @Override
    public boolean openConnection() {
        return defaultSQLSupport.openConnection();
    }

    @Override
    public void closeConnection() {
        defaultSQLSupport.closeConnection();
    }

    @Override
    public boolean hasConnection() {
        return defaultSQLSupport.hasConnection();
    }

    @Override
    public int update(@NonNull String query, Object... objects) {
        System.out.println("fuck nerdola");
        return defaultSQLSupport.update(query, objects);
    }

    @Override
    public <K> List<K> map(@NonNull String query, @NonNull SafetyFunction<ResultSet, K> function, Object... objects) {
        return defaultSQLSupport.map(query, function, objects);
    }

    @Override
    public <K> K query(@NonNull String query, @NonNull SafetyFunction<ResultSet, K> consumer, Object... objects) {
        return defaultSQLSupport.query(query, consumer, objects);
    }

    @Override
    public <T> int[] batch(@NonNull String query, SafetyBiConsumer<T, ComputedBatchQuery> batchFunction, Collection<T> collection) {
        return defaultSQLSupport.batch(query, batchFunction, collection);
    }

    @Override
    public String getName() {
        return "SQLReader";
    }

    @Override
    public BaseExtension setDefaultSQLSupport(@NonNull DefaultSQLSupport defaultSQLSupport) {
        this.defaultSQLSupport = defaultSQLSupport;
        return this;
    }
}
