package com.fvbox.util

import android.annotation.SuppressLint
import android.content.Context

import com.fvbox.util.Log
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
