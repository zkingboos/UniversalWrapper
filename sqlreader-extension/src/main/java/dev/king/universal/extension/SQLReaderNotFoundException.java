package dev.king.universal.extension;

import lombok.NonNull;

public class SQLReaderNotFoundException extends RuntimeException {

    public SQLReaderNotFoundException(@NonNull String message, Object... objects) {
        super(String.format(message, objects));
    }
}
