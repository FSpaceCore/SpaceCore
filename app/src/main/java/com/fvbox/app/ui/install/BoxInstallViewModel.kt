package com.fvbox.app.ui.install

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fvbox.app.base.BaseViewModel
import com.fvbox.data.BoxRepository
import com.fvbox.data.bean.box.BoxUserInfo
import com.fvbox.data.bean.local.LocalAppBean
import com.fvbox.data.state.BoxActionState
import com.fvbox.data.state.BoxInstallProgressState
import com.fvbox.data.state.BoxInstallUserState
import com.fvbox.util.ContextHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


class BoxInstallViewModel : BaseViewModel() {

    private val mInstallUserState = MutableLiveData<BoxInstallUserState>()
    val installUserState: LiveData<BoxInstallUserState> = mInstallUserState

    private val mInstallProgress = MutableLiveData<List<BoxInstallProgressState>>()
    val installProgress: LiveData<List<BoxInstallProgressState>> = mInstallProgress

    private var mAppList: ArrayList<LocalAppBean> = arrayListOf()

    private var tmpApkPath: File? = null

    fun loadData(uri: Uri) {
        launch {
            mInstallUserState.postValue(BoxInstallUserState.Loading)

            tmpApkPath = File(ContextHolder.get().externalCacheDir, System.nanoTime().toString())

            val appList = withContext(Dispatchers.IO) {
                BoxRepository.getLocalApkInfo(tmpApkPath!!, uri)
            }

            val userList = withContext(Dispatchers.IO) {
                BoxRepository.getUserList()
            }

            if (appList.isEmpty() || userList.isEmpty()) {
                mInstallUserState.postValue(BoxInstallUserState.Empty)
                return@launch
            }

            mAppList.clear()
            mAppList.addAll(appList)

            mInstallUserState.postValue(BoxInstallUserState.LoadSuccess(appList, userList))
        }
    }

    fun loadData(pkgList: List<String>?) {
        if (pkgList.isNullOrEmpty()) {
            mInstallUserState.value = BoxInstallUserState.Empty
            return
        }

        launch {
            mInstallUserState.postValue(BoxInstallUserState.Loading)

            val appList = withContext(Dispatchers.IO) {
                BoxRepository.getLocalAppFilterList(pkgList)
            }

            val userList = withContext(Dispatchers.IO) {
                BoxRepository.getUserList()
            }

            if (appList.isEmpty() || userList.isEmpty()) {
                mInstallUserState.postValue(BoxInstallUserState.Empty)
                return@launch
            }

            mAppList.clear()
            mAppList.addAll(appList)

            mInstallUserState.postValue(BoxInstallUserState.LoadSuccess(appList, userList))
        }
    }

    fun startInstall(selectUserList: List<BoxUserInfo>) {

        launchIO {

            val resultList = arrayListOf<BoxInstallProgressState>()

            mAppList.forEach { app ->

                resultList.add(BoxInstallProgressState.Header(app))
                mInstallProgress.postValue(resultList)

                selectUserList.forEach { user ->

                    val index = resultList.size
                    resultList.add(
                        index,
                        BoxInstallProgressState.Loading(app.pkg, user.userName)
                    )
                    mInstallProgress.postValue(resultList)

                    val installResult = if (app.path == null) {
                        BoxRepository.install(app.pkg, user.userID)
                    } else {
                        BoxRepository.install(app.path!!, app.pkg, user.userID)
                    }

                    resultList[index] =
                        BoxInstallProgressState.Finish(user.userName, installResult)
                    mInstallProgress.postValue(resultList)
                }
            }
            mBoxActionState.postValue(BoxActionState.Success())
        }
    }


    override fun onCleared() {
        super.onCleared()
        tmpApkPath?.delete()
    }
}
