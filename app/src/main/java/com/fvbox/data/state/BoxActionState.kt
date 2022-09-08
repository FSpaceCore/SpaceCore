package com.fvbox.data.state

/**
 *
 * @Description:
 * @Author: Jack
 * @CreateDate: 2022/5/17 21:49
 */
sealed class BoxActionState {

    object Loading : BoxActionState()

    data class Fail(val msg: String) : BoxActionState()

    data class Success(val msg: String = "") : BoxActionState()
}