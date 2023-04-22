package com.fvbox.data

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import com.fvbox.data.bean.box.BoxAppBean
import com.fvbox.data.bean.box.BoxInstallBean
import com.fvbox.data.bean.box.BoxUserInfo
import com.fvbox.data.bean.local.LocalAppBean
import com.fvbox.lib.FCore
import com.fvbox.lib.utils.AbiUtils
import com.fvbox.util.ContextHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object BoxRepository {

    private val localAppList = mutableListOf<LocalAppBean>()


    /**
     * 初始化本地已安装应用列表
     */
    fun initLocalAppList() {
        GlobalScope.launch(Dispatchers.IO) {
            synchronized(localAppList) {
                val packageManager = ContextHolder.get().packageManager
                val installedApplications = packageManager.getInstalledApplications(0)

                val selfPkg = ContextHolder.get().packageName

                localAppList.clear()

                installedApplications.forEach {

                    if (it.packageName == selfPkg) {
                        return@forEach
                    }

                    if ((it.flags and ApplicationInfo.FLAG_SYSTEM) != 0) {
                        return@forEach
                    }

                    val apkFile = File(it.sourceDir)


                    val isSupport = AbiUtils.isSupport(apkFile)
                    val name = it.loadLabel(packageManager).toString()
                    val icon = it.loadIcon(packageManager)

                    localAppList.add(
                        LocalAppBean(
                            name,
                            it.packageName,
                            icon = icon,
                            isSupport = isSupport
                        )
                    )
                }


                localAppList.sortBy { it.name }
            }
        }
    }

    /**
     * 获取本地已安装应用列表
     * @return List<LocalAppBean>
     */
    fun getLocalAppList(): List<LocalAppBean> {
        synchronized(localAppList) {
            return localAppList
        }
    }

    /**
     * 获取包名对应的本地应用信息
     * @param pkgList List<String>
     * @return List<LocalAppBean>
     */
    fun getLocalAppFilterList(pkgList: List<String>): List<LocalAppBean> {
        return synchronized(localAppList) {
            localAppList.filter {
                it.pkg in pkgList
            }
        }
    }

    fun getLocalApkInfo(apk: File, uri: Uri): List<LocalAppBean> {
        try {
            val input = ContextHolder.get().contentResolver.openInputStream(uri)
            input?.copyTo(FileOutputStream(apk))
            val packageManager = ContextHolder.get().packageManager
            val info = packageManager.getPackageArchiveInfo(
                apk.absolutePath,
                PackageManager.GET_ACTIVITIES
            )!!
            val localAppBean = LocalAppBean(
                info.applicationInfo.loadLabel(packageManager).toString(),
                info.packageName,
                apk,
                info.applicationInfo.loadIcon(packageManager),
                AbiUtils.isSupport(apk)
            )
            return listOf(localAppBean)

        } catch (e: IOException) {
            return emptyList()
        }
    }

    fun install(path: File, pkg: String, userID: Int): BoxInstallBean {
        val fvCore = FCore.get()
        val installResult = fvCore.installPackageAsUser(path, userID)
        return BoxInstallBean(pkg, installResult.success, installResult.msg ?: "")
    }

    fun install(pkg: String, userID: Int): BoxInstallBean {
        val fvCore = FCore.get()
        val installResult = fvCore.installPackageAsUser(pkg, userID)
        return BoxInstallBean(pkg, installResult.success, installResult.msg ?: "")
    }

    /**
     * 卸载应用
     * @param pkg String
     * @param userID Int
     */
    fun uninstall(pkg: String, userID: Int) {
        FCore.get().uninstallPackageAsUser(pkg, userID)
        BoxDatabase.instance.ruleDao().removeAppRule(pkg, userID)
    }

    /**
     * 清除数据
     * @param pkg String
     * @param userID Int
     */
    fun clearData(pkg: String, userID: Int) {
        FCore.get().clearPackage(pkg, userID)
    }

    /**
     * 强行停止
     * @param pkg String
     * @param userID Int
     */
    fun forceStop(pkg: String, userID: Int) {
        FCore.get().stopPackage(pkg, userID)
    }

    /**
     * 获取用户
     * @return List<FUserInfo>
     */
    fun getUserList(): List<BoxUserInfo> {
        return FCore.get().users.map {
            BoxUserInfo(it.id)
        }
    }

    /**
     * 新建用户
     */
    fun createUser() {
        FCore.get().apply {
            val list = users
            val lastID = list.lastOrNull()?.id ?: -1
            createUser(lastID + 1)
        }
    }

    /**
     * 删除用户
     * @param userID Int 用户ID
     */
    fun deleteUser(userID: Int) {
        FCore.get().deleteUser(userID)
    }

    /**
     * 启动app
     * @param pkg String 包名
     * @param userID Int 用户ID
     */
    fun launchApp(pkg: String, userID: Int) {
        FCore.get().launchApk(pkg, userID)
    }

    /**
     * 获取box已安装应用列表
     */
    fun getBoxAppList(userID: Int): List<BoxAppBean> {
        val appList = mutableListOf<BoxAppBean>()
        FCore.get().apply {
            val list = getInstalledApplications(userID)
            for (app in list) {
                app.packageInfo ?: continue
                val applicationInfo = app.packageInfo!!.applicationInfo
                val name = applicationInfo.loadLabel(packageManager).toString()
                val icon = applicationInfo.loadIcon(packageManager)
                val bean = BoxAppBean(userID, applicationInfo.packageName, name, icon)
                appList.add(bean)
            }
        }
        return appList
    }

    fun stopAllPackage() {
        FCore.get().stopAllPackages()
    }

    fun restartCoreSystem(){
        FCore.get().restartCoreSystem()
    }

}
