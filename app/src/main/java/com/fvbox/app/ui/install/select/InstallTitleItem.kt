package com.fvbox.app.ui.install.select

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fvbox.R
import com.fvbox.databinding.ItemTitleBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

/**
 *
 * @Description: 标题item
 * @Author: Jack
 * @CreateDate: 2022/5/23 23:43
 */
class InstallTitleItem : AbstractBindingItem<ItemTitleBinding>() {

    override val type: Int
        get() = R.id.title

    var title: Any? = null

    fun title(title: Any?): InstallTitleItem {
        this.title = title
        return this
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemTitleBinding {
        return ItemTitleBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemTitleBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.title.text = title.toString()
    }
}