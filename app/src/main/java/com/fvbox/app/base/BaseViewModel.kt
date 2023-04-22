package com.fvbox.app.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fvbox.data.state.BoxActionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


open class BaseViewModel : ViewModel() {

    protected val mBoxActionState = MutableLiveData<BoxActionState>()
    val boxActionState: LiveData<BoxActionState> = mBoxActionState

    fun launchIO(block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            block()
        }
    }

    fun launch(block: suspend () -> Unit) {
        viewModelScope.launch {
            block()
        }
    }
}
