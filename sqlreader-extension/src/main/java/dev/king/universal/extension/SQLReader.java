package dev.king.universal.extension;

import dev.king.universal.shared.SQLUtil;
import lombok.Data;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class SQLReader {

    public static final String DEFAULT_EXTENSION = ".sql";

    private final String root;
    private final String extension;
    private final Map<String, String> registry;

    /**
     * SQLReader constructor class
     *
     * @param root      where is root folder is located
     * @param extension file extension for {@link SQLReader#getSQL(String) format path}
     */
    public SQLReader(@NonNull String root, @NonNull String extension) {
        this.root = root;
        this.extension = extension;
        this.registry = new LinkedHashMap<>(getRecursively(root));
    }

    public SQLReader(@NonNull String root) {
        this(root, DEFAULT_EXTENSION);
    }

    private Map<String, String> getRecursively(String path) {
        final File resource = SQLUtil.getResource(path);
        assert resource != null;

        try {
            final Map<String, String> sqlMap = new LinkedHashMap<>();
            for (File file : Objects.requireNonNull(resource.listFiles())) {
                final String name = String.format("%s/%s", path, file.getName());
                if (file.isDirectory()) {
                    sqlMap.putAll(Objects.requireNonNull(getRecursively(name)));
                    continue;
                }
                sqlMap.put(name, Files.lines(file.toPath(), StandardCharsets.UTF_8).collect(Collectors.joining()));
            }
            return sqlMap;
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public String getSQL(@NonNull String path) throws SQLReaderNotFoundException {
        path = String.format("%s/%s", root, path.trim().replace(".", "/").concat(extension));
        final String content = registry.get(path);
        if (content == null) {
            throw new SQLReaderNotFoundException("SQLReader couldn't read content sql from '%s'.", path);
        }
        return content;
    }
}
