package com.fvbox.app.widget

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import com.fvbox.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class LoadingDialog : BottomSheetDialogFragment() {

    private var mainDialog: Dialog? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mainDialog = Dialog(requireActivity()).apply {
            setContentView(R.layout.dialog_loading)
            window?.setGravity(Gravity.CENTER)

            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }

        return mainDialog!!
    }


}
