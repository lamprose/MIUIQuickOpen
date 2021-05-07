package io.github.lamprose.quick

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.io.DataOutputStream
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    private var context: Context? = null
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        handler = Handler(mainLooper) {
            when (it.what) {
                0 -> {
                    packageManager.setComponentEnabledSetting(
                        ComponentName(this@MainActivity, context!!::class.java.name + "Alias"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                1 -> {
                    packageManager.setComponentEnabledSetting(
                        ComponentName(this@MainActivity, context!!::class.java.name + "Alias"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            false
        }
        addToolbar()
        //加载PrefFragment
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment,
                SettingsFragment(handler!!)
            )
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    private fun addToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_reboot_systemui -> {
                    val suProcess = Runtime.getRuntime().exec("su")
                    val os = DataOutputStream(suProcess.outputStream)
                    os.writeBytes("pkill -f com.android.systemui")
                    os.flush()
                    os.close()
                    val exitValue = suProcess.waitFor()
                    if (exitValue == 0) {
                        exitProcess(0)
                    } else {
                        throw Exception()
                    }
                }
                R.id.menu_about -> showAbout()
            }
            return@setOnMenuItemClickListener false
        }
    }

    private fun showAbout() {
        AlertDialog.Builder(this)
            .setTitle(R.string.menu_about_title)
            .setMessage(R.string.dialog_about_message)
            .setCancelable(true)
            .setPositiveButton(
                "CoolApk"
            ) { _, _ ->
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.coolapk.com/u/1033375")
                    )
                )
            }
            .setNegativeButton(
                "Github"
            ) { _, _ ->
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/lamprose/MIUIQuickOpen")
                    )
                )
            }
            .create().show()
    }

}
