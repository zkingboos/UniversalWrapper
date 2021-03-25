package dev.king;

import dev.king.universal.extension.SQLReaderExtension;
import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.wrapper.mysql.MySQLProvider;
import lombok.extern.java.Log;

@Log
public class ProviderExtensionInstall {

    public static void main(String[] args) {
        final DefaultSQLSupport sqlSupport = MySQLProvider
          .from("localhost:3306", "universalwrapper", "root", "test", 2)
          .install(new SQLReaderExtension());

        if(!sqlSupport.openConnection()) {
            log.severe("Cannot make database connection");
            return;
        }

        sqlSupport.update("king.create");
    }
}
