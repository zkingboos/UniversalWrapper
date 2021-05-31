import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.wrapper.mysql.MySQLProvider;
import dev.king.universal.wrapper.mysql.implementation.connection.H2PoolableConnection;
import dev.king.universal.wrapper.mysql.implementation.credential.MysqlCredential;
import lombok.extern.java.Log;
import org.junit.After;
import org.junit.jupiter.api.*;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log
public class TestInstallSQLReaderExtension {

    private static DefaultSQLSupport defaultSQLSupport;

    @Test
    @BeforeAll
    static void init() {
        defaultSQLSupport = new MySQLProvider(MysqlCredential.EMPTY, new H2PoolableConnection(), 4);
    }

    @Test
    @BeforeEach
    public void testIfHasConnectionAndCreateTableParasite() {
        assertTrue(defaultSQLSupport.hasConnection());
        assertEquals(0, defaultSQLSupport.update("create table if not exists teste(king varchar(255))"));
    }

    @Test
    @After
    public void test() {
        Assertions.assertEquals(1, defaultSQLSupport.update("insert into teste (king) values (?)", "teste"));
    }

    @Test
    @After
    @Disabled
    public void testInsertHalfMillion() {
        final List<String> peopleList = new LinkedList<>();
        for (int i = 0; i < 500000; i++) {
            peopleList.add(UUID.randomUUID().toString());
        }

        final int[] result = defaultSQLSupport.batch(
          "insert into teste (king) values (?)",
          (entity, batchQuery) -> batchQuery.compute(entity)
          , peopleList
        );

        Assertions.assertEquals(result.length, peopleList.size());
    }
}
