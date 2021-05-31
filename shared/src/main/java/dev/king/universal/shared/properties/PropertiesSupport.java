package dev.king.universal.shared.properties;

import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.shared.extension.ExtensionSupport;
import lombok.Data;
import lombok.NonNull;

@Data
public class PropertiesSupport {

    private ExtensionSupport extensionSupport;
    private DefaultSQLSupport defaultSQLSupport;

    public PropertiesSupport(@NonNull DefaultSQLSupport defaultSQLSupport) {
        this.defaultSQLSupport = defaultSQLSupport;
    }

    public ExtensionSupport extensions() {
        if (extensionSupport != null) return extensionSupport;
        return (extensionSupport = new ExtensionSupport(defaultSQLSupport, this));
    }

    public DefaultSQLSupport get() {
        return defaultSQLSupport;
    }
}
