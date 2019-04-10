package au.sjowl.lib.view.charts.telegram.other

import android.content.Context
import android.content.SharedPreferences

fun Context.getPrivateSharedPreferences(): SharedPreferences {
    return this.getSharedPreferences("$packageName:preference", Context.MODE_PRIVATE)
}

fun Context.setProperty(tag: String, isValue: Boolean) {
    try {
        val spe = getPrivateSharedPreferences().edit()
        spe.putBoolean(tag, isValue).apply()
    } catch (ex: Exception) {
    }
}

fun Context.setProperty(tag: String, value: Int) {
    try {
        val spe = getPrivateSharedPreferences().edit()
        spe.putInt(tag, value).apply()
    } catch (ex: Exception) {
    }
}

fun Context.setProperty(tag: String, value: Long) {
    try {
        val spe = getPrivateSharedPreferences().edit()
        spe.putLong(tag, value).apply()
    } catch (ex: Exception) {
    }
}

fun Context.setProperty(tag: String, value: Float) {
    try {
        val spe = getPrivateSharedPreferences().edit()
        spe.putFloat(tag, value).apply()
    } catch (ex: Exception) {
    }
}

fun Context.setProperty(tag: String, value: String) {
    try {
        val spe = getPrivateSharedPreferences().edit()
        spe.putString(tag, value).apply()
    } catch (ex: Exception) {
    }
}

fun Context.getProperty(tag: String, default_value: String): String {
    var result = default_value
    try {
        val sp = getPrivateSharedPreferences()
        result = sp.getString(tag, default_value)
    } catch (ex: Exception) {
    }
    return result
}

fun Context.getProperty(tag: String, default_value: Boolean): Boolean {
    var result = default_value
    try {
        val sp = getPrivateSharedPreferences()
        result = sp.getBoolean(tag, default_value)
    } catch (ex: Exception) {
    }
    return result
}

fun Context.getProperty(tag: String, default_value: Int): Int {
    var result = default_value
    try {
        val sp = getPrivateSharedPreferences()
        result = sp.getInt(tag, default_value)
    } catch (ex: Exception) {
    }
    return result
}

fun Context.getProperty(tag: String, default_value: Long): Long {
    var result = default_value
    try {
        val sp = getPrivateSharedPreferences()
        result = sp.getLong(tag, default_value)
    } catch (ex: Exception) {
    }
    return result
}

fun Context.getProperty(tag: String, default_value: Float): Float {
    var result = default_value
    try {
        val sp = getPrivateSharedPreferences()
        result = sp.getFloat(tag, default_value)
    } catch (ex: Exception) {
    }
    return result
}