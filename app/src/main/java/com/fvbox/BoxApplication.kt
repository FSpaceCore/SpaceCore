package com.fvbox

import android.app.Application
import android.content.Context
import com.fvbox.data.BoxRepository
import com.fvbox.lib.FCore
import com.fvbox.util.ContextHolder

/**
 * Created by FvBox on 2022/4/26.
 */
class BoxApplication : Application() {
    companion object {
        private const val TAG = "BoxApplication"
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        FCore.get().init(this)
        ContextHolder.init(base)
        BoxRepository.initLocalAppList()
    }
}
