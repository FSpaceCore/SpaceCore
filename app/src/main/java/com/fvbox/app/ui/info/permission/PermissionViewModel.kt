package com.fvbox.app.ui.info.permission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fvbox.app.base.BaseViewModel
import com.fvbox.data.PermissionRepository
import com.fvbox.data.state.BoxPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PermissionViewModel : BaseViewModel() {

    private val mBoxAppLiveData = MutableLiveData<BoxPermissionState>()
    val boxAppState: LiveData<BoxPermissionState> = mBoxAppLiveData

    fun loadPermission(userID: Int, pkg: String) {
        launch {
            mBoxAppLiveData.postValue(BoxPermissionState.Loading)

            val pair = withContext(Dispatchers.IO) {
                PermissionRepository.getBoxAppPermissionList(userID, pkg)
            }
            if (pair != null) {
                mBoxAppLiveData.postValue(BoxPermissionState.Success(pair.first, pair.second))
            }
        }
    }

    fun changePermission(userID: Int, pkg: String, permission: String, action: Int) {
        launch {
            PermissionRepository.changePermission(userID, pkg, permission, action)
            loadPermission(userID, pkg)
        }
    }
}
