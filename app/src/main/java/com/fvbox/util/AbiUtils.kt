package com.fvbox.util

import com.fvbox.lib.FCore
import java.io.File
import java.util.*
import java.util.zip.ZipFile

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
            var abiUtils = sAbiUtilsMap[apkFile]
            if (abiUtils == null) {
                abiUtils = AbiUtils(apkFile)
                sAbiUtilsMap[apkFile] = abiUtils
            }
            if (abiUtils.isEmptyAib) {
                return true
            }
            return if (FCore.is64Bit()) {
                abiUtils.is64Bit()
            } else {
                abiUtils.is32Bit()
            }
        }
    }

    init {
        var zipFile: ZipFile? = null
        try {
            zipFile = ZipFile(apkFile)
            val entries = zipFile.entries()
            while (entries.hasMoreElements()) {
                val zipEntry = entries.nextElement()
                val name = zipEntry.name
                when {
                    name.startsWith("lib/arm64-v8a") -> {
                        mLibs.add("arm64-v8a")
                    }
                    name.startsWith("lib/armeabi") -> {
                        mLibs.add("armeabi")
                    }
                    name.startsWith("lib/armeabi-v7a") -> {
                        mLibs.add("armeabi-v7a")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            zipFile?.close()
        }
    }
}
