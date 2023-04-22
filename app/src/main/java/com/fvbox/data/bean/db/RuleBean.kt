package com.fvbox.data.bean.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "rule")
data class RuleBean(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "pkg", index = true) val packageName: String,
    @ColumnInfo(name = "userID", index = true) val userID: Int,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "component") val componentName: String
)

data class RulePackageBean(
    @ColumnInfo(name = "pkg") val packageName: String,
    @ColumnInfo(name = "userID") val userID: Int,
)

object RuleState {
    const val ACTIVITY = 97  //'a'
    const val SERVICE = 115  //'s'
    const val PROCESS = 112  //'p'
    const val BROADCAST = 98  //'b'
    const val CONTENT_PROVIDER = 99  //'c'
    //之前用char做判断

    const val HIDE_PATH = 200
    const val HIDE_ROOT = 201
    const val HIDE_VPN = 202
    const val HIDE_SIM = 203
    const val VISIBLE_APP = 204
    const val DISABLE_NETWORK = 205
    const val ANTI_CRASH = 206
}
