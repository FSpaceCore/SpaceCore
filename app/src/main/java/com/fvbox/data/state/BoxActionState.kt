package com.fvbox.data.state


sealed class BoxActionState {

    object Loading : BoxActionState()

    data class Fail(val msg: String) : BoxActionState()

    data class Success(val msg: String = "") : BoxActionState()
}
