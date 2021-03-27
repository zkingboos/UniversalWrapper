package dev.king.universal.processor;

import lombok.NonNull;

public interface Repository {

    default void unique(@NonNull Object... objects) {

    }
}
