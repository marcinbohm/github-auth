package pl.mb.githubauth.utils;

import java.time.Duration;

public class TimeoutUtil {

    public static long determineTimeoutInMs(long timeoutInMin) {
        return Duration.ofMinutes(timeoutInMin).toMillis();
    }
}
