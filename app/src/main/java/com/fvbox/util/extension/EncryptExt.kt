package com.fvbox.util

import java.security.MessageDigest

/**
 *
 *@description:
 *@author: Jack
 *@create: 2022-06-11
 */

fun String.md5(): String {
    return try {
        val digest = MessageDigest.getInstance("MD5")
        val byteArray = digest.digest(this.toByteArray())
        byteToString(byteArray)
    } catch (e: Exception) {
        ""
    }
}

fun byteToString(byteArray: ByteArray): String {

    val sb = StringBuffer()
    for (b in byteArray) {
        //获取低八位有效值
        val i = b.toInt() and 0xff
        //将整数转化为16进制
        var hexString = Integer.toHexString(i)
        if (hexString.length < 2) {
            //如果是一位的话，补0
            hexString = "0$hexString"
        }
        sb.append(hexString)
    }
    return sb.toString()
}
