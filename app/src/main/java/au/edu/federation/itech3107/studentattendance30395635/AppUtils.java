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
     * 生成UUID
     */
    public static String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取当前周数
     */
    public static int getCurrentWeek(Context context) {
        int week = 1;

        //获取开始时间
        String beginMillis = Preferences.getString(context.getString(
                R.string.app_preference_start_week_begin_millis), "");

        //获取当前时间
        long currentMillis = Calendar.getInstance().getTimeInMillis();

        //存在开始时间
        if (!TextUtils.isEmpty(beginMillis)) {
            long intBeginMillis = Long.valueOf(beginMillis);

            //获取到的配置是时间大于当前时间 重置为第一周
            if (intBeginMillis > currentMillis) {
                PreferencesCurrentWeek(context, 1);

            } else {
                //计算出开始时间到现在时间的周数
                int weekGap = TimeUtils.getWeekGap(intBeginMillis, currentMillis);

                week += weekGap;
            }

        } else {
            //不存在开始时间 初始化为第一周
            PreferencesCurrentWeek(context, 1);
        }

        return week;
    }

    /**
     * 更改当前周
     */
    public static void PreferencesCurrentWeek(Context context, int currentWeekCount) {
        //得到一个当前周 周一的日期
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
     * 根据邮箱获取gravator头像
     */
    public static String getGravatar(String email) {
        String emailMd5 = AppUtils.md5Hex(email);        //设置图片大小32px
        String avatar = "http://www.gravatar.com/avatar/" + emailMd5 + "?s=128";
        return avatar;
    }

    /**
     * 更新widget组件
     */
    public static void updateWidget(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.mnnyang.action.UPDATE_WIDGET");
        intent.setComponent(new ComponentName("com.mnnyang.gzuclassschedule", "com.mnnyang.gzuclassschedule.widget.MyWidget"));
        context.sendBroadcast(intent);
    }

    public static int UPDATE_WIDGET_JOB_ID = 1;

    /**
     * 取消widget更新任务
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
     * 粗略判断邮箱
     */
    public static boolean isEmail(String content) {
        String pattern = "[a-zA-Z0-9._]+@[a-zA-Z0-9.]+\\.[a-zA-Z0-9.]+";

        return Pattern.matches(pattern, content);
    }
}
