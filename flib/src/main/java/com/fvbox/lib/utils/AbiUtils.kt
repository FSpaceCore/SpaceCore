package com.fvbox.lib.utils

import java.io.File
import java.util.*

class AbiUtils(apkFile: File?) {
    private val mLibs: MutableSet<String> = HashSet()
    fun is64Bit(): Boolean {
        return mLibs.contains("arm64-v8a")
    }

    fun is32Bit(): Boolean {
        return mLibs.contains("armeabi") || mLibs.contains("armeabi-v7a")
    }

    val isEmptyAib: Boolean
        get() = mLibs.isEmpty()

    companion object {
        private val sAbiUtilsMap: MutableMap<File, AbiUtils> = HashMap()
        fun isSupport(apkFile: File): Boolean {
            return true
        }
    }
}