package dev.king.universal.shared.extension;

import dev.king.universal.shared.DefaultSQLSupport;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;

@Data
@Log(topic = "Extension Handler")
public abstract class ExtensionSupport {

    private final String name;

    public DefaultSQLSupport selfInstall(@NonNull DefaultSQLSupport support) {
        log.info(String.format("Attempting to install extension '%s'", name));
        return support;
    }
}
