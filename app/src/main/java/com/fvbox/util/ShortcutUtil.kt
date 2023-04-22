package com.fvbox.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.fvbox.R
import com.fvbox.app.config.BoxConfig
import com.fvbox.app.ui.main.ShortcutActivity
import com.fvbox.data.bean.box.BoxAppBean
import com.fvbox.util.extension.getString

import com.fvbox.util.Log
object ShortcutUtil {

    /**
     * 创建桌面快捷方式
     * @param info BoxAppBean
     */
    @SuppressLint("CheckResult")
    fun createShortcut(context: Context, info: BoxAppBean) {

        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            val labelName = info.name + info.userID

            val intent = Intent(context, ShortcutActivity::class.java)
                .setAction(Intent.ACTION_MAIN)
                .putExtra("pkg", info.pkg)
                .putExtra("userId", info.userID)

            MaterialDialog(context).show {
                title(res = R.string.app_shortcut)
                input(
                    hintRes = R.string.shortcut_name,
                    prefill = labelName,
                    allowEmpty = false
                ) { _, input ->

                    val shortcutInfo: ShortcutInfoCompat =
                        ShortcutInfoCompat.Builder(context, info.pkg + info.userID)
                            .setIntent(intent)
                            .setShortLabel(input.toString())
                            .setLongLabel(input.toString())
                            .setIcon(IconCompat.createWithBitmap(info.icon.toBitmap()))
                            .build()

                    ShortcutManagerCompat.requestPinShortcut(context, shortcutInfo, null)
                    showAllowPermissionDialog(context)
                }
                positiveButton(R.string.done)
                negativeButton(R.string.cancel)
            }

        } else {
            showToast(R.string.cannot_create_shortcut)
        }
    }

    private fun showAllowPermissionDialog(context: Context) {
        if (!BoxConfig.showShortcutPermissionDialog) {
            return
        }

        MaterialDialog(context).show {
            title(R.string.try_add_shortcut)
            message(text = getString(R.string.add_shortcut_fail_msg, getString(R.string.app_name)))
            positiveButton(R.string.done)
            negativeButton(R.string.permission_setting) {
                openAppSystemSettings()
            }

            neutralButton(R.string.no_reminders) {
                BoxConfig.showShortcutPermissionDialog = false
            }
        }

    }

    private fun openAppSystemSettings() {
        ContextHolder.get().startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.fromParts("package", ContextHolder.get().packageName, null)
        })
    }

}
