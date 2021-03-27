package dev.king;

import dev.king.universal.extension.SQLReader;

public class SQLReaderTest {

    public static void main(String[] args) {
        final SQLReader sqlReader = new SQLReader("sql");
        final String recursively = sqlReader.getSQL("user.sla.fodase");
        System.out.println("recursively = " + recursively);
    }
}
