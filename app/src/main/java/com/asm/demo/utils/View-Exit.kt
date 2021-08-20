package com.asm.demo.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SwitchCompat

/**
 * @Description
 * @Author lux
 * @Date 2021/8/20 2:53 PM
 * @Version
 */

/**
 * 获取 view 的 anroid:id 对应的字符串
 *
 * @param view View
 * @return String
 */
fun getViewId(view: View): String? {
    var idString: String? = null
    try {
        if (view.id != View.NO_ID) {
            idString = view.context.resources.getResourceEntryName(view.id)
        }
    } catch (e: Exception) {
        //ignore
    }
    return idString
}

/**
 * 获取 View 上显示的文本
 *
 * @param view View
 * @return String
 */
fun getElementContent(view: View?): String? {
    if (view == null) {
        return null
    }
    var viewText: CharSequence? = null
    when (view) {
        is CheckBox -> { // CheckBox
            viewText = view.text
        }
        is SwitchCompat -> {
            viewText = view.textOn
        }
        is RadioButton -> { // RadioButton
            viewText = view.text
        }
        is ToggleButton -> { // ToggleButton
            val isChecked = view.isChecked
            viewText = if (isChecked) {
                view.textOn
            } else {
                view.textOff
            }
        }
        is Button -> { // Button
            viewText = view.text
        }
        is CheckedTextView -> { // CheckedTextView
            viewText = view.text
        }
        is TextView -> { // TextView
            viewText = view.text
        }
        is SeekBar -> {
            viewText = view.progress.toString()
        }
        is RatingBar -> {
            viewText = view.rating.toString()
        }
    }
    return viewText?.toString()
}

/**
 * 获取 View 所属 Activity
 *
 * @param view View
 * @return Activity
 */
fun getActivityFromView(view: View?): Activity? {
    var activity: Activity? = null
    if (view == null) {
        return null
    }
    try {
        var context = view.context
        if (context != null) {
            if (context is Activity) {
                activity = context
            } else if (context is ContextWrapper) {
                while (context !is Activity && context is ContextWrapper) {
                    context = context.baseContext
                }
                if (context is Activity) {
                    activity = context
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return activity
}

/**
 * 获取 Android ID
 *
 * @param context Context
 * @return String
 */
@SuppressLint("HardwareIds")
fun getAndroidID(context: Context): String? {
    var androidID: String? = ""
    try {
        androidID = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return androidID
}