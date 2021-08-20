package com.asm.demo;

import android.os.SystemClock;
import android.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;

public class MethodHook {

    private static final Map<String, Long> times = new LinkedHashMap<>();

    public static void enter(Object thisObj, String className, String methodName, String argsType, String returnType, Object... args) {
        times.put(className + methodName, SystemClock.elapsedRealtime());
    }

    public static void exit(Object thisObj, String className, String methodName, String argsType, String returnType, Object... args) {

        Long time = times.get(className + methodName);

        long duc = SystemClock.elapsedRealtime() - time;

        String value = (thisObj == null ? className : className + "@" + Integer.toHexString(System.identityHashCode(thisObj))) + "." + methodName + "():[" + duc + "ms]";

        if (duc >= 1000) {
            Log.e("MethodHook", value);
        } else if (duc >= 600) {
            Log.w("MethodHook", value);
        } else if (duc >= 300) {
            Log.d("MethodHook", value);
        } else if (duc >= 50) {
            Log.i("MethodHook", value);
        }
    }

    public static void onClick(Object thisObj, String className, String methodName, String argsType, String returnType, Object... args) {

    }
}