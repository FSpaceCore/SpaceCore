package com.fvbox.app.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.fvbox.data.bean.box.BoxAppBean
import com.fvbox.util.extension.nowStr

/**
 *
 * @description: create zip for SAF
 * @author: Jack
 * @create: 2022-06-12
 */
class SaveZipDocument : ActivityResultContract<BoxAppBean, SaveZipDocument.SaveZipBean>() {

    var userID = 0
    var pkg = ""

    override fun createIntent(context: Context, input: BoxAppBean): Intent {
        val title = "${input.name}-${nowStr()}.zip"
        userID = input.userID
        pkg = input.pkg
        return Intent(Intent.ACTION_CREATE_DOCUMENT)
            .setType("*/*")
            .putExtra(Intent.EXTRA_TITLE, title)
    }

    final override fun parseResult(resultCode: Int, intent: Intent?): SaveZipBean {
        val uri = intent.takeIf { resultCode == Activity.RESULT_OK }?.data
        return SaveZipBean(uri, userID, pkg)
    }

    data class SaveZipBean(val uri: Uri?, val userID: Int, val pkg: String)
}
