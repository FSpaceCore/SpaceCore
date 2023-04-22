package com.fvbox.app.ui.info.component

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fvbox.R
import com.fvbox.app.base.BaseFragment
import com.fvbox.app.ui.info.AppInfoActivity
import com.fvbox.data.bean.box.BoxComponentBean
import com.fvbox.data.state.BoxActionState
import com.fvbox.data.state.BoxComponentState
import com.fvbox.databinding.FragmentAppComponentBinding
import com.fvbox.util.hideSnackBar
import com.fvbox.util.property.viewBinding
import com.fvbox.util.showSnackBar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AppComponentFragment : BaseFragment(R.layout.fragment_app_component) {

    private val binding by viewBinding(FragmentAppComponentBinding::bind)

    private val viewModel by viewModels<ComponentViewModel>()

    private val fastAdapter by lazy { FastItemAdapter<ComponentStatusItem>() }

    private var type = 0

    private val appBean by lazy { attachActivity<AppInfoActivity>().getAppBean() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        type = requireArguments().getInt(INTENT_TYPE)
        initView()
        observerData()
    }

    private fun initView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = fastAdapter
        fastAdapter.addEventHook(object : ClickEventHook<ComponentStatusItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return viewHolder.itemView.findViewById(R.id.enable)
            }

            override fun onClick(
                v: View,
                position: Int,
                fastAdapter: FastAdapter<ComponentStatusItem>,
                item: ComponentStatusItem
            ) {
                item.bean?.let {
                    viewModel.changeComponentStatus(
                        appBean.userID,
                        appBean.pkg,
                        type,
                        it.name,
                        !it.status
                    )
                    it.status = !it.status
                }
            }
        })
    }

    private fun observerData() {
        viewModel.loadComponentList(appBean.userID, appBean.pkg, type)
        viewModel.boxActionState.observe(viewLifecycleOwner) {
            when (it) {
                is BoxActionState.Loading -> {
                    showLoadingDialog()
                }

                is BoxActionState.Fail -> {
                }

                is BoxActionState.Success -> {
                    dismissLoadingDialog()
                    showSnackBar(getString(R.string.success_with_reboot), getString(R.string.reboot_now)) {
                        viewModel.restartSystemProcess()
                        showSnackBar(getString(R.string.reboot_success))
                    }
                }
            }
        }

        viewModel.componentState.observe(viewLifecycleOwner) {
            when (it) {
                is BoxComponentState.Loading -> {
                    binding.stateView.showLoading()
                }
                is BoxComponentState.Success -> {
                    if (it.list.isEmpty()) {
                        binding.stateView.showEmpty()
                    } else {
                        setData(it.list)
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        hideSnackBar()
    }

    private fun setData(list: List<BoxComponentBean>) {
        lifecycleScope.launch {
            runCatching {
                val newList = withContext(Dispatchers.IO) {
                    list.map { ComponentStatusItem().withBean(it) }
                }
                if (this.isActive) {
                    withContext(Dispatchers.Main) {
                        binding.stateView.showContent()
//                        fastAdapter.itemAdapter.itemList.setNewList(newList,true)
                        fastAdapter.set(newList)
                    }
                }
            }
        }
    }

    companion object {

        const val INTENT_TYPE = "IntentType"

        fun create(type: Int): AppComponentFragment {
            val fragment = AppComponentFragment()
            val bundle = bundleOf(INTENT_TYPE to type)
            fragment.arguments = bundle
            return fragment
        }
    }
}
