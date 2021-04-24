/*
 * Copyright (c) 2021 yking-projects
 */

package dev.king.universal.shared.functional;

import java.util.function.Function;

/**
 * Is trying block functional interface
 *
 * @param <Type>   input generic param, has used to ResultSet
 * @param <Result> out generic param, has used to returns an object type
 */
public interface SafetyFunction<Type, Result> extends Function<Type, Result> {

    @Override
    default Result apply(Type type) {
        try {
            return applyThrowing(type);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    Result applyThrowing(Type type) throws Exception;
}