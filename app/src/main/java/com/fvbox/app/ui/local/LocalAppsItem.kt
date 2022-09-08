package com.fvbox.app.ui.local

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fvbox.R
import com.fvbox.data.bean.local.LocalAppBean
import com.fvbox.databinding.ItemLocalAppsBinding
import com.fvbox.util.ProcessUtil
import com.fvbox.util.extension.isShow
import com.mikepenz.fastadapter.binding.AbstractBindingItem

/**
 *
 * @Description: item
 * @Author: Jack
 * @CreateDate: 2022/5/16 23:07
 */
class LocalAppsItem : AbstractBindingItem<ItemLocalAppsBinding>() {


    var appBean: LocalAppBean? = null

    override val type: Int
        get() = R.id.appName

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemLocalAppsBinding {
        return ItemLocalAppsBinding.inflate(inflater, parent, false)
    }


    fun withBean(appBean: LocalAppBean): LocalAppsItem {
        this.appBean = appBean
        return this
    }

    override fun bindView(binding: ItemLocalAppsBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        appBean?.let {
            binding.appName.text = it.name
            binding.pkg.text = it.pkg
            binding.icon.setImageDrawable(it.icon)

            binding.cornerView.isShow(!it.isSupport)
            binding.cornerView.setText(ProcessUtil.noSupportBit().toString())

        }

    }

}
