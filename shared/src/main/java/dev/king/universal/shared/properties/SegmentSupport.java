package dev.king.universal.shared.properties;

import dev.king.universal.shared.DefaultSQLSupport;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;

@Getter
public abstract class SegmentSupport<Segment> extends PropertiesSupport {

    @Delegate
    private final PropertiesSupport propertiesSupport;
    public SegmentSupport(@NonNull DefaultSQLSupport defaultSQLSupport, PropertiesSupport propertiesSupport) {
        super(defaultSQLSupport);
        this.propertiesSupport = propertiesSupport;
    }

    public abstract PropertiesSupport install(@NonNull Segment segment);
}
