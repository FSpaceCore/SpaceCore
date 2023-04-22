package com.fvbox.data.state

import com.fvbox.data.bean.box.BoxComponentBean


sealed interface BoxComponentState {

    object Loading : BoxComponentState

    data class Success(val list: List<BoxComponentBean>) : BoxComponentState
}
