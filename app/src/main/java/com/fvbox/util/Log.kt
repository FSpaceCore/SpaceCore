package com.fvbox.util

import android.util.Log

object Log {

    private const val TAG = "FxBox"

    fun e(any: Any?){
        Log.e(TAG, any.toString())
    }
}
