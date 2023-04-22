package com.fvbox.app.ui.install.select

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.fvbox.R
import com.fvbox.app.base.BaseFragment
import com.fvbox.app.ui.install.BoxInstallViewModel
import com.fvbox.app.ui.install.progress.InstallProgressFragment
import com.fvbox.data.bean.box.BoxUserInfo
import com.fvbox.data.bean.local.LocalAppBean
import com.fvbox.data.state.BoxInstallUserState
import com.fvbox.databinding.FragmentBoxInstallSelectBinding
import com.fvbox.util.extension.isShow
import com.fvbox.util.property.viewBinding
import com.mikepenz.fastadapter.ISelectionListener
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.select.getSelectExtension
import kotlinx.coroutines.launch


class BoxInstallSelectFragment : BaseFragment(R.layout.fragment_box_install_select) {

    private val binding by viewBinding(FragmentBoxInstallSelectBinding::bind)

    private val viewModel by activityViewModels<BoxInstallViewModel>()

    private val appAdapter by lazy { FastItemAdapter<InstallAppItem>() }

    private val userAdapter by lazy { FastItemAdapter<UserSelectItem>() }

    private val userSelectHelper by lazy { userAdapter.getSelectExtension() }

    private val selectUserList = arrayListOf<BoxUserInfo>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initFab()
        observeData()
    }

    private fun initFab() {
        binding.fab.isShow(false)
        binding.fab.setOnClickListener {
            lifecycleScope.launch {
                selectUserList.sortBy { it.userID }
                startInstall(selectUserList)
            }
        }
    }

    private fun startInstall(selectUserList: List<BoxUserInfo>) {
        viewModel.startInstall(selectUserList)
        InstallProgressFragment.start(parentFragmentManager)
    }

    private fun initRecyclerView() {

        binding.appRecyclerView.adapter = appAdapter
        binding.appRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.userRecyclerView.adapter = userAdapter
        binding.userRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        userSelectHelper.isSelectable = true
        userSelectHelper.selectWithItemUpdate = true
        userSelectHelper.multiSelect = true
        userSelectHelper.selectionListener = object : ISelectionListener<UserSelectItem> {
            override fun onSelectionChanged(item: UserSelectItem, selected: Boolean) {
                if (selected) {
                    item.getUser()?.let { selectUserList.add(it) }
                } else {
                    selectUserList.remove(item.getUser())
                }

                binding.fab.isShow(selectUserList.isNotEmpty())
            }
        }
    }

    private fun observeData() {
        viewModel.installUserState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is BoxInstallUserState.Loading -> {
                    binding.stateView.showLoading()
                }

                is BoxInstallUserState.Empty -> {
                    binding.stateView.showEmpty()
                    //理论上不会出现，挂个空界面得了
                }

                is BoxInstallUserState.LoadSuccess -> {
                    binding.stateView.showContent()
                    if (state.userList.size == 1) {
                        startInstall(state.userList)
                        return@observe
                    }

                    initAppList(state.appList)
                    initUser(state.userList)

                }
            }

        }
    }

    private fun initAppList(appList: List<LocalAppBean>) {
        appList.map {
            InstallAppItem().withBean(it)
        }.apply {
            appAdapter.set(this)
        }
    }

    private fun initUser(userList: List<BoxUserInfo>) {
        userList.map {
            UserSelectItem().withUser(it)
        }.apply {
            userAdapter.set(this)
        }

    }
}
