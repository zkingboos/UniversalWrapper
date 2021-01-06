/*
 * Copyright (c) 2021 yking-projects
 */

package dev.king.universal.shared.api.functional;

import java.util.function.BiConsumer;

public interface SafetyBiConsumer<T, R> extends BiConsumer<T, R> {

    @Override
    default void accept(T t, R r) {
        try {
            acceptThrowning(t, r);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    void acceptThrowning(T t, R r) throws Exception;
}
