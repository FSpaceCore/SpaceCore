package com.fvbox.app.ui.box

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fvbox.R
import com.fvbox.data.bean.box.BoxAppBean
import com.fvbox.databinding.ItemBoxAppsBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem



class BoxAppsItem : AbstractBindingItem<ItemBoxAppsBinding>() {

    var appBean: BoxAppBean? = null


    override val type: Int
        get() = R.id.app_name

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemBoxAppsBinding {
        return ItemBoxAppsBinding.inflate(inflater, parent, false)
    }

    fun withBean(appBean: BoxAppBean): BoxAppsItem {
        this.appBean = appBean
        return this
    }

    override fun bindView(binding: ItemBoxAppsBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        appBean?.let {
            binding.appName.text = it.name
            binding.icon.setImageDrawable(it.icon)
        }

    }


}
