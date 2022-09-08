package com.fvbox.app.ui.install.progress

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fvbox.R
import com.fvbox.data.bean.box.BoxInstallBean
import com.fvbox.databinding.ItemInstallProgressItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

/**
 *
 * @Description: 具体到用户的安装进度
 * @Author: Jack
 * @CreateDate: 2022/5/28 1:01
 */
class InstallItemItem : AbstractBindingItem<ItemInstallProgressItemBinding>() {

    override val type: Int
        get() = R.id.appName

    private var installBean: BoxInstallBean? = null

    private var userName: String = ""

    private var pkg: String = ""

    fun withUserName(name: String): InstallItemItem {
        this.userName = name
        return this
    }

    fun withPkg(pkg: String): InstallItemItem {
        this.pkg = pkg
        return this
    }

    fun withInstallBean(installBean: BoxInstallBean): InstallItemItem {
        this.installBean = installBean
        return withPkg(installBean.pkg)
    }

    fun withIdentifier(id:Int):InstallItemItem{
        this.identifier = id.toLong()
        return this
    }

    fun getInstallBean(): BoxInstallBean? {
        return installBean
    }

    /**
     * 用来给diffUtil判断是否是同一个item
     * @return String
     */
    fun getItemTag(): String {
        return pkg + userName
    }

    fun getItemState(): Int {

        return when {
            installBean == null -> 1
            installBean?.success == true -> 2
            else -> 3
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemInstallProgressItemBinding {
        return ItemInstallProgressItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemInstallProgressItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.userName.text = userName
        if (installBean == null) {
            binding.stateImage.visibility = View.GONE
            binding.progress.visibility = View.VISIBLE
        } else {

            installBean?.let {
                binding.progress.visibility = View.GONE
                binding.stateImage.visibility = View.VISIBLE

                if (it.success) {
                    binding.stateImage.setImageResource(R.drawable.ic_done_dark)
                } else {
                    binding.stateImage.setImageResource(R.drawable.ic_error_dark)
                }
            }
        }
    }

    override fun toString(): String {
        return "type:${this.javaClass}\n$userName:$installBean"
    }
}
