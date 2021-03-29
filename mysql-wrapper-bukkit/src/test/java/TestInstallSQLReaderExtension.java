import dev.king.universal.extension.SQLReaderExtension;
import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.wrapper.mysql.MySQLProvider;
import dev.king.universal.wrapper.mysql.credential.MysqlCredential;
import lombok.extern.java.Log;
import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        final MysqlCredential credential = new MysqlCredential("localhost:3306", "universalwrapper", "root", "test");
        defaultSQLSupport = new MySQLProvider(credential, new TestInMemoryPoolableConnection(), 2)
          .properties()
          .extensions()
          .install(SQLReaderExtension.from("sql"))
          .build();
    }

    @Test
    @BeforeEach
    public void testIfHasConnectionAndCreateTableParasite() {
        assertTrue(defaultSQLSupport.hasConnection());
        assertEquals(0, defaultSQLSupport.update("aleatory.create"));
    }

    @Test
    @After
    public void testInsertHalfMillion() {
        final List<String> peopleList = new LinkedList<>();
        for (int i = 0; i < 500000; i++) {
            peopleList.add(UUID.randomUUID().toString());
        }

        final int[] result = defaultSQLSupport.batch(
          "aleatory.insert",
          (entity, batchQuery) -> batchQuery.compute(entity)
          , peopleList
        );

        Assertions.assertEquals(result.length, peopleList.size());
    }
}
