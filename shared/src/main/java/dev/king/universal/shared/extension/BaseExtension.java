package dev.king.universal.shared.extension;

import dev.king.universal.shared.DefaultSQLSupport;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseExtension extends DefaultSQLSupport {

    private final String name;

    public abstract BaseExtension setDefaultSQLSupport(@NonNull DefaultSQLSupport defaultSQLSupport);

}
