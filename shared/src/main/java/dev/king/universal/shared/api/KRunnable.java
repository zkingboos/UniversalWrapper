/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.shared.api;

/**
 * Safety function interface
 */
public interface KRunnable extends Runnable {

    @Override
    default void run() {
        try {
            tryRun();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    void tryRun() throws Exception;
}