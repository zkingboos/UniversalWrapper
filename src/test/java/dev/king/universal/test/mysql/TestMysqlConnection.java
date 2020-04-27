package dev.king.universal.test.mysql;

import dev.king.universal.UniversalWrapper;
import dev.king.universal.api.JdbcProvider;
import dev.king.universal.mysql.UniversalCredentials;
import dev.king.universal.test.common.TestPojo;
import dev.king.universal.test.common.UniversalMethod;

import java.util.List;
import java.util.stream.Collectors;

public final class TestMysqlConnection {

    public static void main(String[] args) {
        final UniversalWrapper wrapper = new UniversalWrapper();
        JdbcProvider provider = wrapper.newMysqlProvider(
                new UniversalCredentials(
                        "localhost:3306",
                        "test",
                        "root",
                        ""),
                2
        ).preOpen();

        UniversalMethod.dispatchProvider(provider);
    }
}
