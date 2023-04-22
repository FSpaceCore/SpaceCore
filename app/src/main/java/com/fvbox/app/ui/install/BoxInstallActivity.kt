package com.fvbox.app.ui.install

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import com.fvbox.R
import com.fvbox.app.base.BaseActivity
import com.fvbox.databinding.ActivityInstallBinding
import com.fvbox.util.property.viewBinding


class BoxInstallActivity : BaseActivity() {

    private val binding by viewBinding(ActivityInstallBinding::bind)

    private val viewModel by viewModels<BoxInstallViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_install)

        setUpToolbar(R.string.install_app, true)
        initData()
    }

    private fun initData() {
        val uri = intent.getParcelableExtra<Uri>(APK_URI)
        if (uri != null) {
            viewModel.loadData(uri)
        } else {
            val list = intent.getStringArrayListExtra(PACKAGE_LIST)
            viewModel.loadData(list)
        }

    }

    override fun onBackPressed() {
        finish()
    }


    companion object {

        const val REQUEST_CODE = 113

        private const val PACKAGE_LIST = "pkgList"

        private const val APK_URI = "apkUri"

        fun start(activity: BaseActivity, currentUser: Int, pkgList: ArrayList<String>) {
            val intent = Intent(activity, BoxInstallActivity::class.java)
            intent.putExtra(INTENT_USERID, currentUser)
            intent.putStringArrayListExtra(PACKAGE_LIST, pkgList)
            activity.startActivityForResult(intent, REQUEST_CODE)
        }

        fun start(activity: BaseActivity, currentUser: Int, uri: Uri) {
            val intent = Intent(activity, BoxInstallActivity::class.java)
            intent.putExtra(INTENT_USERID, currentUser)
            intent.putExtra(APK_URI, uri)
            activity.startActivityForResult(intent, REQUEST_CODE)
        }
    }
}
