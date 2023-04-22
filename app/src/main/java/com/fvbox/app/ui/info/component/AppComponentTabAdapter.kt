package com.fvbox.app.ui.info.component

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class AppComponentTabAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val tagArray: List<Int>
) : FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int {
        return tagArray.count()
    }

    override fun createFragment(position: Int): Fragment {
        return AppComponentFragment.create(tagArray[position])
    }
}
