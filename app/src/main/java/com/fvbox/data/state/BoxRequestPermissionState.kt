package com.fvbox.data.state

import com.fvbox.data.bean.box.BoxAppBean
import com.fvbox.data.bean.box.BoxPermissionBean


sealed interface BoxRequestPermissionState{

    object Loading : BoxRequestPermissionState

    data class Empty(val appBean: BoxAppBean):BoxRequestPermissionState

    data class Success(val appBean: BoxAppBean,val list: List<String>) : BoxRequestPermissionState
}
