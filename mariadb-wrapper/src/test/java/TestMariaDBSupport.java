import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.wrapper.mariadb.MariaDBProvider;
import dev.king.universal.wrapper.mysql.implementation.credential.MysqlCredential;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMariaDBSupport {

    private static DefaultSQLSupport defaultSQLSupport;

    @Test
    @BeforeAll
    static void init() {
        final MysqlCredential credential = new MysqlCredential("localhost:3306", "universalwrapper", "root", "test");
        defaultSQLSupport = MariaDBProvider.from(credential, 2);
    }

    @Test
    @AfterAll
    static void dropTable() {
        assertEquals(0, defaultSQLSupport.update("drop table if exists test"));
    }

    @Test
    @BeforeEach
    public void testIfHasConnectionAndCreateTable() {
        assertTrue(defaultSQLSupport.hasConnection());
        assertEquals(0, defaultSQLSupport.update("create table if not exists test(name varchar(255))"));
    }

    @Test
    void testIfInsertionsWorksCorrectly() {
        assertEquals(1, defaultSQLSupport.update("insert into test(name) values (?)", "king"));
    }

    @Test
    void testIfSelectionsWorksCorrectly() {
        assertNotNull(defaultSQLSupport.query("select name from test", resultSet -> resultSet.getString("name")));
    }
}
