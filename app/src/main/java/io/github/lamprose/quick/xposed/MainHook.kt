package io.github.lamprose.quick.xposed

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.findMethodByCondition
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.invokeMethod
import com.github.kyuubiran.ezxhelper.utils.loadClass
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedBridge
import io.github.lamprose.quick.BuildConfig


class MainHook {
    companion object {
        private const val HOOK_CLASSNAME_PREFIX = "com.android.keyguard.fod.item"
        private const val HOOK_OPEN_VIEW = "com.android.keyguard.fod.MiuiGxzwQuickOpenView"
        private val HOOK_CLASSNAME_LIST = arrayOf(
            "WechatPayItem",
            "WechatScanItem",
            "XiaoaiItem",
            "AlipayScanItem",
            "AlipayPayItem",
            "AddEventItem",
            "AddNoteItem",
            "DialerItem",
            "QrCodeItem",
            "SearchItem"
        )

        private val pref by lazy {
            val pref = XSharedPreferences(BuildConfig.APPLICATION_ID, "data")
            return@lazy if (pref.file.canRead()) pref else {
                XposedBridge.log("pref is can't be read")
                null
            }
        }

        fun hookQuickOpen() {
            hookOpenView()
            hookQuickOpenItem()
        }

        private fun hookOpenView() {
            try {
                findMethodByCondition(HOOK_OPEN_VIEW) {
                    it.name == "handleQucikOpenItemTouchUp" &&
                            it.parameterTypes[0] == loadClass("$HOOK_CLASSNAME_PREFIX.IQuickOpenItem")
                }.also { hookMethod ->
                    XposedBridge.log("find hook method")
                    Log.d("find hook method")
                    hookMethod.hookBefore {
                        val intent: Intent = it.args?.get(0)?.invokeMethod("getIntent") as Intent
                        XposedBridge.log("intent ${intent.data}")
                        Log.d("intent ${intent.data}")
                        it.thisObject.invokeMethod(
                            "startActivitySafely",
                            arrayOf(intent),
                            arrayOf(Intent::class.java)
                        )
                        it.result = null
                    }
                }
            } catch (e: Exception) {
                Log.e(e, "hookOpenView")
                XposedBridge.log("[MIUIDock] hookOpenView Error:$e")
            }
        }

        private fun hookQuickOpenItem() {
            try {
                for (item in HOOK_CLASSNAME_LIST) {
                    val target: String = pref!!.getString(item, null) ?: continue
                    findMethodByCondition("$HOOK_CLASSNAME_PREFIX.$item") {
                        it.name == "getIntent" || it.name == "startActionByService"
                    }.also { hookMethod ->
                        if ("XiaoaiItem" == item && hookMethod.name == "startActionByService")
                            hookMethod.hookBefore {
                                it.result = false
                            }
                        else if (hookMethod.name == "getIntent")
                            hookMethod.hookBefore {
                                XposedBridge.log("$item is $target")
                                val intent = Intent(Intent.ACTION_VIEW)
                                when {
                                    target.indexOf("://") != -1 -> {
                                        intent.data = Uri.parse(target)
                                    }
                                    target.indexOf('/') != -1 -> {
                                        val split = target.split('/')
                                        intent.component = ComponentName(
                                            split[0],
                                            if (split[1].startsWith('.')) split[0] + split[1] else split[1]
                                        )
                                    }
                                    else -> {
                                        Log.d("error:$item's parameter is not valid")
                                        XposedBridge.log("[MIUIQuickOpen] error:$item's parameter is not valid")
                                        return@hookBefore
                                    }
                                }
                                it.result = intent
                            }
                    }
                }
            } catch (e: Exception) {
                Log.e(e, "hookQuickOpenItem")
                XposedBridge.log("[MIUIQuickOpen] hookQuickOpenItem Error:" + e.message)
            }
        }
    }

}