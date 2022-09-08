package com.fvbox.util

import android.annotation.SuppressLint
import android.content.Context

/**
 *
 * @Description: context
 * @Author: Jack
 * @CreateDate: 2022/5/15 14:56
 */
@SuppressLint("StaticFieldLeak")
object ContextHolder {

    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    fun get(): Context {
        return context
    }
}