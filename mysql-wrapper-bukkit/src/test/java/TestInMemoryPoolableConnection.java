import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.king.universal.shared.credential.UniversalCredential;
import dev.king.universal.wrapper.mysql.PoolableProvider;
import lombok.NonNull;

public class TestInMemoryPoolableConnection implements PoolableProvider {

    public HikariDataSource obtainDataSource(@NonNull UniversalCredential credential, int maxConnections) {
        final HikariConfig configuration = getHikariConfiguration(credential, maxConnections);
        configuration.setJdbcUrl("jdbc:h2:mem:");
        return new HikariDataSource(configuration);
    }

    @Override
    public HikariConfig getHikariConfiguration(@NonNull UniversalCredential credential, int maxConnections) {
        return new HikariConfig();
    }
}
