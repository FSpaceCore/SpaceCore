package com.fvbox.data

import android.net.Uri
import android.os.ParcelFileDescriptor
import com.fvbox.lib.FCore
import com.fvbox.util.ContextHolder
import java.io.File
import java.io.FileOutputStream



object BackRepository {

    fun importData(userID: Int, uri: Uri): String? {
        val file = File(ContextHolder.get().externalCacheDir, "${System.currentTimeMillis()}.zip")
        var msg: String?
        try {
            ContextHolder.get().contentResolver.openInputStream(uri).use { input ->
                file.outputStream().use { output ->
                    input?.copyTo(output)
                }
            }
            val result = FCore.get().importAppData(userID, file)
            msg = if (result.isSuccess) {
                null
            } else {
                result.msg
            }
        } catch (e: Exception) {
            msg = e.message
        } finally {
            file.delete()
        }

        return msg
    }

    fun exportData(userID: Int, pkg: String, uri: Uri): String? {
        val file = File(ContextHolder.get().externalCacheDir, "${System.currentTimeMillis()}.zip")
        var msg: String? = null
        try {
            val result = FCore.get().exportAppData(userID, pkg, file)
            if (!result.isSuccess) {
                msg = result.msg
                return msg
            }
            val pfd: ParcelFileDescriptor? =
                ContextHolder.get().contentResolver.openFileDescriptor(uri, "w")

            pfd?.let {
                FileOutputStream(it.fileDescriptor).use { output ->
                    output.channel.truncate(0)
                    //覆盖的话重新设置大小
                    file.inputStream().use { input ->
                        input.copyTo(output)
                    }
                }
            }

        } catch (e: Exception) {
            msg = e.message
        } finally {
            file.delete()
        }
        return msg
    }
}
