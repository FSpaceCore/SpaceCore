package com.fvbox.app.ui.setting.device

import com.fvbox.app.base.BaseViewModel
import com.fvbox.data.state.BoxActionState
import com.fvbox.lib.FCore



class FakeDeviceViewModel : BaseViewModel() {

    fun originDevice(userID: Int) {
        mBoxActionState.postValue(BoxActionState.Loading)
        launch {
            FCore.get().setDevice(userID, null)
            //success事件会重新get
            mBoxActionState.postValue(BoxActionState.Success())
        }
    }
}
