package com.fvbox.app.ui.setting.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fvbox.R
import com.fvbox.databinding.ItemSettingSearchBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem


class SettingSearchItem : AbstractBindingItem<ItemSettingSearchBinding>() {
    override val type: Int
        get() = R.id.radio

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemSettingSearchBinding {
        return ItemSettingSearchBinding.inflate(inflater, parent, false)
    }

    var text: String = ""

    fun withText(text: String): SettingSearchItem {
        this.text = text
        return this
    }

    override fun bindView(binding: ItemSettingSearchBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.radio.isChecked = isSelected
        binding.text.text = text
    }
}
