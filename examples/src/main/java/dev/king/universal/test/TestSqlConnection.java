package dev.king.universal.test;

import dev.king.universal.shared.api.JdbcProvider;
import dev.king.universal.test.util.UniversalMethod;
import dev.king.universal.wrapper.sql.SqlProvider;

import java.io.File;

public final class TestSqlConnection {

    public static void main(String[] args) {
        final JdbcProvider provider = SqlProvider.from(
          new File("C:/Users/fcthe/Desktop/teste.db")
        ).preOpen(); //you don't need exactly call this method, only for demonstration

        UniversalMethod.dispatchProvider(provider);
    }
}
