package com.github.lamprose.quick

import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage


class MainHook : IXposedHookLoadPackage {
    companion object {
        const val HANDLE_LOAD_PACKAGE_NAME = "com.android.systemui"
        const val HOOK_CLASSNAME_PREFIX = "com.android.keyguard.fod.item"
        val HOOK_CLASSNAME_LIST = arrayOf(
            "WechatPayItem",
            "WechatScanItem",
            "XiaoaiItem",
            "AlipayScanItem",
            "AlipayPayItem"
        )
        val pref by lazy {
            val pref = XSharedPreferences(BuildConfig.APPLICATION_ID, "data")
            return@lazy if (pref.file.canRead()) pref else null
        }

    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (lpparam?.packageName != HANDLE_LOAD_PACKAGE_NAME) {
            return
        }
        hookQuickOpen(lpparam);
    }

    private fun hookQuickOpen(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            for (item in HOOK_CLASSNAME_LIST) {
                val hookClass =
                    XposedHelpers.findClassIfExists(
                        "$HOOK_CLASSNAME_PREFIX.$item",
                        lpparam.classLoader
                    ) ?: continue
                XposedHelpers.findAndHookMethod(
                    hookClass,
                    "getIntent",
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam?) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            val target: String = pref!!.getString(item, null)!!
                            when {
                                target.indexOf('/') != -1 -> {
                                    val split = target.split('/')
                                    intent.component = ComponentName(
                                        split[0],
                                        if (split[1].startsWith('.')) split[0] + split[1] else split[1]
                                    )
                                }
                                else -> {
                                    XposedBridge.log("[MIUIQuickOpen] error:$item's parameter is not valid")
                                    return
                                }
                            }
                            param?.result = intent
                        }
                    })
            }
        } catch (e: Exception) {
            XposedBridge.log("[MIUIQuickOpen] Hook Error:" + e.message)
        }
    }
}