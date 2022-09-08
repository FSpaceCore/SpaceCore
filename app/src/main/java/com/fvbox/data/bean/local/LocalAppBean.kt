package com.fvbox.data.bean.local

import android.graphics.drawable.Drawable

/**
 *
 * @Description:
 * @Author: Jack
 * @CreateDate: 2022/5/15 14:55
 */
data class LocalAppBean(
    var name: String,
    var pkg: String,
    var icon: Drawable,
    var isSupport: Boolean
)
