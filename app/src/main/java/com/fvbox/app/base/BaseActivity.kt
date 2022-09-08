package com.fvbox.app.base

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.fvbox.R
import com.fvbox.app.widget.LoadingDialog
import com.fvbox.data.state.BoxActionState
import com.fvbox.util.showSnackBar
import com.gyf.immersionbar.ktx.immersionBar

/**
 *
 * @Description: base activity
 * @Author: Jack
 * @CreateDate: 2022/5/14 20:53
 */
abstract class BaseActivity : AppCompatActivity() {


    private val mLoadingDialog: LoadingDialog by lazy {
        LoadingDialog()
    }

    lateinit var toolbar: Toolbar


    fun showLoadingDialog() {
        runCatching {
            if (!mLoadingDialog.isAdded) {
                mLoadingDialog.show(supportFragmentManager, "Loading")
            }
        }
    }

    fun dismissLoadingDialog() {
        runCatching {
            mLoadingDialog.dismiss()
        }
    }


    fun setUpToolbar(titleRes: Int? = null, showBack: Boolean = false) {
        toolbar = findViewById(R.id.toolbar) ?: return
        setSupportActionBar(toolbar)

        if (titleRes != null) {
            supportActionBar?.title = getString(titleRes)
        }

        if (showBack) {
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                toolbar.setNavigationOnClickListener {
                    finish()
                }
            }
        }

        initToolbar()
        initImmersionBar()
    }

    fun setToolbarTitle(titleRes: Int) {
        if (supportActionBar == null) {
            setUpToolbar(titleRes)
        } else {
            supportActionBar!!.title = getString(titleRes)
        }
    }

    fun setToolbarTitle(title: String) {
        supportActionBar?.let {
            it.title = title
        }

    }

    /**
     * 默认透明导航栏
     * 状态栏primary
     */
    open fun initImmersionBar() {
        immersionBar {
            transparentNavigationBar()
            navigationBarDarkIcon(true)
            statusBarColor(R.color.colorPrimary)
        }
    }

    open fun initToolbar() {

    }

    fun currentUserID(mIntent: Intent = intent): Int {
        return mIntent.getIntExtra(INTENT_USERID, 0)
    }

    fun hideKeyboard() {
        val inputManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputManager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    /**
     * 默认的加载弹窗事件
     */
    val boxActionStateObserver: Observer<BoxActionState> = Observer<BoxActionState>
    {
        when (it) {
            is BoxActionState.Loading -> {
                showLoadingDialog()
            }

            is BoxActionState.Success -> {
                dismissLoadingDialog()
                showSnackBar(it.msg)
            }

            is BoxActionState.Fail -> {
                dismissLoadingDialog()
                showSnackBar(it.msg)
            }
        }
    }

    companion object {
        const val INTENT_USERID = "userID"
    }
}
