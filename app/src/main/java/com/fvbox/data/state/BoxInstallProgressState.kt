package com.fvbox.data.state

import com.fvbox.data.bean.box.BoxInstallBean
import com.fvbox.data.bean.local.LocalAppBean


sealed class BoxInstallProgressState {

    data class Loading(val pkg: String, val userName: String) : BoxInstallProgressState()

    data class Finish(val userName: String, val installBean: BoxInstallBean) :
        BoxInstallProgressState()

    data class Header(val appBean: LocalAppBean) : BoxInstallProgressState()
}
