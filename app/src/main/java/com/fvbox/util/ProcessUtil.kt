package com.fvbox.util

import com.fvbox.lib.FCore

/**
 *
 * @description: 进程相关工具
 * @author: Jack
 * @create: 2022-07-06
 */
object ProcessUtil {

    fun currentBit(): Int {
        return if (FCore.is64Bit()) {
            64
        } else {
            32
        }
    }

    fun noSupportBit(): Int {
        return return if (FCore.is64Bit()) {
            32
        } else {
            64
        }
    }
}
