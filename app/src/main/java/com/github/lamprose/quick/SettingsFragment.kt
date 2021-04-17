package com.github.lamprose.quick

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_WORLD_READABLE
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.preference.*

class SettingFragment() : BasePreferenceFragment(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    var pref: SharedPreferences? = null;


    @SuppressLint("WorldReadableFiles")
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //加载xml文件
        setPreferencesFromResource(R.xml.setting_preference, rootKey)
        preferenceManager.sharedPreferencesName = getString(R.string.pref_name)
        preferenceManager.sharedPreferencesMode = MODE_WORLD_READABLE
        pref = try {
            activity?.getSharedPreferences(
                getString(R.string.pref_name),
                MODE_WORLD_READABLE
            )
        } catch (e: SecurityException) {
            null
        }
        pref?.all?.forEach { (key, value) ->
            findPreference<EditTextPreference>(key)?.apply {
                summary = value as CharSequence?
                text = value.toString()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        findPreference<EditTextPreference>(key!!)?.apply {
            summary = sharedPreferences!!.getString(key, null)
        }
    }
}