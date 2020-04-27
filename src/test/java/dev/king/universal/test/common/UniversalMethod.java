package dev.king.universal.test.common;

import dev.king.universal.api.JdbcProvider;

import java.util.List;
import java.util.stream.Collectors;

public class UniversalMethod {

    public static void dispatchProvider(JdbcProvider provider){
        if(!provider.openConnection()) {
            System.out.println("Nenhuma conexão com o banco de dados foi estabelecida");
            return;
        }

        /*
         * The method update is used to send querys that you dont need returns
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
        List<TestPojo> pojoSet = provider.map(
                "select * from king",
                set -> new TestPojo(set.getString("name"))
        ).get().collect(Collectors.toList());

        System.out.println("Single object: " + pojo);
        System.out.println("Object collection: " + pojoSet);
    }
}
