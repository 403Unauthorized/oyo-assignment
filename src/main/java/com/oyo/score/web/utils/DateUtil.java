package com.oyo.score.web.utils;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class DateUtil {

    // clock of 'Asia/Tokyo' time zone
    private static final Clock clock = Clock.system(ZoneId.ofOffset("GMT", ZoneOffset.of("+09:00")));

    /**
     * Get current millisecond instant of the clock
     * @return current millisecond
     */
    public static long currentTimeMillis() {
        return clock.millis();
    }
}
