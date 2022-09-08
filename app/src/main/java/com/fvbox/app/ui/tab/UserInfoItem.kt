package com.fvbox.app.ui.tab

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.fvbox.R
import com.fvbox.app.ui.install.progress.InstallItemItem
import com.fvbox.data.bean.box.BoxUserInfo
import com.fvbox.databinding.ItemUserSelectBinding
import com.fvbox.util.CaptureUtil
import com.fvbox.util.coli.TopCropTransformation
import com.fvbox.util.extension.dp
import com.mikepenz.fastadapter.binding.AbstractBindingItem

/**
 *
 * @Description: item
 * @Author: Jack
 * @CreateDate: 2022/5/17 23:21
 */

class UserInfoItem : AbstractBindingItem<ItemUserSelectBinding>() {

    var userInfo: BoxUserInfo? = null


    override val type: Int
        get() = R.id.app_name

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemUserSelectBinding {
        return ItemUserSelectBinding.inflate(inflater, parent, false)
    }

    fun withBean(userInfo: BoxUserInfo): UserInfoItem {
        this.userInfo = userInfo
        return this
    }


    fun withIdentifier(id:Int): UserInfoItem {
        this.identifier = id.toLong()
        return this
    }

    override fun bindView(binding: ItemUserSelectBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.root.isSelected = isSelected
        userInfo?.let {
            binding.userName.text = it.userName
            if (CaptureUtil.getCapturePath(it.userID).exists()) {
                binding.userPreview.scaleType = ImageView.ScaleType.FIT_XY
                binding.userPreview.load(CaptureUtil.getCapturePath(it.userID)) {
                    transformations(
                        TopCropTransformation(),
                        RoundedCornersTransformation(
                            topLeft = 6.dp,
                            topRight = 6.dp,
                            bottomLeft = 12.dp,
                            bottomRight = 12.dp
                        )
                    )
                }
            } else {
                binding.userPreview.scaleType = ImageView.ScaleType.CENTER_INSIDE
                binding.userPreview.setImageResource(R.drawable.ic_empty)
            }
        }

    }


}
