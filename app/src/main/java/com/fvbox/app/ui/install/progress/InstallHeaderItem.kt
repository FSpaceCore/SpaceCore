package com.fvbox.app.ui.install.progress

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fvbox.R
import com.fvbox.data.bean.local.LocalAppBean
import com.fvbox.databinding.ItemInstallProgressHeaderBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

/**
 *
 * @Description: header
 * @Author: Jack
 * @CreateDate: 2022/5/28 1:00
 */
class InstallHeaderItem : AbstractBindingItem<ItemInstallProgressHeaderBinding>() {

    override val type: Int
        get() = R.id.icon

    private var appBean: LocalAppBean? = null

    fun withApp(appBean: LocalAppBean): InstallHeaderItem {
        this.appBean = appBean
        return this
    }

    fun withIdentifier(id:Int):InstallHeaderItem{
        this.identifier = id.toLong()
        return this
    }

    fun getAppName(): String {
        return appBean?.name.toString()
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemInstallProgressHeaderBinding {
        return ItemInstallProgressHeaderBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemInstallProgressHeaderBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        appBean?.let {
            binding.icon.setImageDrawable(it.icon)
            binding.appName.text = it.name
        }
    }

    override fun toString(): String {
        return "type:${this.javaClass}\n${appBean.toString()}"
    }
}
