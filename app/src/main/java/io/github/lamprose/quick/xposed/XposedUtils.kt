package io.github.lamprose.quick.xposed

import io.github.lamprose.quick.BuildConfig

class XposedUtils {
    companion object {
        const val APP_NAME = BuildConfig.APPLICATION_ID
        const val SYSTEM_UI_PACKAGE_NAME = "com.android.systemui"
    }
}