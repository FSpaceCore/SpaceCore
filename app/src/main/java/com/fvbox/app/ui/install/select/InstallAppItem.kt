package com.fvbox.app.ui.install.select

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fvbox.R
import com.fvbox.data.bean.local.LocalAppBean
import com.fvbox.databinding.ItemInstallAppBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

/**
 *
 * @Description: 安装界面的app显示
 * @Author: Jack
 * @CreateDate: 2022/5/23 23:09
 */
class InstallAppItem : AbstractBindingItem<ItemInstallAppBinding>() {

    var appBean: LocalAppBean? = null

    override val type: Int
        get() = R.id.appName

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemInstallAppBinding {
        return ItemInstallAppBinding.inflate(inflater, parent, false)
    }


    fun withBean(appBean: LocalAppBean): InstallAppItem {
        this.appBean = appBean
        return this
    }

    override fun bindView(binding: ItemInstallAppBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        appBean?.let {
            binding.appName.text = it.name
            binding.icon.setImageDrawable(it.icon)
        }

    }
}