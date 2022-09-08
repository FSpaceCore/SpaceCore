package com.fvbox.app.base

import androidx.fragment.app.Fragment
import com.fvbox.app.widget.LoadingDialog

/**
 *
 * @Description:
 * @Author: Jack
 * @CreateDate: 2022/5/18 21:08
 */
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
    fun <T> attachActivity(): T {
        return requireActivity() as T
    }
}
