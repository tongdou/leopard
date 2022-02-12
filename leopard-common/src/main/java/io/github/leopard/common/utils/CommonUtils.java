package io.github.leopard.common.utils;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:fuwei13@xdf.cn">pleuvoir</a>
 */
public class CommonUtils {


    public static void sleepSeconds(Integer seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException ignored) {
        }
    }

    public static void sleepMinutes(Integer minutes) {
        try {
            TimeUnit.MINUTES.sleep(minutes);
        } catch (InterruptedException ignored) {
        }
    }

    public static void sleepHours(Integer hours) {
        try {
            TimeUnit.HOURS.sleep(hours);
        } catch (InterruptedException ignored) {
        }
    }

    public static void join() {
        try {
            Thread.currentThread().join();
        } catch (InterruptedException ignored) {
        }
    }
}
