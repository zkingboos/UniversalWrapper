/*
 * Copyright (c) 2021 yking-projects
 */

package dev.king.universal.shared.api.functional;

/**
 * Safety function interface
 */
public interface SafetyRunnable extends Runnable {

    @Override
    default void run() {
        try {
            runThrowning();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    void runThrowning() throws Exception;
}
