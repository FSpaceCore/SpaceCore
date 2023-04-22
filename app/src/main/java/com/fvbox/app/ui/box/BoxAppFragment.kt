package com.fvbox.app.ui.box

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.fvbox.R
import com.fvbox.app.base.BaseFragment
import com.fvbox.app.ui.main.MainViewModel
import com.fvbox.app.ui.info.AppInfoActivity
import com.fvbox.data.bean.box.BoxAppBean
import com.fvbox.data.state.BoxActionState
import com.fvbox.data.state.BoxAppState
import com.fvbox.data.state.BoxRequestPermissionState
import com.fvbox.databinding.FragmentBoxAppsBinding
import com.fvbox.util.CaptureUtil
import com.fvbox.util.extension.showTwoStepDialog
import com.fvbox.util.property.viewBinding
import com.fvbox.util.showSnackBar
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger



class BoxAppFragment : BaseFragment(R.layout.fragment_box_apps) {

    private val binding by viewBinding(FragmentBoxAppsBinding::bind)

    private val viewModel by viewModels<BoxAppViewModel>()

    private val activityViewModel by activityViewModels<MainViewModel>()

    private val fastAdapter by lazy { FastItemAdapter<BoxAppsItem>() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        observeData()
    }

    private fun initRecyclerView() {

        binding.recyclerView.layoutManager = object : GridLayoutManager(requireContext(), 4) {
            override fun onLayoutCompleted(state: RecyclerView.State?) {
                super.onLayoutCompleted(state)
                capture()
            }
        }

        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.adapter = fastAdapter
        fastAdapter.onClickListener = { _, _, item, _ ->
            item.appBean?.let {
                viewModel.getAllowPermission(it)
            }
            true
        }

        fastAdapter.onLongClickListener = { view, _, item, _ ->
            item.appBean?.let {
                AppInfoActivity.start(baseActivity(), it.userID, it.pkg)
            }
            true
        }
    }

    private fun observeData() {

        activityViewModel.userChangeLiveData.observe(viewLifecycleOwner) {
            viewModel.loadBoxAppList(it.userID)
        }

        viewModel.boxActionState.observe(viewLifecycleOwner) {
            when (it) {
                is BoxActionState.Loading -> {
                    showLoadingDialog()
                }

                is BoxActionState.Success -> {
                    dismissLoadingDialog()
                    showSnackBar(it.msg)
                }

                is BoxActionState.Fail -> {
                    dismissLoadingDialog()
                    showSnackBar(it.msg)
                }
            }
        }

        viewModel.boxAppState.observe(viewLifecycleOwner) { appState ->
            when (appState) {
                is BoxAppState.Loading -> {
                    binding.stateView.showLoading()
                }

                is BoxAppState.Empty -> {
                    binding.stateView.showEmpty()
                    fastAdapter.clear()
                    CaptureUtil.deleteCapture(currentUserID())
                    //空数据就把截图删了
                    //因为截图是取上半部分，但是空界面是居中的，截取出来是不完整的
                }

                is BoxAppState.Success -> {
                    binding.stateView.showContent()
                    appState.list.map {
                        BoxAppsItem().withBean(it)
                    }.apply {
                        fastAdapter.set(this)
                    }
                }
            }
        }

        viewModel.appPermissionState.observe(viewLifecycleOwner) {
            when (it) {
                is BoxRequestPermissionState.Loading -> {
                    showLoadingDialog()
                }

                is BoxRequestPermissionState.Empty -> {
                    dismissLoadingDialog()
                    viewModel.launchApp(it.appBean)
                }

                is BoxRequestPermissionState.Success -> {
                    dismissLoadingDialog()

                    requestAppPermission(it.appBean, it.list)
                }
            }
        }
    }

    private val needRequest = AtomicBoolean(false)

    private fun requestAppPermission(appBean: BoxAppBean, list: List<String>) {
        val permissionX = PermissionX.init(this).permissions(list)
        if (needRequest.getAndSet(false)) {
            permissionX.onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                        deniedList,
                        getString(R.string.permission_to_setting),
                        getString(R.string.done),
                        getString(R.string.cancel)
                )
            }
        }
        permissionX.request { allGranted, _, _ ->
            if (allGranted) {
                viewModel.launchApp(appBean)
                return@request
            }
            MaterialDialog(requireContext()).show {
                title(R.string.permission_request_denied)
                message(R.string.permission_denied_hint)
                positiveButton(R.string.launch_first) { _ ->
                    viewModel.launchApp(appBean)
                }
                negativeButton(R.string.request_again) {
                    needRequest.set(true)
                    requestAppPermission(appBean, list)
                }
            }
        }
    }


    private val captureID = AtomicInteger()

    private fun capture() {
        val userID = currentUserID()
        lifecycleScope.launch {
            val mCaptureID = captureID.incrementAndGet()
            delay(500)
            //延迟500，等界面刷新完成
            if (captureID.get() == mCaptureID) {
                val activityContent = requireActivity().findViewById<View>(android.R.id.content)
                CaptureUtil.captureView(requireActivity().window, activityContent)
                //背景
                CaptureUtil.captureView(requireActivity().window, binding.root, userID)
            }
        }
    }

    private fun currentUserID(): Int {
        return activityViewModel.userChangeLiveData.value?.userID ?: 0
    }


}
