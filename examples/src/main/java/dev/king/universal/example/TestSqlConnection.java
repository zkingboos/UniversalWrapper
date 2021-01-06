package dev.king.universal.example;

import dev.king.universal.example.util.UniversalMethod;
import dev.king.universal.shared.api.JdbcProvider;
import dev.king.universal.wrapper.sql.SqlProvider;

import java.io.File;

public final class TestSqlConnection {

    public static void main(String[] args) {
        final JdbcProvider provider = SqlProvider.from(
          new File("D:/test.db")
        ).preOpen(); //you don't need exactly call this method, only for demonstration

        UniversalMethod.dispatchProvider(provider);
    }
}
