package com.fvbox.app.ui.info.permission

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.fvbox.R
import com.fvbox.app.base.BaseFragment
import com.fvbox.app.ui.info.AppInfoActivity
import com.fvbox.app.ui.info.AppInfoPreferenceFragment
import com.fvbox.data.bean.box.BoxPermissionBean
import com.fvbox.data.state.BoxPermissionState
import com.fvbox.databinding.FragmentAppPermissionBinding
import com.fvbox.util.property.viewBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.ModelAdapter


class AppPermissionFragment : BaseFragment(R.layout.fragment_app_permission) {

    private val binding by viewBinding(FragmentAppPermissionBinding::bind)

    private val viewModel by activityViewModels<PermissionViewModel>()

    private lateinit var itemAdapter: ModelAdapter<Any, GenericItem>

    private val appBean by lazy { attachActivity<AppInfoActivity>().getAppBean() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeData()
    }

    override fun onResume() {
        super.onResume()
        baseActivity().setToolbarTitle(R.string.permission_manager)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden){
            baseActivity().setToolbarTitle(R.string.permission_manager)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initRecyclerView() {
        itemAdapter = ModelAdapter.models(this::convertData)
        val fastAdapter = FastAdapter.with(itemAdapter)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = fastAdapter
        fastAdapter.onClickListener = { view, _, item, _ ->
            val tvName = view?.findViewById<View>(R.id.tvName)
            if (tvName != null && item is PermissionItem && item.bean != null) {
                showMenu(tvName, item.bean!!)
            }
            true
        }
    }

    private fun observeData() {
        viewModel.loadPermission(appBean.userID, appBean.pkg)
        viewModel.boxAppState.observe(viewLifecycleOwner) {
            when (it) {
                is BoxPermissionState.Loading -> {
                    binding.stateView.showLoading()
                }

                is BoxPermissionState.Success -> {
                    if (it.list.isNotEmpty()) {
                        binding.stateView.showContent()
                        setListData(it.list)
                    }else{
                        binding.stateView.showEmpty()
                    }
                }
            }
        }
    }

    private fun setListData(list: List<BoxPermissionBean>) {
        val dataList = arrayListOf<Any>()
        list.groupBy {
            it.status
        }.toSortedMap { last, next ->
            next - last
        }.forEach { (status, beanList) ->
            dataList.add(status)
            dataList.addAll(beanList)
        }
        itemAdapter.set(dataList)
    }

    private fun convertData(any: Any?): GenericItem {
        return when (any) {
            is BoxPermissionBean -> {
                PermissionItem().withBean(any)
            }
            is Int -> {
                PermissionHeaderItem().withName(any)
            }
            else -> {
                PermissionHeaderItem().withName(any.hashCode())
                //走不到这里来，不管它
            }
        }
    }

    private fun showMenu(view: View, bean: BoxPermissionBean) {
        val menu = PopupMenu(requireContext(), view)
        menu.inflate(R.menu.menu_permission_action)
        menu.setOnMenuItemClickListener {
            viewModel.changePermission(appBean.userID, appBean.pkg, bean.permission, it.itemId)
            true
        }
        menu.show()
    }
}
