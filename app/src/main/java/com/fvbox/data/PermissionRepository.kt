package com.fvbox.data

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import com.fvbox.R
import com.fvbox.data.bean.box.BoxAppBean
import com.fvbox.data.bean.box.BoxPermissionBean
import com.fvbox.data.mapping.permissionMapOnQ
import com.fvbox.data.mapping.permissionMapOnR
import com.fvbox.data.mapping.permissionMapOnS
import com.fvbox.data.mapping.permissionMapOnT
import com.fvbox.lib.FCore
import com.fvbox.lib.common.FPermission
import com.fvbox.lib.utils.compat.BuildCompat
import com.fvbox.util.ContextHolder

import com.fvbox.util.Log
object PermissionRepository {


    fun getBoxAppPermissionList(
        userID: Int,
        pkg: String
    ): Pair<BoxAppBean, List<BoxPermissionBean>>? {
        val packageInfo = FCore.get().getPackageInfo(pkg, PackageManager.GET_PERMISSIONS, userID)
        val list = arrayListOf<BoxPermissionBean>()
        val packageManager = FCore.get().packageManager

        if (packageInfo?.applicationInfo == null||packageInfo.requestedPermissions.isNullOrEmpty()) {
            return null
        }

        packageInfo.requestedPermissions.map {
            return@map try {
                packageManager.getPermissionInfo(it, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        }.filter {
            it != null && it.protectionLevel == PermissionInfo.PROTECTION_DANGEROUS
        }.sortedBy {
            it?.group
        }.forEach {
            val name = it!!.loadLabel(packageManager).toString()
            val fPermission = FCore.get().getPermission(userID, pkg)
            val drawable = getPermGroupIcon(ContextHolder.get(), it.name, it.group.toString())
            list.add(
                BoxPermissionBean(
                    name,
                    it.name,
                    drawable,
                    fPermission?.getPermission(it.name) ?: 0
                )
            )
        }
        val appBean =
            BoxAppBean(
                userID,
                pkg,
                packageInfo.applicationInfo.loadLabel(packageManager).toString(),
                packageInfo.applicationInfo.loadIcon(packageManager)
            )
        return appBean to list
    }

    private fun getPermGroupIcon(
        context: Context,
        permission: String,
        defaultGroupName: String
    ): Int {
        val groupName = when {
            BuildCompat.isT() -> permissionMapOnT[permission]
            BuildCompat.isS() -> permissionMapOnS[permission]
            BuildCompat.isR() -> permissionMapOnR[permission]
            BuildCompat.isQ() -> permissionMapOnQ[permission]
            else -> defaultGroupName
        }
        runCatching {
            val groupInfo = context.packageManager.getPermissionGroupInfo(groupName.toString(), 0)
            var icon: Int = groupInfo.icon

            if (icon == 0) {
                icon = R.drawable.ic_permission_setting
            }
            return icon
        }
        return R.drawable.ic_permission_setting
    }


    fun changePermission(userID: Int, pkg: String, permission: String, action: Int) {
        val permissionInfo = FCore.get().getPermission(userID, pkg) ?: FPermission()

        when (action) {
            R.id.permission_grant -> {
                permissionInfo.grantedPermission(permission)
            }
            R.id.permission_deny -> {
                permissionInfo.deniedPermission(permission)
            }
            R.id.permission_host -> {
                permissionInfo.revokePermission(permission)
            }
        }
        FCore.get().updatePermission(userID, pkg, permissionInfo)
    }
}
