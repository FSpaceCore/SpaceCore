package com.fvbox.app.ui.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.fvbox.R
import com.fvbox.app.base.BaseFragment
import com.fvbox.app.ui.box.BoxAppViewModel
import com.fvbox.databinding.FragmentAppInfoBinding
import com.fvbox.util.extension.setTwoStepClickListener
import com.fvbox.util.property.viewBinding


class AppInfoFragment : BaseFragment(R.layout.fragment_app_info) {

    private val binding by viewBinding(FragmentAppInfoBinding::bind)

    private val viewModel by activityViewModels<BoxAppViewModel>()

    private val appBean by lazy { attachActivity<AppInfoActivity>().getAppBean() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        baseActivity().setToolbarTitle(R.string.app_info)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden){
            baseActivity().setToolbarTitle(R.string.app_info)
        }
    }

    private fun initView() {
        binding.btnForce.setTwoStepClickListener(
            R.string.force_stop,
            R.string.force_stop_hint,
            appBean,
            viewModel::forceStop
        )
        binding.btnClean.setTwoStepClickListener(
            R.string.clear_data,
            R.string.clear_data_hint,
            appBean,
            viewModel::clearData
        )
        binding.btnUninstall.setTwoStepClickListener(
            R.string.uninstall_app,
            R.string.uninstall_app_hint,
            appBean
        ) {
            viewModel.unInstallApp(it)
            baseActivity().setResult(AppInfoActivity.RESULT_UNINSTALL)
            baseActivity().finish()
        }

        binding.ivAppLogo.setImageDrawable(appBean.icon)
        binding.tvAppName.text = appBean.name
    }

    companion object {

        fun show(fragmentManager: FragmentManager) {
            fragmentManager.commit {
                add(R.id.fragment_container, AppInfoFragment())
            }
        }
    }
}
