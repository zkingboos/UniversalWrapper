package dev.king.universal.extension;

import dev.king.universal.shared.SQLUtil;
import lombok.Data;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
        this.registry = new LinkedHashMap<>(getRecursively());
    }

    public SQLReader(@NonNull String root) {
        this(root, DEFAULT_EXTENSION);
    }

    private Map<String, String> getRecursively() {
        try {
            final File resource = SQLUtil.getAbsoluteFile();
            final Map<String, String> sqlMap = new LinkedHashMap<>();

            final JarFile jarFile = new JarFile(resource);
            final Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                final JarEntry jarEntry = entries.nextElement();
                final String name = jarEntry.getName();

                if (!name.startsWith(root) || !name.endsWith(extension)) continue;
                try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
                    final String readAllContent = SQLUtil.readAllContent(inputStream);
                    sqlMap.put(name, readAllContent);
                }
            }
            return sqlMap;
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public String getSQL(@NonNull String path) throws SQLReaderNotFoundException {
        path = String.format("%s/%s%s", root, path.trim().replace(".", "/"), extension);
        final String content = registry.get(path);
        if (content == null) {
            throw new SQLReaderNotFoundException("SQLReader couldn't read content sql from '%s'.", path);
        }
        return content;
    }
}
