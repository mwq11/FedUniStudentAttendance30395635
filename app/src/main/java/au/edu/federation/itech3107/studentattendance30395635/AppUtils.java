package au.edu.federation.itech3107.studentattendance30395635;

import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

import au.edu.federation.itech3107.studentattendance30395635.util.Preferences;
import au.edu.federation.itech3107.studentattendance30395635.util.TimeUtils;

/**
 *
 */
public class AppUtils {

    /**
     * Generate UUID
     */
    public static String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Gets the current number of weeks
     */
    public static int getCurrentWeek(Context context) {
        int week = 1;

        //Get start time
        String beginMillis = Preferences.getString(context.getString(
                R.string.app_preference_start_week_begin_millis), "");

        //Get current time
        long currentMillis = Calendar.getInstance().getTimeInMillis();

        //Existence start time
        if (!TextUtils.isEmpty(beginMillis)) {
            long intBeginMillis = Long.valueOf(beginMillis);

            //The obtained configuration is time greater than the
            // current time reset to the first week
            if (intBeginMillis > currentMillis) {
                PreferencesCurrentWeek(context, 1);

            } else {
                //Calculate the number of weeks between the start time and the present time
                int weekGap = TimeUtils.getWeekGap(intBeginMillis, currentMillis);

                week += weekGap;
            }

        } else {
            //There is no start time initialized to the first week
            PreferencesCurrentWeek(context, 1);
        }

        return week;
    }

    /**
     * Change current week
     */
    public static void PreferencesCurrentWeek(Context context, int currentWeekCount) {
        //Get a date for the Monday of the current week
        Calendar calendar = Calendar.getInstance();
        Date weekBegin = TimeUtils.getNowWeekBegin();
        calendar.setTime(weekBegin);

        if (currentWeekCount > 1) {
            calendar.add(Calendar.DATE, -7 * (currentWeekCount - 1));
        }


        Preferences.putString(context.getString(R.string.app_preference_start_week_begin_millis),
                calendar.getTimeInMillis() + "");
    }

    /**
     * Get the gravator avatar based on the mailbox
     */
    public static String getGravatar(String email) {
        String emailMd5 = AppUtils.md5Hex(email);
        String avatar = "http://www.gravatar.com/avatar/" + emailMd5 + "?s=128";
        return avatar;
    }

    /**
     * Update widget components
     */
    public static void updateWidget(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.mnnyang.action.UPDATE_WIDGET");
        intent.setComponent(new ComponentName("com.mnnyang.gzuclassschedule", "com.mnnyang.gzuclassschedule.widget.MyWidget"));
        context.sendBroadcast(intent);
    }

    public static int UPDATE_WIDGET_JOB_ID = 1;

    /**
     * Cancel the widget update task
     */
    public static void cancelUpdateWidgetService(Context context) {
        JobScheduler scheduler = (JobScheduler) context
                .getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(UPDATE_WIDGET_JOB_ID);
    }

    private static String hex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                    & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    private static String md5Hex(String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Rough judgment mailbox
     */
    public static boolean isEmail(String content) {
        String pattern = "[a-zA-Z0-9._]+@[a-zA-Z0-9.]+\\.[a-zA-Z0-9.]+";

        return Pattern.matches(pattern, content);
    }
}
