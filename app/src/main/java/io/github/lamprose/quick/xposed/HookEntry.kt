package io.github.lamprose.quick.xposed

import android.os.Build
import io.github.lamprose.quick.xposed.XposedUtils.Companion.APP_NAME
import io.github.lamprose.quick.xposed.XposedUtils.Companion.SYSTEM_UI_PACKAGE_NAME
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.util.*

class HookEntry : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == APP_NAME) {
            XposedHelpers.findAndHookMethod("$APP_NAME.SettingsFragment",
                lpparam.classLoader, "isModuleActivated", object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        param.result = true
                        XposedBridge.log("[MIUIOPEN] 已激活")
                    }
                })
        } else if (lpparam.packageName == SYSTEM_UI_PACKAGE_NAME) {
            MainHook.hookQuickOpen(
                lpparam
            )
        }
    }
}