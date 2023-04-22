package com.fvbox.app.ui.info.permission

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fvbox.R
import com.fvbox.data.bean.box.BoxPermissionBean
import com.fvbox.databinding.ItemPermissionManagerBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem


class PermissionItem: AbstractBindingItem<ItemPermissionManagerBinding>() {
    override val type: Int
        get() = R.id.tvPermission

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemPermissionManagerBinding {
        return ItemPermissionManagerBinding.inflate(inflater,parent,false)
    }

    var bean:BoxPermissionBean? = null

    fun withBean(bean: BoxPermissionBean): PermissionItem {
        this.bean = bean
        this.identifier = bean.permission.hashCode().toLong()
        return this
    }

    override fun bindView(binding: ItemPermissionManagerBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        bean?.let {
            binding.tvName.text = it.name
            binding.tvPermission.text = it.permission
            binding.icon.setImageResource(it.drawable)
        }

    }
}
