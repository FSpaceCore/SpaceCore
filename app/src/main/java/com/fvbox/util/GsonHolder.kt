package com.fvbox.util

import com.google.gson.Gson

object GsonHolder {

    val gson by lazy {
        Gson()
    }
}
