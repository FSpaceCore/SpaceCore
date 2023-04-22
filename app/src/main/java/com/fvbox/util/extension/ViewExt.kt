package com.fvbox.util.extension

import android.content.Context
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.fvbox.R
import com.fvbox.data.bean.box.BoxAppBean

import com.fvbox.util.Log

fun View.isShow(any: Any?, hideVisibility: Int = View.GONE) {

    val show: Boolean = when (any) {
        is Boolean -> any
        is String -> any.isNotBlank()
        else -> any != null
    }

    if (show) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = hideVisibility
    }
}

fun View.setTwoStepClickListener(
        titleRes: Int,
        messageRes: Int,
        appBean: BoxAppBean,
        block: (BoxAppBean) -> Unit
) {
    this.setOnClickListener {
        this.context.showTwoStepDialog(titleRes, messageRes, appBean, block)
    }
}

fun Context.showTwoStepDialog(
        titleRes: Int,
        messageRes: Int,
        appBean: BoxAppBean,
        block: (BoxAppBean) -> Unit) {
    MaterialDialog(this).show {
        title(titleRes)
        message(text = com.fvbox.util.extension.getString(messageRes, appBean.name))
        negativeButton(R.string.cancel)
        positiveButton(R.string.done) {
            block.invoke(appBean)
        }
    }
}
