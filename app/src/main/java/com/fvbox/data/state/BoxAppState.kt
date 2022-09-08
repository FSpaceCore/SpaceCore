package com.fvbox.data.state

import com.fvbox.data.bean.box.BoxAppBean

/**
 *
 * @Description: get box app state
 * @Author: Jack
 * @CreateDate: 2022/5/17 23:01
 */
sealed class BoxAppState {
    object Loading : BoxAppState()
    object Empty : BoxAppState()
    data class Success(val list: List<BoxAppBean>) : BoxAppState()
}