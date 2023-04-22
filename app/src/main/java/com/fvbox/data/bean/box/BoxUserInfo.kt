package com.fvbox.data.bean.box

import com.fvbox.app.config.BoxConfig


data class BoxUserInfo(val userID: Int) {
    val userName: String
        get() {
            return BoxConfig.getUserRemark(userID)
        }
}
