package com.fvbox.app.ui.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fvbox.R
import com.fvbox.app.base.BaseViewModel
import com.fvbox.app.config.BoxConfig
import com.fvbox.data.BackRepository
import com.fvbox.data.BoxRepository
import com.fvbox.data.bean.box.BoxUserInfo
import com.fvbox.data.state.BoxActionState
import com.fvbox.util.extension.getString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MainViewModel : BaseViewModel() {

    /**
     * 用户切换
     */
    private val mUserChangeLiveData = MutableLiveData<BoxUserInfo>()
    val userChangeLiveData: LiveData<BoxUserInfo> = mUserChangeLiveData


    private val userList: MutableList<BoxUserInfo?> = mutableListOf()

    init {
        getUserList()
    }

    fun changeUser(userID: Int) {
        launchIO {
            userList.clear()
            userList.addAll(BoxRepository.getUserList())
            var user: BoxUserInfo? = userList.findLast {
                it?.userID == userID
            }

            if (user == null) {
                user = userList.firstOrNull()
            }

            if (user == null) {
                return@launchIO
            }

            user.let {
                mUserChangeLiveData.postValue(it)
            }
        }


    }

    /**
     * 没用户就创建
     * 有用户就post通知fragment
     */
    fun getUserList() {
        launchIO {
            userList.clear()
            userList.addAll(BoxRepository.getUserList())

            if (userList.isEmpty()) {
                BoxRepository.createUser()
                getUserList()
            } else {
                changeUser(BoxConfig.currentUserID)
            }
        }
    }

    /**
     * 清除所有后台，释放内存
     */
    fun stopAll() {
        BoxRepository.stopAllPackage()
    }


    /**
     * 导入数据
     * @param userID Int
     * @param uri Uri
     */
    fun importData(userID: Int, uri: Uri) {
        launch {
            mBoxActionState.postValue(BoxActionState.Loading)
            val msg = withContext(Dispatchers.IO) {
                BackRepository.importData(userID, uri)
            }
            if (msg.isNullOrEmpty()) {
                mBoxActionState.postValue(BoxActionState.Success(getString(R.string.import_success)))
            } else {
                mBoxActionState.postValue(BoxActionState.Fail(msg))
            }
        }
    }

}
