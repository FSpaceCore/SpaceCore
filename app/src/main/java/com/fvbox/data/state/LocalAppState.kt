package com.fvbox.data.state

import com.fvbox.data.bean.local.LocalAppBean

/**
 *
 * @Description:
 * @Author: Jack
 * @CreateDate: 2022/5/16 22:55
 */
sealed class LocalAppState {

    object Loading : LocalAppState()

    object Empty : LocalAppState()

    data class Success(val list: List<LocalAppBean>) : LocalAppState()
}