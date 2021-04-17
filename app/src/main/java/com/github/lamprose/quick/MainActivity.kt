package com.github.lamprose.quick

import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import com.stericson.RootShell.exceptions.RootDeniedException
import com.stericson.RootShell.execution.Command
import com.stericson.RootTools.RootTools
import java.io.IOException
import java.util.concurrent.TimeoutException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addToolbar()
        //加载PrefFragment
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, SettingFragment())
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    private fun addToolbar(){
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.reboot_systemui -> {
                    if (RootTools.isRootAvailable()) {
                        // su exists, do something
                        val commandStr = "pkill -f com.android.systemui"
                        val command = Command(0, commandStr)
                        try {
                            RootTools.getShell(true).add(command)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: TimeoutException) {
                            e.printStackTrace()
                        } catch (e: RootDeniedException) {
                            e.printStackTrace()
                        }
                    } else {
                        // do something else
                        Toast.makeText(this, "请授予root权限", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            return@setOnMenuItemClickListener false
        }
    }

}
