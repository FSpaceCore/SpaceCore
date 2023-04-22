package com.fvbox.app.ui.info

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.fvbox.R
import com.fvbox.app.base.BaseActivity
import com.fvbox.app.ui.box.BoxAppViewModel
import com.fvbox.data.bean.box.BoxAppBean
import com.fvbox.databinding.ActivityAppInfoBinding
import com.fvbox.lib.FCore
import com.fvbox.util.property.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AppInfoActivity : BaseActivity() {

    private val binding by viewBinding(ActivityAppInfoBinding::bind)

    private val viewModel by viewModels<BoxAppViewModel>()

    private lateinit var appBean: BoxAppBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_info)
        setUpToolbar(R.string.app_info, true)
        initData()
        observeData()
    }

    override fun initToolbar() {
        binding.toolbarLayout.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun observeData() {
        viewModel.boxActionState.observe(this, boxActionStateObserver)
    }

    private fun initData() {
        val pkg = intent.getStringExtra(INTENT_PACKAGE).toString()

        lifecycleScope.launch {
            val packageInfo = withContext(Dispatchers.IO) {
                FCore.get().getPackageInfo(pkg, PackageManager.GET_META_DATA, currentUserID())
            }
            //别跟我说什么设计模式，老夫就是梭哈

            if (packageInfo == null) {
                finish()
                return@launch
            }
            val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
            val icon = packageInfo.applicationInfo.loadIcon(packageManager)

            appBean = BoxAppBean(currentUserID(), pkg, appName, icon)

            withContext(Dispatchers.Main) {
                AppInfoFragment.show(supportFragmentManager)
            }
        }

    }

    fun getAppBean(): BoxAppBean {
        return appBean
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            return
        }

        finish()
    }


    companion object {

        const val INTENT_PACKAGE = "IntentPkg"

        const val REQUEST_CODE = 115

        const val RESULT_UNINSTALL = 120

        fun start(activity: BaseActivity, userID: Int, pkg: String) {
            val intent = Intent(activity, AppInfoActivity::class.java)
            intent.putExtra(INTENT_USERID, userID)
            intent.putExtra(INTENT_PACKAGE, pkg)
            activity.startActivityForResult(intent, REQUEST_CODE)
        }
    }
}
