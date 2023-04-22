package com.fvbox.app.ui.setting

import androidx.annotation.StringRes
import kotlin.reflect.KMutableProperty0


sealed interface BaseBoxPreference

/**
 * 分组，标题
 */
data class TitleBoxPreference(@StringRes val title: Int) : BaseBoxPreference

data class SwitchBoxPreference(
    @StringRes val title: Int,
    @StringRes val subTitle: Int,
    val valueDelegate: KMutableProperty0<Boolean>
) : BaseBoxPreference

data class EditBoxPreference(
    @StringRes val title: Int,
    val valueDelegate: KMutableProperty0<String>
) : BaseBoxPreference

data class ClickPreference(
    @StringRes val title: Int,
    @StringRes val subTitle: Int = 0,
    val click: () -> Unit
) : BaseBoxPreference

data class SingleChoicePreference(
    @StringRes val title: Int,
    val list: List<String>,
    val valueDelegate: KMutableProperty0<String>
) : BaseBoxPreference

//支持参数对state
data class StateChangePreference(
    @StringRes val title: Int,
    @StringRes val subTitle: Int,
    val type:Int,
    val state: State<Int, Boolean>,
) : BaseBoxPreference


interface State<P, V> {
    fun get(type: P): V

    fun set(type: P, value: V)
}
