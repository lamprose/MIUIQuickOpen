package io.github.lamprose.quick.xposed

import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import io.github.lamprose.quick.xposed.XposedUtils.Companion.APP_NAME
import io.github.lamprose.quick.xposed.XposedUtils.Companion.SYSTEM_UI_PACKAGE_NAME

class HookEntry : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != lpparam.processName)
            return
        EzXHelperInit.setLogTag("MIUIQuickOpen")
        when (lpparam.packageName) {
            APP_NAME -> {
                XposedHelpers.findAndHookMethod("$APP_NAME.SettingsFragment",
                    lpparam.classLoader, "isModuleActivated", object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam) {
                            param.result = true
                        }
                    })
            }
            SYSTEM_UI_PACKAGE_NAME -> {
                EzXHelperInit.initHandleLoadPackage(lpparam)
                MainHook.hookQuickOpen()
            }
        }
    }
}