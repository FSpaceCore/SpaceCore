package com.fvbox.lib

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.*
import androidx.annotation.Keep
import com.fvbox.lib.common.FMessageResult
import com.fvbox.lib.common.pm.InstallResult
import com.fvbox.lib.common.pm.InstalledPackage
import com.fvbox.lib.system.server.user.FUserInfo
import java.io.File

/**
 * Created by FvBox on 2022/4/25.
 */
@SuppressLint("StaticFieldLeak", "NewApi")
class FCore {
    lateinit var context: Context

    @Keep
    fun init(context: Context) {
        this.context = context
    }

    fun getPackageManager(): PackageManager {
        return context.packageManager
    }

    fun launchApk(packageName: String, userId: Int) {
    }

    fun clearPackage(packageName: String, userId: Int) {
    }

    fun stopPackage(packageName: String, userId: Int) {
    }

    fun stopAllPackages() {
    }

    fun getInstalledApplications(userId: Int): List<InstalledPackage> {
        return emptyList()
    }

    fun uninstallPackageAsUser(packageName: String, userId: Int) {
    }

    fun installPackageAsUser(packageName: String, userId: Int): InstallResult {
        return InstallResult().installError("null")
    }

    fun installPackageAsUser(file: File, userId: Int): InstallResult {
        return InstallResult().installError("null")
    }

    fun getUsers(): List<FUserInfo> {
        return emptyList()
    }

    fun createUser(userId: Int): FUserInfo? {
        return null
    }

    fun deleteUser(userId: Int) {
    }

    fun exportAppData(userId: Int, packageName: String, zipFile: File): FMessageResult {
        return FMessageResult.buildError("null")
    }

    fun importAppData(userId: Int, zipFile: File): FMessageResult {
        return FMessageResult.buildError("null")
    }

    fun isRunning(packageName: String, userId: Int): Boolean {
        return false
    }

    private fun getProcessName(context: Context): String {
        val ams = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        ams.runningAppProcesses.forEach {
            if (it.pid == Process.myPid())
                return it.processName
        }
        return ""
    }

    companion object {
        private const val TAG = "FCore"
        private val S_F_CORE: FCore = FCore()

        @JvmStatic
        @Keep
        fun get(): FCore {
            return S_F_CORE
        }
    }
}
