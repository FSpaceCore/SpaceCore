package com.fvbox.app.ui.box

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fvbox.R
import com.fvbox.databinding.ItemBoxAppsMenuBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

/**
 *
 * @Description: 长按菜单
 * @Author: Jack
 * @CreateDate: 2022/5/22 3:03
 */
class BoxAppMenuItem : AbstractBindingItem<ItemBoxAppsMenuBinding>() {

    override val type: Int
        get() = R.id.icon

    var imageRes: Int? = null

    var textRes: Int? = null


    fun withImage(id: Int): BoxAppMenuItem {
        this.imageRes = id
        return this
    }

    fun withText(id: Int): BoxAppMenuItem {
        this.textRes = id
        return this
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemBoxAppsMenuBinding {
        return ItemBoxAppsMenuBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemBoxAppsMenuBinding, payloads: List<Any>) {
        imageRes?.let { binding.icon.setImageResource(it) }
        textRes?.let { binding.title.setText(it) }
    }


}