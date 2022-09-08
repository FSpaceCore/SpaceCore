package com.fvbox.app.ui.setting.device

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.fvbox.R
import com.fvbox.app.base.BaseActivity
import com.fvbox.app.ui.setting.*
import com.fvbox.data.state.BoxActionState
import com.fvbox.util.showSnackBar

/**
 *
 * @Description: 设备信息修改
 * @Author: Jack
 * @CreateDate: 2022/6/1 22:46
 */
class FakeDeviceFragment : PreferenceFragment() {

    private lateinit var deviceMapping: DeviceMapping

    private lateinit var languageMapping: LanguageMapping

    private var userID: Int = 0

    private val viewModel by viewModels<FakeDeviceViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        observeData()
    }

    override fun initData() {
        userID = requireArguments().getInt(BaseActivity.INTENT_USERID)
        deviceMapping = DeviceMapping(userID)
        languageMapping = LanguageMapping(userID)
    }

    private fun observeData() {
        viewModel.boxActionState.observe(this) {
            when (it) {
                is BoxActionState.Loading -> {
                    attachActivity().showLoadingDialog()
                }

                is BoxActionState.Fail -> {
                    attachActivity().dismissLoadingDialog()
                    showSnackBar(it.toString())
                }

                is BoxActionState.Success -> {
                    deviceMapping.refresh()
                    loadData()
                    attachActivity().dismissLoadingDialog()
                }
            }
        }
    }

    override fun getSettingTypeList(): Array<BaseBoxPreference> {
        return arrayOf(
            TitleBoxPreference(R.string.fake_language),
            SingleChoicePreference(
                R.string.app_language,
                languageMapping.getLanguageList(),
                languageMapping::language
            ),
            SingleChoicePreference(
                R.string.app_country,
                languageMapping.getRegionList(),
                languageMapping::region
            ),
            SingleChoicePreference(
                R.string.app_timezone,
                languageMapping.getTimeZoneList(),
                languageMapping::timeZone
            ),
            TitleBoxPreference(R.string.fake_device_setting),
            SwitchBoxPreference(
                R.string.fake_device_setting,
                R.string.fake_device_enable,
                deviceMapping::enable
            ),
            ClickPreference(R.string.origin_device_config, this::originDeviceInfo),
            ClickPreference(R.string.random_device_config, this::randomDeviceInfo),
            EditBoxPreference(R.string.fake_device_device, deviceMapping::device),
            EditBoxPreference(R.string.fake_device_board, deviceMapping::board),
            EditBoxPreference(R.string.fake_device_brand, deviceMapping::brand),
            EditBoxPreference(R.string.fake_device_wifiMac, deviceMapping::wifiMac),
            EditBoxPreference(R.string.fake_device_hardware, deviceMapping::hardware),
            EditBoxPreference(R.string.fake_device_manufacturer, deviceMapping::manufacturer),
            EditBoxPreference(R.string.fake_device_product, deviceMapping::product),
            EditBoxPreference(R.string.fake_device_model, deviceMapping::model),
            EditBoxPreference(R.string.fake_device_serial, deviceMapping::serial),
            EditBoxPreference(R.string.fake_device_androidId, deviceMapping::androidId),
            EditBoxPreference(R.string.fake_device_deviceId, deviceMapping::deviceId),
            EditBoxPreference(R.string.fake_device_id, deviceMapping::id),
            EditBoxPreference(R.string.fake_device_bootId, deviceMapping::bootId),
        )
    }

    private fun randomDeviceInfo() {
        viewModel.randomDevice(userID)
    }

    private fun originDeviceInfo() {
        viewModel.originDevice(userID)
    }


    private fun attachActivity(): PreferenceActivity {
        return requireActivity() as PreferenceActivity
    }


    companion object {
        fun create(userID: Int): FakeDeviceFragment {
            val fragment = FakeDeviceFragment()
            fragment.arguments = bundleOf(BaseActivity.INTENT_USERID to userID)
            return fragment
        }
    }
}
