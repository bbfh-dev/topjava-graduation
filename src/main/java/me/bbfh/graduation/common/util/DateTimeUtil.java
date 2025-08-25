package me.bbfh.graduation.common.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalTime;

@UtilityClass
public class DateTimeUtil {

    public static final LocalTime VOTE_TIME_LIMIT = LocalTime.of(11, 0);

    private static LocalDate currentDate;
    private static LocalTime currentTime;

    public static LocalDate getCurrentDate() {
        return currentDate == null ? LocalDate.now() : currentDate;
    }

    public static LocalTime getCurrentTime() {
        return currentTime == null ? LocalTime.now() : currentTime;
    }

    public static void overrideCurrentDate(LocalDate currentDate) {
        DateTimeUtil.currentDate = currentDate;
    }

    public static void overrideCurrentTime(LocalTime currentTime) {
        DateTimeUtil.currentTime = currentTime;
    }

    public static boolean isVoteChangeAllowed() {
        return getCurrentTime().isBefore(VOTE_TIME_LIMIT);
    }
}
