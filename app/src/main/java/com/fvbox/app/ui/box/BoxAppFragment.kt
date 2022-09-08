package com.fvbox.app.ui.box

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.fvbox.R
import com.fvbox.app.base.BaseFragment
import com.fvbox.app.contract.SaveZipDocument
import com.fvbox.app.ui.main.MainViewModel
import com.fvbox.data.bean.box.BoxAppBean
import com.fvbox.data.state.BoxActionState
import com.fvbox.data.state.BoxAppState
import com.fvbox.databinding.FragmentBoxAppsBinding
import com.fvbox.util.CaptureUtil
import com.fvbox.util.ShortcutUtil
import com.fvbox.util.property.viewBinding
import com.fvbox.util.showSnackBar
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

/**
 *
 * @Description: box 内部安装的app
 * @Author: Jack
 * @CreateDate: 2022/5/18 21:05
 */

class BoxAppFragment : BaseFragment(R.layout.fragment_box_apps) {

    private val binding by viewBinding(FragmentBoxAppsBinding::bind)

    private val viewModel by viewModels<BoxAppViewModel>()

    private val activityViewModel by activityViewModels<MainViewModel>()

    private val fastAdapter by lazy { FastItemAdapter<BoxAppsItem>() }

    private val menuAdapter by lazy { initMenuAdapter() }

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
                viewModel.launchApp(it)
            }
            true
        }

        fastAdapter.onLongClickListener = { _, _, item, _ ->
            item.appBean?.let {
                showLongClickDialog(it)
            }
            true
        }
    }

    /**
     * 初始化长按菜单的menu
     * 懒加载
     * @return FastItemAdapter<BoxAppMenuItem>
     */
    private fun initMenuAdapter(): FastItemAdapter<BoxAppMenuItem> {
        val mAdapter = FastItemAdapter<BoxAppMenuItem>()
        mAdapter.add(BoxMenuData.getMenuList())
        return mAdapter

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

    }

    private fun showLongClickDialog(appBean: BoxAppBean) {
        MaterialDialog(requireContext()).show {
            title(text = appBean.name)
            icon(drawable = appBean.icon)
            customListAdapter(menuAdapter)

            menuAdapter.onClickListener = { _, _, item, _ ->
                when (item.textRes) {
                    R.string.uninstall_app -> {
                        showTwoStepDialog(
                            R.string.uninstall_app,
                            R.string.uninstall_app_hint,
                            appBean,
                            viewModel::unInstallApp
                        )
                    }
                    R.string.force_stop -> {
                        showTwoStepDialog(
                            R.string.force_stop,
                            R.string.force_stop_hint,
                            appBean,
                            viewModel::forceStop
                        )
                    }
                    R.string.clear_data -> {
                        showTwoStepDialog(
                            R.string.clear_data,
                            R.string.clear_data_hint,
                            appBean,
                            viewModel::clearData
                        )
                    }
                    R.string.export_data -> {
                    }

                    R.string.app_shortcut -> {
                        runCatching {
                            ShortcutUtil.createShortcut(requireContext(), appBean)
                        }
                    }
                }
                dismiss()
                true
            }
        }
    }

    private fun showTwoStepDialog(
        titleRes: Int,
        messageRes: Int,
        appBean: BoxAppBean,
        block: (BoxAppBean) -> Unit
    ) {
        MaterialDialog(requireContext()).show {
            title(titleRes)
            message(text = getString(messageRes, appBean.name))
            negativeButton(R.string.cancel)
            positiveButton(R.string.done) {
                block.invoke(appBean)
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

    private val saveDocument = registerForActivityResult(SaveZipDocument()) {

        if (it.uri != null) {
            viewModel.exportData(it.userID, it.pkg, it.uri)
        }
    }

}
