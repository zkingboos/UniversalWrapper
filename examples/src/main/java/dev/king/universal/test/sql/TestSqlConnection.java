package test.sql;

import dev.king.universal.UniversalWrapper;
import dev.king.universal.api.JdbcProvider;
import test.common.UniversalMethod;

import java.io.File;

public final class TestSqlConnection {

    public static void main(String[] args) {
        final UniversalWrapper wrapper = new UniversalWrapper();
        final JdbcProvider provider = wrapper.newSqlProvider(
                new File("C:/Users/fcthe/Desktop/teste.db")
        ).preOpen();

        UniversalMethod.dispatchProvider(provider, wrapper);
    }
}
