package com.fvbox.app.ui.install.select

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fvbox.R
import com.fvbox.data.bean.box.BoxUserInfo
import com.fvbox.databinding.ItemInstallUserCheckBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem


class UserSelectItem : AbstractBindingItem<ItemInstallUserCheckBinding>() {

    override val type: Int
        get() = R.id.userName

    private var userInfo: BoxUserInfo? = null

    fun withUser(userInfo: BoxUserInfo): UserSelectItem {
        this.userInfo = userInfo
        return this
    }

    fun getUser(): BoxUserInfo? {
        return userInfo
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemInstallUserCheckBinding {
        return ItemInstallUserCheckBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemInstallUserCheckBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        userInfo?.let {
            binding.root.isSelected = isSelected
            binding.userName.text = it.userName
        }

    }
}
