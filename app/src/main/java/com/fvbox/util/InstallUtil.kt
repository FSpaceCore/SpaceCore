package com.fvbox.util

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

/**
 *
 * @description:
 * @author: Jack
 * @create: 2022-07-04
 */
object InstallUtil {

    fun install(apk: String) {

        runCatching { //这里有文件流的读写，需要处理一下异常
            val context = ContextHolder.get()
            val authority = "${context.packageName}.provider"
            val file = File(apk)
            val uri: Uri
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //如果SDK版本 =24，即：Build.VERSION.SDK_INT  = 24
                val packageName: String = context.applicationContext.packageName
                uri = FileProvider.getUriForFile(context, authority, file)
                intent.setDataAndType(uri, "application/vnd.android.package-archive")
            } else {
                uri = Uri.fromFile(file)
                intent.setDataAndType(uri, "application/vnd.android.package-archive")
            }
            context.startActivity(intent)
        }.onFailure {
        }
    }

}
