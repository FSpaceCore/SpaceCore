package com.fvbox.app.ui.welcome

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.fvbox.R
import com.fvbox.app.base.BaseActivity
import com.fvbox.app.ui.main.MainActivity
import com.fvbox.util.showToast
import com.permissionx.guolindev.PermissionX



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
                    go()
                } else {
                    showToast(getString(R.string.permission_request_denied))
                    finish()
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        go()
    }

    private fun go() {
        MainActivity.start(this)
        finish()
    }
}
