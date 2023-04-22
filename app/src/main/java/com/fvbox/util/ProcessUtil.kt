package com.fvbox.util

import com.fvbox.lib.FCore

import com.fvbox.util.Log
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
