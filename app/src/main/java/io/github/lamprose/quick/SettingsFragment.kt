package io.github.lamprose.quick

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.preference.EditTextPreference
import androidx.preference.SwitchPreference

@Suppress("DEPRECATION")
class SettingsFragment(private val handler: Handler) : BasePreferenceFragment() {
    private var pref: SharedPreferences? = null
    private var editArray: Array<String>? = null

    private fun isModuleActivated(): Boolean {
        return false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        editArray = arrayOf(
            requireActivity().getString(R.string.wechat_pay_key),
            requireActivity().getString(R.string.wechat_scan_key),
            requireActivity().getString(R.string.xiaoai_key),
            requireActivity().getString(R.string.alipay_scan_key),
            requireActivity().getString(R.string.alipay_pay_key),
            requireActivity().getString(R.string.add_event_key),
            requireActivity().getString(R.string.add_note_key),
            requireActivity().getString(R.string.dialer_key),
            requireActivity().getString(R.string.search_key),
            requireActivity().getString(R.string.qr_code_key)
        )
    }


    @SuppressLint("WorldReadableFiles")
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //加载xml文件
        setPreferencesFromResource(R.xml.setting_preference, rootKey)
        preferenceManager.sharedPreferencesName = getString(R.string.pref_name)
        preferenceManager.sharedPreferencesMode = Context.MODE_WORLD_READABLE
        pref = try {
            activity?.getSharedPreferences(
                getString(R.string.pref_name),
                Context.MODE_WORLD_READABLE
            )
        } catch (e: SecurityException) {
            preferenceManager.preferenceScreen.isEnabled = false
            return
        }
        initData()
    }

    private fun initData() {
        editArray!!.forEach { key ->
            findPreference<EditTextPreference>(key)?.apply {
                pref!!.getString(key, null).let {
                    summary = it
                    text = it
                }
                setOnPreferenceChangeListener { _, newValue ->
                    summary = newValue as CharSequence?
                    true
                }
            }
        }
        findPreference<SwitchPreference>(requireActivity().getString(R.string.hide_app_icon_key))?.apply {
            isChecked =
                pref!!.getBoolean(requireActivity().getString(R.string.hide_app_icon_key), false)
            setOnPreferenceChangeListener { _, newValue ->
                handler.sendEmptyMessageDelayed(if (newValue as Boolean) 0 else 1, 1000)
                true
            }
        }
        findPreference<SwitchPreference>(requireActivity().getString(R.string.app_status_key))?.apply {
            isChecked = isModuleActivated()
            setOnPreferenceClickListener {
                true
            }
        }
    }
}