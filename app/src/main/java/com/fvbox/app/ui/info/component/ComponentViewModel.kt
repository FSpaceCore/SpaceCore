package com.fvbox.app.ui.info.component

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fvbox.app.base.BaseViewModel
import com.fvbox.data.BoxRepository
import com.fvbox.data.RuleRepository
import com.fvbox.data.state.BoxActionState
import com.fvbox.data.state.BoxComponentState
import com.fvbox.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ComponentViewModel : BaseViewModel() {

    private val componentLiveData = MutableLiveData<BoxComponentState>()
    val componentState: LiveData<BoxComponentState> = componentLiveData

    fun loadComponentList(userID: Int, pkg: String, type: Int) {
        componentLiveData.postValue(BoxComponentState.Loading)
        launch {
            val list = withContext(Dispatchers.IO) {
                RuleRepository.getComponentList(userID, pkg, type)
            }
            componentLiveData.postValue(BoxComponentState.Success(list))
        }
    }

    fun changeComponentStatus(userID: Int, pkg: String, type: Int, name: String, status: Boolean) {
        mBoxActionState.postValue(BoxActionState.Loading)
        launch {
            withContext(Dispatchers.IO){
                 RuleRepository.changeComponentStatus(userID, pkg,type,name, status)
            }
            mBoxActionState.postValue(BoxActionState.Success())
        }
    }

    fun restartSystemProcess(){
        launch {
            BoxRepository.restartCoreSystem()
        }
    }
}
