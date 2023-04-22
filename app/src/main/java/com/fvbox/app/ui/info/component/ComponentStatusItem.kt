package com.fvbox.app.ui.info.component

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fvbox.R
import com.fvbox.data.bean.box.BoxComponentBean
import com.fvbox.databinding.ItemComponentStatusBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem


class ComponentStatusItem: AbstractBindingItem<ItemComponentStatusBinding>() {
    override val type: Int
        get() = R.id.enable

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemComponentStatusBinding {
        return ItemComponentStatusBinding.inflate(inflater,parent,false)
    }

    var bean:BoxComponentBean? = null

    fun withBean(bean: BoxComponentBean):ComponentStatusItem{
        this.bean = bean
        return this
    }

    override fun bindView(binding: ItemComponentStatusBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        bean?.let {
            binding.enable.isChecked = !it.status
            binding.tvName.text = it.name
        }
    }
}
