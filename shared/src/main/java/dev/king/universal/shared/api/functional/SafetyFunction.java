/*
 * Copyright (c) 2021 yking-projects
 */

package dev.king.universal.shared.api.functional;

import java.util.function.Function;

/**
 * Is trying block functional interface
 *
 * @param <T> input generic param, has used to ResultSet
 * @param <R> out generic param, has used to returns an object type
 */
public interface SafetyFunction<T, R> extends Function<T, R> {

    @Override
    default R apply(T t) {
        try {
            return applyThrowning(t);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    R applyThrowning(T t) throws Exception;
}