package com.fvbox.data.bean.box

import android.graphics.drawable.Drawable

/**
 *
 * @Description: box 已安装应用信息
 * @Author: Jack
 * @CreateDate: 2022/5/15 14:52
 */
data class BoxAppBean(
    var userID: Int,
    var pkg: String,
    var name: String,
    var icon: Drawable,
)
