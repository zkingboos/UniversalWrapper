/*
 * Copyright (c) 2021 yking-projects
 */

package dev.king.universal.shared.functional;

import java.util.function.BiConsumer;

public interface SafetyBiConsumer<T, R> extends BiConsumer<T, R> {

    @Override
    default void accept(T t, R r) {
        try {
            acceptThrowing(t, r);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    void acceptThrowing(T t, R r) throws Exception;
}
