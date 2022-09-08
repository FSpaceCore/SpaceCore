package com.fvbox.util.extension

import android.view.View

/**
 *
 * @Description:
 * @Author: Jack
 * @CreateDate: 2022/5/29 23:09
 */

fun View.isShow(any: Any?, hideVisibility: Int = View.GONE) {

    val show: Boolean = when (any) {
        is Boolean -> any
        is String -> any.isNotBlank()
        else -> any != null
    }

    if (show) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = hideVisibility
    }
}
