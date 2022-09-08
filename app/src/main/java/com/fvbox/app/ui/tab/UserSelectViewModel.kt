package com.fvbox.app.ui.tab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fvbox.R
import com.fvbox.app.base.BaseViewModel
import com.fvbox.app.config.BoxConfig
import com.fvbox.data.BoxRepository
import com.fvbox.data.bean.box.BoxUserInfo
import com.fvbox.data.state.BoxActionState
import com.fvbox.util.CaptureUtil
import com.fvbox.util.extension.getString

/**
 *
 * @Description: tab选择 （很简单的一个界面，有没有用mvvm的必要）
 * @Author: Jack
 * @CreateDate: 2022/5/21 2:28
 */
class UserSelectViewModel : BaseViewModel() {

    private val mUserLiveData = MutableLiveData<List<BoxUserInfo>>()
    val userLiveData: LiveData<List<BoxUserInfo>> = mUserLiveData
    //无需加状态

    init {
        loadUserList()
    }

    fun loadUserList() {
        launch {
            val list = BoxRepository.getUserList()
            mUserLiveData.postValue(list)
        }
    }

    fun addUser() {
        launchIO {
            BoxRepository.createUser()
            loadUserList()
        }
    }

    fun deleteUser(userID: Int) {
        launchIO {
            mBoxActionState.postValue(BoxActionState.Loading)
            BoxRepository.deleteUser(userID)
            BoxConfig.deleteUserRemark(userID)
            CaptureUtil.deleteCapture(userID)
            loadUserList()
            mBoxActionState.postValue(BoxActionState.Success(getString(R.string.delete_success)))
        }
    }


}
