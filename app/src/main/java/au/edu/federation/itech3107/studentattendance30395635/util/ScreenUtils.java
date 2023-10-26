package au.edu.federation.itech3107.studentattendance30395635.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Size screen related tool class<br>
 */
public class ScreenUtils {

    public static Context context;

    public static void init(Context context) {
        ScreenUtils.context = context.getApplicationContext();
    }

    /**
     * Gets status bar height
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int temp = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * Hidden status bar
     * * <p> is to set the full screen, be sure to call before setContentView, otherwise an error </p>
     * * <p> This method Activity can inherit AppCompatActivity</p>
     * * <p> When starting the status bar will be displayed and then hidden, such as QQ welcome interface </p>
     * * < p > in the configuration file Activity add attribute android: theme = "@ android: style/theme. NoTitleBar. Fullscreen" < / p >
     * * <p> If the above configuration is added, the Activity cannot inherit AppCompatActivity and will report an error </p>.
     *
     * @param activity activity
     */
    public static void hideStatusBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * Transparent status bar setContentView is valid until called
     * >21
     * @param activity
     */
    public static void setSystemBarTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //If the layout is not topped causes a blank problem
        /* else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/
    }


    public static int getSHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay()
                .getMetrics(metrics);

        return metrics.heightPixels;

    }

    public static int getSWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay()
                .getMetrics(metrics);

        return metrics.widthPixels;

    }

    /**
     * From dp to px(pixels) depending on the phone's resolution
     */
    public static int dp2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * From px to dp depending on the phone's resolution
     */
    public static int px2dp(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * Convert the sp value to a px value, keeping the text size unchanged
     */
    public static int sp2px(float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}