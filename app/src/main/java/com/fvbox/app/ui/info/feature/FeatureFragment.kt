package com.fvbox.app.ui.info.feature

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.fvbox.R
import com.fvbox.app.ui.info.AppInfoActivity
import com.fvbox.app.ui.setting.*
import com.fvbox.data.bean.db.RuleBean
import com.fvbox.data.bean.db.RuleState
import com.fvbox.data.state.BoxConfigRuleState
import com.fvbox.util.hideSnackBar
import com.fvbox.util.showSnackBar


class FeatureFragment : PreferenceFragment() {

    private val appInfoActivity by lazy {
        requireActivity() as AppInfoActivity
    }

    private val appBean by lazy {
        appInfoActivity.getAppBean()
    }

    private val viewModel by viewModels<FeatureViewModel>()

    private var ruleList: List<RuleBean>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadConfig(appBean.userID,appBean.pkg)
        viewModel.configState.observe(viewLifecycleOwner) {
            when (it) {
                is BoxConfigRuleState.Loading -> {}

                is BoxConfigRuleState.Success -> {
                    ruleList = it.list
                    loadData()
                }
                is BoxConfigRuleState.Updated -> {
                    showSnackBar(getString(R.string.success_with_reboot), getString(R.string.reboot_now)) {
                        viewModel.restartSystemProcess()
                        showSnackBar(getString(R.string.reboot_success))
                    }
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        appInfoActivity.setToolbarTitle(R.string.feature_setting)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            appInfoActivity.setToolbarTitle(R.string.feature_setting)
        }
    }

    override fun onStop() {
        super.onStop()
        hideSnackBar()
    }

    override fun getSettingTypeList(): Array<BaseBoxPreference> {
        if (ruleList == null){
            return arrayOf(TitleBoxPreference(R.string.general_settings))
        }

        return arrayOf(
            TitleBoxPreference(R.string.general_settings),
            StateChangePreference(
                R.string.setting_force_io,
                R.string.setting_force_io_sub,
                RuleState.HIDE_PATH,
                configProperty
            ),
            StateChangePreference(
                R.string.setting_visible_package,
                R.string.setting_visible_package_sub,
                RuleState.VISIBLE_APP,
                configProperty
            ),
            StateChangePreference(
                R.string.setting_hide_sim,
                R.string.setting_hide_sim_sub,
                RuleState.HIDE_SIM,
                configProperty
            ),
            StateChangePreference(
                R.string.setting_disable_network,
                R.string.setting_disable_network_sub,
                RuleState.DISABLE_NETWORK,
                configProperty
            ),
            StateChangePreference(
                R.string.setting_hide_root,
                R.string.setting_hide_root_sub,
                RuleState.HIDE_ROOT,
                configProperty
            ),
            StateChangePreference(
                R.string.setting_hide_vpn,
                R.string.setting_hide_vpn_sub,
                RuleState.HIDE_VPN,
                configProperty
            ),
            StateChangePreference(
                R.string.setting_disable_kill,
                R.string.setting_disable_kill_sub,
                RuleState.ANTI_CRASH,
                configProperty
            )
        )
    }


    private val configProperty = object : State<Int, Boolean> {
        override fun get(type: Int): Boolean {
            return ruleList?.find { it.type == type } != null
        }

        override fun set(type: Int, value: Boolean) {
            viewModel.changeConfig(appBean.userID, appBean.pkg, type, value)
        }

    }

}
