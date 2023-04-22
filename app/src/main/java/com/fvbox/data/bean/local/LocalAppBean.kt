package com.fvbox.data.bean.local

import android.graphics.drawable.Drawable
import java.io.File


data class LocalAppBean(
    var name: String,
    var pkg: String = "",
    var path: File? = null,
    var icon: Drawable,
    var isSupport: Boolean
)
