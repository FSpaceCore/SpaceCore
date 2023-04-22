package com.fvbox.app.ui.info.feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fvbox.app.base.BaseViewModel
import com.fvbox.data.BoxRepository
import com.fvbox.data.RuleRepository
import com.fvbox.data.state.BoxConfigRuleState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class FeatureViewModel : BaseViewModel() {


    private val configLiveData = MutableLiveData<BoxConfigRuleState>()
    val configState: LiveData<BoxConfigRuleState> = configLiveData

    fun loadConfig(userID: Int, pkg: String) {
        configLiveData.postValue(BoxConfigRuleState.Loading)
        launch {
            val config = withContext(Dispatchers.IO) {
                RuleRepository.getConfigState(userID, pkg)
            }
            configLiveData.postValue(BoxConfigRuleState.Success(config))
        }
    }

    fun changeConfig(userID: Int, pkg: String, type: Int, state: Boolean) {
        launch {
            withContext(Dispatchers.IO){
                RuleRepository.setConfigState(userID, pkg, type, state)
                configLiveData.postValue(BoxConfigRuleState.Updated)
            }
        }
    }

    fun restartSystemProcess(){
        launch {
            BoxRepository.restartCoreSystem()
        }
    }
}
