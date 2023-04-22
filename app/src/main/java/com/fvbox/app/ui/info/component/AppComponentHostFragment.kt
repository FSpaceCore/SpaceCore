package com.fvbox.app.ui.info.component

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.fvbox.R
import com.fvbox.app.base.BaseFragment
import com.fvbox.data.bean.db.RuleState
import com.fvbox.databinding.FragmetnAppComponentHostBinding
import com.fvbox.util.property.viewBinding
import com.google.android.material.tabs.TabLayout


class AppComponentHostFragment : BaseFragment(R.layout.fragmetn_app_component_host) {

    private val binding by viewBinding(FragmetnAppComponentHostBinding::bind)

    private val tagMapping = linkedMapOf(
        R.string.activity to RuleState.ACTIVITY,
        R.string.service to RuleState.SERVICE,
        R.string.broadcast to RuleState.BROADCAST,
        R.string.content_provider to RuleState.CONTENT_PROVIDER,
         R.string.process to RuleState.PROCESS
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTab()
        initViewPager()
    }

    override fun onResume() {
        super.onResume()
        baseActivity().setToolbarTitle(R.string.package_manger)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            baseActivity().setToolbarTitle(R.string.package_manger)
        }
    }

    private fun initTab() {
        tagMapping.keys.forEach { tag ->
            binding.tabLayout.newTab().let { tab ->
                tab.setText(tag)
                binding.tabLayout.addTab(tab)
            }
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager2.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }

        })
    }

    private fun initViewPager() {
        val adapter = AppComponentTabAdapter(childFragmentManager, lifecycle, tagMapping.values.toList())
        binding.viewPager2.adapter = adapter
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.getTabAt(position)?.select()
            }
        })
    }
}
