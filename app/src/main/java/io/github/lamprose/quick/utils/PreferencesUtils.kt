package io.github.lamprose.quick.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor


object PreferencesUtils {

    fun putString(
        context: Context,
        spName: String?,
        key: String?,
        value: String?
    ): Boolean {
        val settings: SharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.putString(key, value)
        return editor.commit()
    }

    fun remove(context: Context, spName: String?, key: String?): Boolean {
        val settings: SharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.remove(key)
        return editor.commit()
    }

    fun getString(context: Context, spName: String?, key: String?): String? {
        return getString(context, spName, key, null)
    }

    fun getString(
        context: Context,
        spName: String?,
        key: String?,
        defaultValue: String?
    ): String? {
        val settings: SharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        return settings.getString(key, defaultValue)
    }

    fun putInt(
        context: Context,
        spName: String?,
        key: String?,
        value: Int
    ): Boolean {
        val settings: SharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.putInt(key, value)
        return editor.commit()
    }

    fun getInt(context: Context, spName: String?, key: String?): Int {
        return getInt(context, spName, key, -1)
    }

    fun getInt(
        context: Context,
        spName: String?,
        key: String?,
        defaultValue: Int
    ): Int {
        val settings: SharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        return settings.getInt(key, defaultValue)
    }

    fun putLong(
        context: Context,
        spName: String?,
        key: String?,
        value: Long
    ): Boolean {
        val settings: SharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.putLong(key, value)
        return editor.commit()
    }

    fun getLong(context: Context, spName: String?, key: String?): Long {
        return getLong(context, spName, key, -1)
    }

    fun getLong(
        context: Context,
        spName: String?,
        key: String?,
        defaultValue: Long
    ): Long {
        val settings: SharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        return settings.getLong(key, defaultValue)
    }

    fun putFloat(
        context: Context,
        spName: String?,
        key: String?,
        value: Float
    ): Boolean {
        val settings: SharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.putFloat(key, value)
        return editor.commit()
    }

    fun getEditor(context: Context, spName: String?): Editor {
        val settings: SharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        return settings.edit()
    }

    fun getFloat(context: Context, spName: String?, key: String?): Float {
        return getFloat(context, spName, key, -1f)
    }

    fun getFloat(
        context: Context,
        spName: String?,
        key: String?,
        defaultValue: Float
    ): Float {
        val settings: SharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        return settings.getFloat(key, defaultValue)
    }

    fun putBoolean(
        context: Context,
        spName: String?,
        key: String?,
        value: Boolean
    ): Boolean {
        val settings: SharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.putBoolean(key, value)
        return editor.commit()
    }

    fun getBoolean(context: Context, spName: String?, key: String?): Boolean {
        return getBoolean(context, spName, key, false)
    }

    fun getBoolean(
        context: Context,
        spName: String?,
        key: String?,
        defaultValue: Boolean
    ): Boolean {
        val settings: SharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        return settings.getBoolean(key, defaultValue)
    }
}