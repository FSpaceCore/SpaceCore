package com.fvbox.util

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fvbox.R
import com.fvbox.util.extension.getString
import com.google.android.material.snackbar.Snackbar
import com.gyf.immersionbar.ImmersionBar


import com.fvbox.util.Log

var mToast: Toast? = null

fun showToast(any: Any?) {
    when {
        any.toString().isBlank() -> {
            return
        }
        else -> {
            mToast?.cancel()

            mToast = Toast.makeText(ContextHolder.get(), any.toString(), Toast.LENGTH_SHORT)
            mToast?.show()
        }
    }
}


var mSnackBar: Snackbar? = null

fun View.showSnackBar(
    text: String,
    actionText: String? = null,
    actionCallback: ((View) -> Unit)? = null
) {
    if (text.isBlank()) {
        return
    }
    mSnackBar?.dismiss()
    mSnackBar = Snackbar.make(this, text, Snackbar.LENGTH_SHORT)
    mSnackBar?.setAction(actionText ?: getString(R.string.done), actionCallback)

    runCatching {
        val params = mSnackBar?.view?.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = ImmersionBar.getNavigationBarHeight(context)
        mSnackBar?.view?.layoutParams = params
    }

    mSnackBar?.show()

}

fun View.showSnackBar(
    any: Any?,
    actionText: String? = null,
    actionCallback: ((View) -> Unit)? = null
) {
    if (any == null) {
        return
    }
    showSnackBar(any.toString(), actionText, actionCallback)

}

fun AppCompatActivity.showSnackBar(
    any: Any?,
    actionText: String? = null,
    actionCallback: ((View) -> Unit)? = null
) {
    this.findViewById<View>(android.R.id.content)?.showSnackBar(any, actionText, actionCallback)
}

fun Fragment.showSnackBar(
    any: Any?,
    actionText: String? = null,
    actionCallback: ((View) -> Unit)? = null
) {
    if (this.isRemoving || this.isDetached) {
        return
    }
    if (any == null) {
        return
    }
    view?.showSnackBar(any, actionText, actionCallback)
}

fun hideSnackBar(){
    mSnackBar?.dismiss()
}
