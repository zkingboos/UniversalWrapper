package dev.king.universal.example;

import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.wrapper.sql.SQLProvider;

import java.io.File;

public final class TestSqlConnection {

    /**
     * Initialize sql local connection, using UniversalWrapper project
     * Call {@link SQLProvider#from(File)} with already created and treated {@link File} instance (you need to create manually the file, if still doesn't exists)
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        final DefaultSQLSupport provider = SQLProvider.from(new File("C:/test.db"));
        DispatchMethod.dispatchProvider(provider);
    }
}
