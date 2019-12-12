package me.ImSpooks.iwbtgengine.helpers;

import org.tinylog.Logger;

/**
 * Created by Nick on 27 sep. 2019.
 * Copyright © ImSpooks
 */
public class JavaHelpers {

    /**
     * Sleeps the current thread
     *
     * @param millis milliseconds to sleep
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Logger.warn("Something went wrong while trying to sleep thread \"{}\"", Thread.currentThread().getName());
        }
    }
    /**
     * @see JavaHelpers#sleep(long)
     */
    public static void sleep(int millis) {
        JavaHelpers.sleep((long) millis);
    }
}
