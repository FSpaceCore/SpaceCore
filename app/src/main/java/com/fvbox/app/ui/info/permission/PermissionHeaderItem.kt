package com.fvbox.app.ui.info.permission

import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.fvbox.R
import com.fvbox.databinding.ItemPermissionHeaderBinding
import com.fvbox.util.extension.getString
import com.mikepenz.fastadapter.binding.AbstractBindingItem


class PermissionHeaderItem : AbstractBindingItem<ItemPermissionHeaderBinding>() {

    override val type: Int
        get() = R.id.tvName


    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemPermissionHeaderBinding {
        return ItemPermissionHeaderBinding.inflate(inflater, parent, false)
    }

    var name: String? = null

    fun withName(status: Int): PermissionHeaderItem {
        val name = when(status){
            PackageManager.PERMISSION_GRANTED -> R.string.permission_granted
            PackageManager.PERMISSION_DENIED -> R.string.permission_denied
            else -> R.string.permission_need_request
        }
        this.name = getString(name)
        this.identifier = name.hashCode().toLong()
        return this
    }

    override fun bindView(binding: ItemPermissionHeaderBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.tvName.text = name
    }

}
