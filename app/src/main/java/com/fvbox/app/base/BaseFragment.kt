package com.fvbox.app.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.fvbox.R
import com.fvbox.app.ui.info.permission.AppPermissionFragment
import com.fvbox.app.widget.LoadingDialog


abstract class BaseFragment(layoutID: Int) : Fragment(layoutID) {

    private val mLoadingDialog: LoadingDialog by lazy {
        LoadingDialog()
    }

    fun showLoadingDialog() {
        runCatching {
            if (!mLoadingDialog.isAdded) {
                mLoadingDialog.show(parentFragmentManager, "Loading")
            }
        }
    }

    fun dismissLoadingDialog() {
        runCatching {
            mLoadingDialog.dismiss()
        }
    }

    /**
     * 如果activity不继承BaseActivity，那也没什么好说的
     */
    fun baseActivity(): BaseActivity {
        return requireActivity() as BaseActivity
    }

    @Suppress("UNCHECKED_CAST")
    open fun <T> attachActivity(): T {
        return requireActivity() as T
    }

}
