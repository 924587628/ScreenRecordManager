package com.bzt.screenrecordmanager.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by sunxy on 2016/7/13.
 */

public class ScreenUtils {

    private static DisplayMetrics metrics = null;

    private static void initDisplay(Activity activity) {

        metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    }

    public static int getScreenWidth(Activity activity) {

        if (metrics == null)
            initDisplay(activity);

        return metrics.widthPixels;

    }

    public static int getScreenHeight(Activity activity) {

        if (metrics == null)
            initDisplay(activity);

        return metrics.heightPixels;

    }

    public static int getScreenDpi(Activity activity) {

        if (metrics == null)
            initDisplay(activity);

        return metrics.densityDpi;
    }

}
