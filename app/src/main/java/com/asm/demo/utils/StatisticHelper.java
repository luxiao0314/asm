package com.asm.demo.utils;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import androidx.annotation.Keep;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.asm.demo.utils.View_ExitKt.getElementContent;
import static com.asm.demo.utils.View_ExitKt.getViewId;

/**
 * 方法耗时统计+埋点统计
 */
public class StatisticHelper {

    /*************************  埋点 *************************/

    @Keep
    public static void viewOnClick(View view) {
        Log.d("StatisticHelper", "自动埋点 --> ViewId:" + getViewId(view) + " ViewText:" + getElementContent(view));
    }

    @Keep
    public static void viewOnPageSelected(int position) {
        Log.d("StatisticHelper", "自动埋点 --> OnPageSelected:" + position);
    }

    @Keep
    public static void testAnnotation(Object object, int code, String message) {
        Log.d("StatisticHelper", "自动埋点:注解 --> " + message + ":" + code + ":" + object.getClass().getSimpleName());
    }

    /*************************  方法耗时 *************************/

    private static final Map<String, Long> times = new LinkedHashMap<>();

    @Keep
    public static void enter(Object thisObj, String className, String methodName, String argsType, String returnType, Object... args) {
        times.put(className + methodName, SystemClock.elapsedRealtime());
    }

    @Keep
    public static void exit(Object thisObj, String className, String methodName, String argsType, String returnType, Object... args) {

        Long time = times.get(className + methodName);

        long duc = SystemClock.elapsedRealtime() - time;

        String value = (thisObj == null ? className : className + "@" + Integer.toHexString(System.identityHashCode(thisObj))) + "." + methodName + "():[" + duc + "ms]";

        if (duc >= 1000) {
            Log.e("StatisticHelper", value);
        } else if (duc >= 600) {
            Log.w("StatisticHelper", value);
        } else if (duc >= 300) {
            Log.d("StatisticHelper", value);
        } else if (duc >= 50) {
            Log.i("StatisticHelper", value);
        }
    }
}
