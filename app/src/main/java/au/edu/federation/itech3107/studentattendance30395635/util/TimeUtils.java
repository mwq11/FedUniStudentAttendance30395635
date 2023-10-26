package au.edu.federation.itech3107.studentattendance30395635.util;

import java.util.Calendar;
import java.util.Date;


public class TimeUtils {
    /**
     *Returns the actual number of weeks from the first week of a week to the present
     *
     * @param weekBeginMillis
     * @param endMillis
     * @return
     */
    public static int getWeekGap(long weekBeginMillis, long endMillis) {
        return (int) (((endMillis - weekBeginMillis) / (1000 * 3600 * 24)) / 7);
    }

    /**
     * Get the date of Monday of this week
     *
     * @return
     */
    public static Date getNowWeekBegin() {
        return getThisWeekMonday(new Date());
    }

    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // Get the day of the week the current date is
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // Set the first day of a week. According to Chinese custom, the first day of a week is Monday
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // Get the day of the week the current date is
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // Subtract the difference between the day of the week and the first day of the week
        // from the current date according to the rules of the calendar
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    /**
     * Get current month
     * @return
     */
    public static int getNowMonth() {
        Calendar calendar = Calendar.getInstance();
        return 1 + calendar.get(Calendar.MONTH);
    }
}
