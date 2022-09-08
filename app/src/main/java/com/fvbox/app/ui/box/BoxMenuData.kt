package com.fvbox.app.ui.box

import com.fvbox.R

/**
 *
 * @Description: 长按菜单内容
 * @Author: Jack
 * @CreateDate: 2022/5/22 3:11
 */
object BoxMenuData {

    fun getMenuList(): List<BoxAppMenuItem> {
        return listOf(
            BoxAppMenuItem().withImage(R.drawable.ic_clearing).withText(R.string.clear_data),
            BoxAppMenuItem().withImage(R.drawable.ic_stop_dark).withText(R.string.force_stop),
            BoxAppMenuItem().withImage(R.drawable.ic_cloud_upload_dark).withText(R.string.export_data),
            BoxAppMenuItem().withImage(R.drawable.ic_shortcut).withText(R.string.app_shortcut),
            BoxAppMenuItem().withImage(R.drawable.ic_delete_dark).withText(R.string.uninstall_app),
        )
    }
}
