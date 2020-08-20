package dev.king.universal.test.sql;

import dev.king.universal.UniversalWrapper;
import dev.king.universal.api.JdbcProvider;
import dev.king.universal.test.common.UniversalMethod;

import java.io.File;

public final class TestSqlConnection {

    public static void main(String[] args) {
        final UniversalWrapper wrapper = UniversalWrapper.getInstance();
        final JdbcProvider provider = wrapper.newSqlProvider(
          new File("C:/Users/fcthe/Desktop/teste.db")
        );

        UniversalMethod.dispatchProvider(provider);
    }
}
