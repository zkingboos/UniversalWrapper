package dev.king.universal.test.common;

import dev.king.universal.UniversalWrapper;
import dev.king.universal.api.JdbcProvider;

import java.util.List;
import java.util.stream.Collectors;

public final class UniversalMethod {

    public static void dispatchProvider(JdbcProvider provider, UniversalWrapper wrapper) {
        if (!provider.openConnection()) {
            System.out.println("No database connection has been established");
            return;
        }

        /*
         * The method update is used to send query that you don't need returns
         */
        provider.update("create table if not exists king(name varchar(1))");
        provider.update("insert into king (name) values (?)", "y");

        /*
         * In the bellow example, we used an simply query to select one user from table
         * The first field is the query, the second field is the lambda set, on third field
         * Is the query objects
         */
        final TestPojo pojo = provider.query(
                "select * from king where name = ?",
                set -> new TestPojo(set.getString("name")),
                "y"
        ).orElse(new TestPojo("invalid user"));

        /*
         * Get a map of objects
         */
        List<TestPojo> pooSet = provider.map(
                "select * from king",
                set -> new TestPojo(set.getString("name"))
        ).get().collect(Collectors.toList());

        System.out.println("Single object: " + pojo);
        System.out.println("Object collection: " + pooSet);

        /**
         * If you're using bukkit, you need to close
         * the service instance using: UniversalWrapper#closeService() at onDisable method.
         */
        //wrapper.closeService();
    }
}
