package com.fvbox.app.ui.setting.setting

import com.fvbox.R
import com.fvbox.app.ui.setting.BaseBoxPreference
import com.fvbox.app.ui.setting.PreferenceFragment
import com.fvbox.app.ui.setting.TitleBoxPreference

/**
 *
 * @Description: 设置界面
 * @Author: Jack
 * @CreateDate: 2022/5/31 22:45
 */
class SettingFragment : PreferenceFragment() {

    override fun getSettingTypeList(): Array<BaseBoxPreference> {
        return arrayOf(
            TitleBoxPreference(R.string.core_settings),
//            SwitchBoxPreference(
//                R.string.setting_use_hook,
//                R.string.setting_use_hook_sub,
//                FSystemConfig::useHook
//            ),
//            SwitchBoxPreference(
//                R.string.setting_stable_pattern,
//                R.string.setting_stable_pattern_sub,
//                FSystemConfig::stablePattern
//            ),
//            TitleBoxPreference(R.string.general_settings),
//            SwitchBoxPreference(
//                R.string.setting_force_io,
//                R.string.setting_force_io_sub,
//                FSystemConfig::forceIO
//            ),
//            SwitchBoxPreference(
//                R.string.setting_visible_package,
//                R.string.setting_visible_package_sub,
//                FSystemConfig::visitExternalApp
//            ),
//            SwitchBoxPreference(
//                R.string.setting_hide_sim,
//                R.string.setting_hide_sim_sub,
//                FSystemConfig::hideSim
//            ),
//            SwitchBoxPreference(
//                R.string.setting_hide_root,
//                R.string.setting_hide_root_sub,
//                FSystemConfig::hideRoot
//            ),
//            SwitchBoxPreference(
//                R.string.setting_hide_vpn,
//                R.string.setting_hide_vpn_sub,
//                FSystemConfig::hideVPN
//            ),
//            SwitchBoxPreference(
//                R.string.setting_disable_kill,
//                R.string.setting_disable_kill_sub,
//                FSystemConfig::disableKill
//            )
        )
    }
}
