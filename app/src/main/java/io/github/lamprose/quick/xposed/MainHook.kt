package io.github.lamprose.quick.xposed

import android.content.ComponentName
import android.content.Intent
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage
import io.github.lamprose.quick.BuildConfig


class MainHook {
    companion object {
        private const val HOOK_CLASSNAME_PREFIX = "com.android.keyguard.fod.item"
        private val HOOK_CLASSNAME_LIST = arrayOf(
            "WechatPayItem",
            "WechatScanItem",
            "XiaoaiItem",
            "AlipayScanItem",
            "AlipayPayItem"
        )
        private val pref by lazy {
            val pref = XSharedPreferences(BuildConfig.APPLICATION_ID, "data")
            return@lazy if (pref.file.canRead()) pref else null
        }

        fun hookQuickOpen(lpparam: XC_LoadPackage.LoadPackageParam) {
            try {
                for (item in HOOK_CLASSNAME_LIST) {
                    val target: String = pref!!.getString(item, null) ?: continue
                    val hookClass =
                        XposedHelpers.findClassIfExists(
                            "$HOOK_CLASSNAME_PREFIX.$item",
                            lpparam.classLoader
                        ) ?: continue
                    if ("XiaoaiItem" == item) {
                        XposedHelpers.findAndHookMethod(
                            hookClass,
                            "startActionByService",
                            object : XC_MethodHook() {
                                override fun beforeHookedMethod(param: MethodHookParam?) {
                                    param?.result = false
                                }
                            })
                    }
                    XposedHelpers.findAndHookMethod(
                        hookClass,
                        "getIntent",
                        object : XC_MethodHook() {
                            override fun beforeHookedMethod(param: MethodHookParam?) {
                                val intent = Intent(Intent.ACTION_VIEW)
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
}