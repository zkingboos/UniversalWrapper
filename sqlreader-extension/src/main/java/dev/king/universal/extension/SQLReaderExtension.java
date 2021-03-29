package dev.king.universal.extension;

import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.shared.batch.ComputedBatchQuery;
import dev.king.universal.shared.extension.BaseExtension;
import dev.king.universal.shared.functional.SafetyBiConsumer;
import dev.king.universal.shared.functional.SafetyFunction;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class SQLReaderExtension extends BaseExtension {

    private final SQLReader sqlReader;
    private DefaultSQLSupport defaultSQLSupport;

    public SQLReaderExtension(@NonNull String root, @NonNull String extension) {
        super("SQLReader");
        this.sqlReader = new SQLReader(root, extension);
    }

    public static SQLReaderExtension from(@NonNull String root) {
        return new SQLReaderExtension(root, SQLReader.DEFAULT_EXTENSION);
    }

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
    public int update(@NonNull String path, Object... objects) {
        return defaultSQLSupport.update(sqlReader.getSQL(path), objects);
    }

    @Override
    public <K> List<K> map(@NonNull String path, @NonNull SafetyFunction<ResultSet, K> function, Object... objects) {
        return defaultSQLSupport.map(sqlReader.getSQL(path), function, objects);
    }

    @Override
    public <K> K query(@NonNull String path, @NonNull SafetyFunction<ResultSet, K> consumer, Object... objects) {
        return defaultSQLSupport.query(sqlReader.getSQL(path), consumer, objects);
    }

    @Override
    public <T> int[] batch(@NonNull String path, SafetyBiConsumer<T, ComputedBatchQuery> batchFunction, Collection<T> collection) {
        return defaultSQLSupport.batch(sqlReader.getSQL(path), batchFunction, collection);
    }

    @Override
    public BaseExtension setDefaultSQLSupport(@NonNull DefaultSQLSupport defaultSQLSupport) {
        this.defaultSQLSupport = defaultSQLSupport;
        return this;
    }
}
