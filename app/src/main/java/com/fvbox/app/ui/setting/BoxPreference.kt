package com.fvbox.app.ui.setting

import androidx.annotation.StringRes
import kotlin.reflect.KMutableProperty0

/**
 *
 * @Description: 设置类型
 * @Author: Jack
 * @CreateDate: 2022/5/31 22:54
 */
sealed interface BaseBoxPreference

data class TitleBoxPreference(@StringRes val title: Int) : BaseBoxPreference

data class SwitchBoxPreference(
    @StringRes val title: Int,
    @StringRes val subTitle: Int,
    val valueDelegate: KMutableProperty0<Boolean>
) :
    BaseBoxPreference
//我也不知道这个KMutableProperty0是啥，但是应该变量代理之后变成的类型

data class EditBoxPreference(
    @StringRes val title: Int,
    val valueDelegate: KMutableProperty0<String>
) : BaseBoxPreference

data class ClickPreference(@StringRes val title: Int, val click: () -> Unit) : BaseBoxPreference

data class SingleChoicePreference(
    @StringRes val title: Int,
    val list: List<String>,
    val valueDelegate: KMutableProperty0<String>
) : BaseBoxPreference
