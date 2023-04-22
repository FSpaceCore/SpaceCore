package com.fvbox.data.state

import com.fvbox.data.bean.box.BoxUserInfo
import com.fvbox.data.bean.local.LocalAppBean


sealed class BoxInstallUserState {
    /**
     * 首次进入界面，加载用户以及图标
     */
    object Loading : BoxInstallUserState()

    object Empty : BoxInstallUserState()

    /**
     * 加载成功
     * @property appList List<LocalAppBean> app列表
     * @property userList List<BoxUserInfo> 用户列表
     * @constructor
     */
    data class LoadSuccess(val appList: List<LocalAppBean>, val userList: List<BoxUserInfo>) :
        BoxInstallUserState()

}


