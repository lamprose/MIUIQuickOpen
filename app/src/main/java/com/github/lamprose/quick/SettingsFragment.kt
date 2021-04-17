package com.github.lamprose.quick

import android.annotation.SuppressLint
import android.content.Context.MODE_WORLD_READABLE
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.preference.EditTextPreference
import androidx.preference.SwitchPreference

class SettingFragment(private val handler: Handler) : BasePreferenceFragment() {
    var pref: SharedPreferences? = null;
    val editArray = arrayOf(
        "WechatPayItem",
        "WechatScanItem",
        "XiaoaiItem",
        "AlipayScanItem",
        "AlipayPayItem"
    )


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
        initData()
    }

    private fun initData() {
        editArray.forEach {
            findPreference<EditTextPreference>(it)?.apply {
                summary = pref!!.getString(it, null)
                text = pref!!.getString(it, null)
                setOnPreferenceChangeListener { preference, newValue ->
                    summary = newValue as CharSequence?
                    true
                }
            }
        }
        findPreference<SwitchPreference>("hideAppIcon")?.apply {
            isChecked = pref!!.getBoolean("hideAppIcon", false)
            setOnPreferenceChangeListener { preference, newValue ->
                handler.sendEmptyMessageDelayed(if (newValue as Boolean) 0 else 1, 1000)
                true
            }
        }
    }
}