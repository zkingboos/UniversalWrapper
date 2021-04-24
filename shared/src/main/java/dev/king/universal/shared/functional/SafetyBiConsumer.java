/*
 * Copyright (c) 2021 yking-projects
 */

package dev.king.universal.shared.functional;

import java.util.function.BiConsumer;

public interface SafetyBiConsumer<Type, Result> extends BiConsumer<Type, Result> {

    @Override
    default void accept(Type type, Result result) {
        try {
            acceptThrowing(type, result);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    void acceptThrowing(Type type, Result result) throws Exception;
}
