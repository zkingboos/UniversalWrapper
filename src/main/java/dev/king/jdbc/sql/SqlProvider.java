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

@RequiredArgsConstructor
public class SqlProvider implements JdbcProvider {

    private final File output;

    private Connection con;

    @Override
    public void closeConnection() {
        if(!hasConnection()) return;
        close(con);
    }

    @SneakyThrows
    private boolean hasConnection(){
        return con != null && !con.isClosed();
    }

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

    @SneakyThrows
    public void close(AutoCloseable... closeables) {
        for(AutoCloseable close : closeables) { close.close(); }
    }
}
