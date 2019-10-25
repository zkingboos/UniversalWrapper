package dev.king.jdbc.functional;

import java.util.function.Function;

public interface KFunction<T, R> extends Function<T, R> {

    @Override
    default R apply(T t) {
        try {
            return kApply(t);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    R kApply(T t) throws Exception;
}