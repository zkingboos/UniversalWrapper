package dev.king.universal.shared.functional;

import java.util.function.Consumer;

public interface SafetyConsumer<Type> extends Consumer<Type> {

    @Override
    default void accept(Type type) {
        try {
            acceptThrowing(type);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    void acceptThrowing(Type type) throws Exception;
}
