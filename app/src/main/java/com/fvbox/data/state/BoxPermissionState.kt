package com.fvbox.data.state

import com.fvbox.data.bean.box.BoxAppBean
import com.fvbox.data.bean.box.BoxPermissionBean


sealed interface BoxPermissionState{

    object Loading : BoxPermissionState

    data class Success(val appBean: BoxAppBean,val list: List<BoxPermissionBean>) : BoxPermissionState
}
