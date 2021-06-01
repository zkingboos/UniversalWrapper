package dev.king.universal.example;

import dev.king.universal.extension.SQLReaderExtension;
import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.wrapper.mysql.MySQLProvider;
import dev.king.universal.wrapper.mysql.implementation.connection.H2PoolableConnection;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * In this class we use driver from {@link H2PoolableConnection}
 * Then all the data is stored in memory and right after the program exits it is discarded.
 */
public class TestMysqlBuilderH2Memory extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        final DefaultSQLSupport defaultSQLSupport = MySQLProvider
          .builder()
          .fromPlugin(this, "connection.h2")
          .driver(new H2PoolableConnection())
          .build()
          .properties()
          .extensions()
          .install(SQLReaderExtension.from("sql"))
          .get();

        defaultSQLSupport.update("aleatory.create");
        final int result = defaultSQLSupport.update("aleatory.insert", "king");
        System.out.println("result = " + result);
    }
}
