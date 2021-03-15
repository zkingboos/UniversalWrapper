package dev.king.universal.extension;

import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.shared.batch.ComputedBatchQuery;
import dev.king.universal.shared.extension.ExtensionSupport;
import dev.king.universal.shared.functional.SafetyBiConsumer;
import dev.king.universal.shared.functional.SafetyFunction;
import lombok.NonNull;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

public class SQLReaderExtension extends ExtensionSupport implements DefaultSQLSupport {

    public SQLReaderExtension() {
        super("SQLReader");
    }

    private DefaultSQLSupport support;

    @Override
    public boolean openConnection() {
        return support.openConnection();
    }

    @Override
    public void closeConnection() {
        support.closeConnection();
    }

    @Override
    public boolean hasConnection() {
        return support.hasConnection();
    }

    @Override
    public int update(@NonNull String query, Object... objects) {
        return support.update(query, objects);
    }

    @Override
    public <K> List<K> map(@NonNull String query, @NonNull SafetyFunction<ResultSet, K> function, Object... objects) {
        return support.map(query, function, objects);
    }

    @Override
    public <K> K query(@NonNull String query, @NonNull SafetyFunction<ResultSet, K> consumer, Object... objects) {
        return support.query(query, consumer, objects);
    }

    @Override
    public <T> int[] batch(@NonNull String query, SafetyBiConsumer<T, ComputedBatchQuery> batchFunction, Collection<T> collection) {
        return support.batch(query, batchFunction, collection);
    }

    @Override
    public DefaultSQLSupport selfInstall(@NonNull DefaultSQLSupport support) {
        super.selfInstall(support);
        this.support = support;
        return this;
    }
}
