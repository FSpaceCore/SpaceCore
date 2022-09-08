package com.fvbox.data.state

import com.fvbox.data.bean.box.BoxInstallBean
import com.fvbox.data.bean.local.LocalAppBean

/**
 *
 * @Description: 安装界面的状态
 * @Author: Jack
 * @CreateDate: 2022/5/28 0:36
 */
sealed class BoxInstallProgressState {

    data class Loading(val pkg: String, val userName: String) : BoxInstallProgressState()

    data class Finish(val userName: String, val installBean: BoxInstallBean) :
        BoxInstallProgressState()

    data class Header(val appBean: LocalAppBean) : BoxInstallProgressState()
}
