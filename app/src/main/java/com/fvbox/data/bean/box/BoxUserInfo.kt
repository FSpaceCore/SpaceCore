package com.fvbox.data.bean.box

import com.fvbox.app.config.BoxConfig

/**
 *
 * @Description: box 内部的用户信息
 * @Author: Jack
 * @CreateDate: 2022/5/21 2:40
 */
data class BoxUserInfo(val userID: Int) {
    val userName: String
        get() {
            return BoxConfig.getUserRemark(userID)
        }
}
