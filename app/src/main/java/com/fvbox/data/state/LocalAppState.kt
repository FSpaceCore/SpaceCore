package com.fvbox.data.state

import com.fvbox.data.bean.local.LocalAppBean

import com.fvbox.util.Log
sealed class LocalAppState {

    object Loading : LocalAppState()

    object Empty : LocalAppState()

    data class Success(val list: List<LocalAppBean>) : LocalAppState()
}
