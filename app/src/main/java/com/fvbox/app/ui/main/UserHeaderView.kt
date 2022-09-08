package com.fvbox.app.ui.main

import android.annotation.SuppressLint
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.fvbox.BuildConfig
import com.fvbox.R
import com.fvbox.databinding.ViewMainHeaderBinding

/**
 *
 *@description:
 *@author: Jack
 *@create: 2022-06-11
 */

@SuppressLint("SetTextI18n")
class UserHeaderView(view: View, private val activity: MainActivity, private val viewModel: MainViewModel) {

    private val binding = ViewMainHeaderBinding.bind(view)

    private var isLogin = false

    init {
        initGlobalClick()
        observeData()
        binding.version.text = "v${BuildConfig.VERSION_NAME}"
    }

    private fun initGlobalClick() {
        binding.root.setOnClickListener {

        }
    }

    private fun logout() {
        MaterialDialog(activity).show {
            title(R.string.logout)
            message(R.string.logout_msg)
            negativeButton(R.string.cancel)
            positiveButton(R.string.done) {
            }
        }
    }


    private fun observeData() {
    }
}
