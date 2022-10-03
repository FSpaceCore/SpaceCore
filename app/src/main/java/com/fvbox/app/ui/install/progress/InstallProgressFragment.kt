package com.fvbox.app.ui.install.progress

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.fvbox.R
import com.fvbox.app.base.BaseFragment
import com.fvbox.app.ui.install.BoxInstallViewModel
import com.fvbox.app.widget.WrapContentLayoutManager
import com.fvbox.data.state.BoxActionState
import com.fvbox.data.state.BoxInstallProgressState
import com.fvbox.databinding.FragmentInstallProgressBinding
import com.fvbox.util.property.viewBinding
import com.fvbox.util.showSnackBar
import com.fvbox.util.showToast
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.diff.DiffCallback
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *
 * @Description: 安装进度
 * @Author: Jack
 * @CreateDate: 2022/5/26 0:26
 */
class InstallProgressFragment : BaseFragment(R.layout.fragment_install_progress) {

    private val binding by viewBinding(FragmentInstallProgressBinding::bind)

    private val viewModel by activityViewModels<BoxInstallViewModel>()

    private val fastAdapter by lazy { FastItemAdapter<GenericItem>() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        observerData()
    }

    private fun initRecyclerView() {

        binding.recyclerView.adapter = fastAdapter
//        binding.recyclerView.itemAnimator = DefaultItemAnimator().apply {
//            supportsChangeAnimations = false
//        }
        binding.recyclerView.layoutManager = WrapContentLayoutManager(requireContext())

        fastAdapter.addEventHook(object : ClickEventHook<InstallItemItem>() {

            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return viewHolder.itemView.findViewById(R.id.stateImage)
            }

            override fun onClick(
                v: View,
                position: Int,
                fastAdapter: FastAdapter<InstallItemItem>,
                item: InstallItemItem
            ) {
                if (item.getItemState() == 3) {
                    showToast(item.getInstallBean()?.msg ?: "")
                }
            }

        })

    }


    private fun observerData() {
        viewModel.installProgress.observe(viewLifecycleOwner) { list ->

            if (list.isEmpty()) {
                return@observe
            }

            lifecycleScope.launch {
                runCatching {

                    val newList = convertItem(list)

                    val diffCallback = withContext(Dispatchers.IO) {
                        FastAdapterDiffUtil.calculateDiff(fastAdapter.itemAdapter, newList, diffCallback)
                    }

                    if (this.isActive) {
                        withContext(Dispatchers.Main) {
                            FastAdapterDiffUtil[fastAdapter.itemAdapter] = diffCallback
                            binding.recyclerView.layoutManager?.scrollToPosition(newList.lastIndex)
                        }
                    }
                }
            }
        }

        viewModel.boxActionState.observe(viewLifecycleOwner) {
            if (it is BoxActionState.Success) {
                showSnackBar(getString(R.string.install_finish))
                val intent = Intent()
                intent.putExtra(INTENET_SUCCESS, true)
                requireActivity().setResult(AppCompatActivity.RESULT_OK, intent)
            }
        }

    }


    private val diffCallback = object : DiffCallback<GenericItem> {

        override fun areContentsTheSame(oldItem: GenericItem, newItem: GenericItem): Boolean {
            if (oldItem is InstallHeaderItem) {
                return true
            }

            if (oldItem is InstallItemItem && newItem is InstallItemItem) {
                if (oldItem.getItemState() == newItem.getItemState()) {
                    return true
                }
            }

            return false
        }

        override fun areItemsTheSame(oldItem: GenericItem, newItem: GenericItem): Boolean {

            if (oldItem is InstallHeaderItem && newItem is InstallHeaderItem) {
                if (oldItem.getAppName() == oldItem.getAppName()) {
                    return true
                }
            }

            if (oldItem is InstallItemItem && newItem is InstallItemItem) {
                if (oldItem.getItemTag() == newItem.getItemTag()) {
                    return true
                }
            }
            return false
        }

        override fun getChangePayload(
            oldItem: GenericItem,
            oldItemPosition: Int,
            newItem: GenericItem,
            newItemPosition: Int
        ): Any {
            return 0
        }

    }

    private fun convertItem(list: List<BoxInstallProgressState>): List<GenericItem> {
        val newList = list.mapIndexed{ index, data ->
            when(data){
                is BoxInstallProgressState.Header -> {
                    InstallHeaderItem().withApp(data.appBean).withIdentifier(index)
                }
                is BoxInstallProgressState.Loading -> {
                    InstallItemItem().withUserName(data.userName).withPkg(data.pkg).withIdentifier(index)
                }
                is BoxInstallProgressState.Finish -> {
                    InstallItemItem().withUserName(data.userName).withInstallBean(data.installBean).withIdentifier(index)
                }
            }
        }
        return newList
    }

    companion object {

        val INTENET_SUCCESS = "Intent_Install_Success"

        fun start(fragmentManager: FragmentManager) {
            fragmentManager.commit {
                val fragment = InstallProgressFragment()
                replace(R.id.fragmentContainer, fragment)
            }
        }
    }

}
