package com.fvbox.app.ui.local

import androidx.lifecycle.MutableLiveData
import com.fvbox.app.base.BaseViewModel
import com.fvbox.data.BoxRepository
import com.fvbox.data.state.LocalAppState

/**
 *
 * @Description: app list viewModel
 * @Author: Jack
 * @CreateDate: 2022/5/16 22:04
 */
class LocalAppsViewModel : BaseViewModel() {

    private val localAppLiveData = MutableLiveData<LocalAppState>()

    fun getLocalAppList(): MutableLiveData<LocalAppState> {

        launchIO {
            localAppLiveData.postValue(LocalAppState.Loading)

            val appList = BoxRepository.getLocalAppList()

            if (appList.isEmpty()) {
                localAppLiveData.postValue(LocalAppState.Empty)
            } else {
                localAppLiveData.postValue(LocalAppState.Success(appList))
            }

        }

        return localAppLiveData
    }

}
