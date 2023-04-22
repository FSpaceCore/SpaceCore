package com.fvbox.app.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.fvbox.R
import com.fvbox.app.base.BaseActivity
import com.fvbox.app.config.BoxConfig
import com.fvbox.app.ui.setting.device.FakeDeviceFragment


class PreferenceActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setUpToolbar(R.string.app_setting, true)

        initFragment()
    }

    private fun initFragment() {
        val fragment = when (intent.getIntExtra(PREFERENCE_TYPE, DeviceSetting)) {
            DeviceSetting -> {
                setToolbarTitle(R.string.fake_device_setting)
                FakeDeviceFragment.create(currentUserID())
            }
            else -> Fragment()
        }

        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, fragment)
        }

    }

    override fun initToolbar() {

        val userID = intent.getIntExtra(INTENT_USERID, -1)

        if (userID == -1) {
            return
        }

        toolbar.subtitle = BoxConfig.getUserRemark(userID)
    }

    companion object {
        const val GeneralSettings = 0
        const val DeviceSetting = 1

        private const val PREFERENCE_TYPE = "Intent_Type"

        fun start(activity: BaseActivity, type: Int, userID: Int? = -1) {
            val intent = Intent()
            intent.putExtra(PREFERENCE_TYPE, type)
            intent.putExtra(INTENT_USERID, userID)
            intent.setClass(activity, PreferenceActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
