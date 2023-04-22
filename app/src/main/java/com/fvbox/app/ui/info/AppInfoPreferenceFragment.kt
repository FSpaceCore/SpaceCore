package com.fvbox.app.ui.info

import androidx.fragment.app.activityViewModels
import com.fvbox.R
import com.fvbox.app.contract.SaveZipDocument
import com.fvbox.app.ui.box.BoxAppViewModel
import com.fvbox.app.ui.info.component.AppComponentHostFragment
import com.fvbox.app.ui.info.feature.FeatureFragment
import com.fvbox.app.ui.info.permission.AppPermissionFragment
import com.fvbox.app.ui.setting.BaseBoxPreference
import com.fvbox.app.ui.setting.ClickPreference
import com.fvbox.app.ui.setting.PreferenceFragment
import com.fvbox.app.ui.setting.TitleBoxPreference
import com.fvbox.util.ShortcutUtil
import com.fvbox.util.extension.showFragment


class AppInfoPreferenceFragment : PreferenceFragment() {

    private val appInfoActivity by lazy {
        requireActivity() as AppInfoActivity
    }

    private val appBean by lazy {
        appInfoActivity.getAppBean()
    }

    private val viewModel by activityViewModels<BoxAppViewModel>()

    override fun getSettingTypeList(): Array<BaseBoxPreference> {
        return arrayOf(
            TitleBoxPreference(R.string.app_manager),
            ClickPreference(R.string.package_manger, R.string.package_manager_hint) {
                showFragment(requireActivity().supportFragmentManager) { AppComponentHostFragment() }
            },
            ClickPreference(R.string.permission_manager, R.string.permission_manager_hint) {
                showFragment(requireActivity().supportFragmentManager) { AppPermissionFragment() }
            },
            ClickPreference(R.string.feature_setting, R.string.feature_setting_hint) {
                showFragment(requireActivity().supportFragmentManager) { FeatureFragment() }
            },
            ClickPreference(R.string.app_shortcut, R.string.app_shortcut_hint) {
                ShortcutUtil.createShortcut(requireContext(), appBean)
            },
            ClickPreference(R.string.export_data, R.string.export_data_hint) {
                saveDocument.launch(appBean)
            }
        )
    }


    private val saveDocument = registerForActivityResult(SaveZipDocument()) {

        if (it.uri != null) {
            viewModel.exportData(it.userID, it.pkg, it.uri)
        }
    }

}
