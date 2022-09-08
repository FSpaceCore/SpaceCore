package com.fvbox.app.config

import com.fvbox.R
import com.fvbox.util.extension.getString
import com.fvbox.util.property.MMKVProperty
import com.tencent.mmkv.MMKV

/**
 *
 * @Description: setting save
 * @Author: Jack
 * @CreateDate: 2022/5/15 23:16
 */
object BoxConfig {

    val mmkv = MMKV.mmkvWithID("FvBoxSetting")

    var currentUserID by MMKVProperty(0)

    var showShortcutPermissionDialog by MMKVProperty(true)

    fun getUserRemark(userID: Int): String {
        return mmkv.decodeString("UserRemark$userID") ?: "${
            getString(R.string.space)
        } $userID"
    }

    fun setUserRemark(userID: Int, remark: String) {
        mmkv.encode("UserRemark$userID", remark)
    }

    fun deleteUserRemark(userID: Int) {
        mmkv.removeValueForKey("UserRemark$userID")
    }

    fun isLogin(): Boolean {
        return true
    }
}
