package dev.king.universal.shared.extension;

import dev.king.universal.shared.DefaultSQLSupport;
import lombok.NonNull;

public interface BaseExtension extends DefaultSQLSupport {

    String getName();

    BaseExtension setDefaultSQLSupport(@NonNull DefaultSQLSupport defaultSQLSupport);

}
