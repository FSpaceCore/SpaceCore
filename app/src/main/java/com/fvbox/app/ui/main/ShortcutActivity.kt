package com.fvbox.app.ui.main

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.fvbox.R
import com.fvbox.app.base.BaseActivity
import com.fvbox.lib.FCore
import com.fvbox.util.showSnackBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import com.fvbox.util.Log
class ShortcutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pkg = intent.getStringExtra("pkg")
        val userID = intent.getIntExtra("userId", 0)

        lifecycleScope.launch {
            runCatching {
                FCore.get().launchApk(pkg!!, userID)
                finish()
            }.onFailure {
                withContext(Dispatchers.Main) {
                    showSnackBar(getString(R.string.launch_fail))
                }
            }
        }
    }
}
