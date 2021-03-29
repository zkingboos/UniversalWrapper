package dev.king.universal.shared.extension;

import dev.king.universal.shared.DefaultSQLSupport;
import dev.king.universal.shared.properties.PropertiesSupport;
import dev.king.universal.shared.properties.SegmentSupport;
import lombok.NonNull;
import lombok.extern.java.Log;

@Log(topic = "ExtensionSupport")
public final class ExtensionSupport extends SegmentSupport<BaseExtension> {

    public ExtensionSupport(@NonNull DefaultSQLSupport defaultSQLSupport, PropertiesSupport propertiesSupport) {
        super(defaultSQLSupport, propertiesSupport);
    }

    @Override
    public PropertiesSupport install(@NonNull BaseExtension baseExtension) {
        log.info(String.format("Attempting to install '%s' delegate extension.%n", baseExtension.getName()));
        setDefaultSQLSupport(baseExtension.setDefaultSQLSupport(getDefaultSQLSupport()));
        return getPropertiesSupport();
    }
}