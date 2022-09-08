package com.fvbox.app.ui.welcome

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.fvbox.R
import com.fvbox.app.base.BaseActivity
import com.fvbox.app.ui.main.MainActivity
import com.fvbox.data.AppRepository
import com.fvbox.util.showToast
import com.permissionx.guolindev.PermissionX

/**
 *
 *@description: welcome activity
 *@author: Jack
 *@create: 2022-05-28
 */

class WelcomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionX.init(this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    getString(R.string.permission_to_setting),
                    getString(R.string.done),
                    getString(R.string.cancel)
                )
            }.request { allGranted, _, _ ->
                if (allGranted) {
                    preload()
                } else {
                    showToast(getString(R.string.permission_denied))
                    finish()
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        preload()
    }

    /**
     * 这里就没必要套viewModel了
     */
    private fun preload() {
        go()
    }

    private fun go() {
        MainActivity.start(this)
        finish()
    }
}
