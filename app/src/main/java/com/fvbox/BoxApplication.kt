package com.fvbox

import android.app.Application
import android.content.Context
import android.util.Log
import com.fvbox.app.config.BoxConfigLoader
import com.fvbox.data.BoxRepository
import com.fvbox.lib.FCore
import com.fvbox.lib.abs.ApplicationCallback
import com.fvbox.util.ContextHolder

class BoxApplication : Application() {
    companion object {
        private const val TAG = "BoxApplication"
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        FCore.get().init(this, true)
        ContextHolder.init(base)
        if (FCore.isHost()) {
            BoxRepository.initLocalAppList()
        } else {
            BoxConfigLoader.loadRule()
        }
        FCore.get().applicationCallback = object : ApplicationCallback {
            override fun beforeCreateApplication(packageName: String, processName: String, userId: Int) {
                Log.d(TAG, "beforeCreateApplication: packageName=$packageName, processName=$processName, userId=$userId")
            }

            override fun afterCreatedApplication(
                application: Application,
                packageName: String,
                processName: String,
                userId: Int
            ) {
                Log.d(
                    TAG,
                    "afterCreatedApplication: application=$application, packageName=$packageName, processName=$processName, userId=$userId"
                )
            }
        }
    }
}
