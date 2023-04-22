package com.fvbox.data

import android.content.pm.PackageManager
import com.fvbox.R
import com.fvbox.app.config.BoxComponentConfig
import com.fvbox.data.bean.box.BoxComponentBean
import com.fvbox.data.bean.db.RuleBean
import com.fvbox.data.bean.db.RuleState
import com.fvbox.lib.FCore
import com.fvbox.lib.rule.common.ConfigRule
import com.fvbox.lib.rule.common.PackageRule


object RuleRepository {

    fun getComponentList(userID: Int, pkg: String, type: Int): List<BoxComponentBean> {

        val list = when (type) {
            RuleState.ACTIVITY -> getActivityList(userID, pkg, type)
            RuleState.SERVICE -> getServiceList(userID, pkg, type)
            RuleState.CONTENT_PROVIDER -> getProviderList(userID, pkg, type)
            RuleState.BROADCAST -> getBroadcastList(userID, pkg, type)
            RuleState.PROCESS -> getProcessList(userID, pkg, type)
            else -> null
        }

        return list?.sortedBy { it.name } ?: emptyList()
    }

    private fun getActivityList(
        userID: Int,
        pkg: String,
        type: Int
    ): List<BoxComponentBean>? {
        val packageInfo = FCore.get().getPackageInfo(pkg, PackageManager.GET_ACTIVITIES, userID)
        val blockComponentList = BoxComponentConfig.getBlockComponentList(pkg, userID, type)
        return packageInfo?.activities?.map {
            BoxComponentBean(it.name, blockComponentList.contains(it.name))
        }
    }

    private fun getServiceList(
        userID: Int,
        pkg: String,
        type: Int
    ): List<BoxComponentBean>? {
        val packageInfo = FCore.get().getPackageInfo(pkg, PackageManager.GET_SERVICES, userID)
        val blockComponentList = BoxComponentConfig.getBlockComponentList(pkg, userID, type)
        return packageInfo?.services?.map {
            BoxComponentBean(it.name, blockComponentList.contains(it.name))
        }
    }

    private fun getBroadcastList(
        userID: Int,
        pkg: String,
        type: Int
    ): List<BoxComponentBean>? {

        val packageInfo = FCore.get().getPackageInfo(pkg, PackageManager.GET_RECEIVERS, userID)
        val blockComponentList = BoxComponentConfig.getBlockComponentList(pkg, userID, type)
        return packageInfo?.receivers?.map {
            BoxComponentBean(it.name, blockComponentList.contains(it.name))
        }
    }

    private fun getProviderList(
        userID: Int,
        pkg: String,
        type: Int
    ): List<BoxComponentBean>? {
        val packageInfo = FCore.get().getPackageInfo(pkg, PackageManager.GET_PROVIDERS, userID)
        val blockComponentList = BoxComponentConfig.getBlockComponentList(pkg, userID, type)
        return packageInfo?.providers?.map {
            BoxComponentBean(it.name, blockComponentList.contains(it.name))
        }
    }

    private fun getProcessList(
        userID: Int,
        pkg: String,
        type: Int
    ): List<BoxComponentBean> {
        val packageInfo = FCore.get().getPackageInfo(
            pkg,
            PackageManager.GET_PROVIDERS or PackageManager.GET_SERVICES or PackageManager.GET_PROVIDERS or PackageManager.GET_RECEIVERS,
            userID
        )
        val processList = hashSetOf<String>()
        packageInfo?.activities?.map { it.processName }?.let { processList.addAll(it) }
        packageInfo?.services?.map { it.processName }?.let { processList.addAll(it) }
        packageInfo?.receivers?.map { it.processName }?.let { processList.addAll(it) }
        packageInfo?.providers?.map { it.processName }?.let { processList.addAll(it) }

        val blockComponentList = BoxComponentConfig.getBlockComponentList(pkg, userID, type)
        return processList.map {
            BoxComponentBean(it, blockComponentList.contains(it))
        }
    }

    fun changeComponentStatus(userID: Int, pkg: String, type: Int, name: String, status: Boolean) {
        if (status) {
            BoxComponentConfig.addBlockComponent(pkg, userID, name, type)
        } else {
            BoxComponentConfig.removeBlockComponent(pkg, userID, name, type)
        }

    }

    fun getConfigState(userID: Int, pkg: String): List<RuleBean> {
        return BoxComponentConfig.getStateRule(pkg, userID)
    }

    fun setConfigState(userID: Int, pkg: String, type: Int, status: Boolean) {
        if (status) {
            BoxComponentConfig.addStateRule(pkg, userID, type)
        } else {
            BoxComponentConfig.removeStateRule(pkg, userID, type)
        }

    }

}
