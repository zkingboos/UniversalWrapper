package dev.king.jdbc.sql;

import dev.king.jdbc.JdbcProvider;
import dev.king.jdbc.KFunction;
import dev.king.jdbc.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.sqlite.JDBC;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The provider for sqlite for jdbc
 * @author zkingboos_
 */
@RequiredArgsConstructor
public class SqlProvider implements JdbcProvider {

    private final File output;

    private Connection con;

    /**
     * Close the single connection
     */
    @Override
    public void closeConnection() {
        if(!hasConnection()) return;
        close(con);
    }

    /**
     * Verify if the connection is valid
     * @return returns if connection an valid
     */
    @SneakyThrows
    private boolean hasConnection(){
        return con != null && !con.isClosed();
    }

    /**
     * Opens the single connection
     * @return returns if connection is opened
     */
    @Override
    public boolean openConnection() {
        try {
            if(hasConnection()) return true;
            if(!output.exists()) return false;
            DriverManager.registerDriver(new JDBC());
            con = DriverManager.getConnection("jdbc:sqlite:"+output);
            return !con.isClosed();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Uses just in select query
     * @param query the query of mysql
     * @param consumer if has a valid entry, function will be called and returns a result
     * @param objects the objects that will be putted in the prepared statment
     * @param <K> the generic type, used to return your prefer value
     * @return returns a optional value, applied in function parameter
     */
    @Override
    public <K> Optional<K> query(
            String query,
            KFunction<ResultSet, K> consumer,
            Object... objects
    ) {
        try {
            PreparedStatement ps = con.prepareStatement(query);
            Utilities.syncObjects(ps, objects);
            ResultSet set = ps.executeQuery();
            K result = set != null && set.next() ? consumer.apply(set) : null;
            close(ps, set);
            return Optional.ofNullable(result);
        }catch (Exception e){
            return Optional.empty();
        }
    }

    /**
     * Uses just in create, delete, insert and update querys
     * @param query the query of mysql
     * @param objects the objects that will be putted in the prepared statment
     * @return returns a int response, if do appear -1, signify that have an error
     */
    @Override
    public int update(
            String query,
            Object... objects
    ) {
        try {
            PreparedStatement ps = con.prepareStatement(query);
            Utilities.syncObjects(ps, objects);

            int result = ps.executeUpdate();
            close(ps);

            return result;
        }catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Uses just in select query
     * @param query the query of mysql
     * @param function if has a valid entry, function will be called and returns a result
     * @param objects the objects that will be putted in the prepared statment
     * @param <K> the generic type, used to return your prefer value
     * @return returns a optional value, applied in function parameter
     */
    @Override
    public <K> Optional<Stream<K>> map(
            String query,
            KFunction<ResultSet, K> function,
            Object... objects
    ) {
        try {
            PreparedStatement ps = con.prepareStatement(query);
            Utilities.syncObjects(ps, objects);
            ResultSet set = ps.executeQuery();

            List<K> paramResult = new ArrayList<>();
            while (set.next()) { paramResult.add(function.apply(set)); }

            close(set, ps);
            return Optional.ofNullable(paramResult.stream());
        }catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Close the all AutoCloseable instances
     * @param closeables the all closeable connections
     */
    @SneakyThrows
    public void close(AutoCloseable... closeables) {
        for(AutoCloseable close : closeables) { close.close(); }
    }
}
