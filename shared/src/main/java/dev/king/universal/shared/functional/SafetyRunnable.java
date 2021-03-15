/*
 * Copyright (c) 2021 yking-projects
 */

package dev.king.universal.shared.functional;

/**
 * Safety function interface
 */
public interface SafetyRunnable extends Runnable {

    @Override
    default void run() {
        try {
            runThrowing();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    void runThrowing() throws Exception;
}
