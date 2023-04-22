package com.fvbox.data.state

import com.fvbox.data.bean.box.BoxAppBean


sealed class BoxAppState {
    object Loading : BoxAppState()
    object Empty : BoxAppState()
    data class Success(val list: List<BoxAppBean>) : BoxAppState()
}
