package com.fvbox.app.ui.local

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.ferfalk.simplesearchview.SimpleSearchView
import com.fvbox.R
import com.fvbox.app.base.BaseActivity
import com.fvbox.app.contract.OpenApkDocument
import com.fvbox.app.ui.install.BoxInstallActivity
import com.fvbox.app.ui.install.progress.InstallProgressFragment
import com.fvbox.data.state.LocalAppState
import com.fvbox.databinding.ActivityLocalAppsBinding
import com.fvbox.util.extension.isShow
import com.fvbox.util.property.viewBinding
import com.fvbox.util.showSnackBar
import com.mikepenz.fastadapter.ISelectionListener
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.select.getSelectExtension
import java.util.concurrent.atomic.AtomicInteger


class LocalAppActivity : BaseActivity() {

    private val binding by viewBinding(ActivityLocalAppsBinding::bind)

    private val viewModel by viewModels<LocalAppsViewModel>()

    private val fastAdapter by lazy { FastItemAdapter<LocalAppsItem>() }

    private val adapterSelectHelper by lazy { fastAdapter.getSelectExtension() }

    private var filterStr: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_apps)

        setUpToolbar(R.string.install_app, true)
        initMultiSelect()
        initRecyclerView()
        initSearchView()

        loadData()
    }

    private fun initRecyclerView() {

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.adapter = fastAdapter
        fastAdapter.itemFilter.filterPredicate = { item: LocalAppsItem, constraint: CharSequence? ->
            val appBean = item.appBean
            if (appBean == null) {
                true
            } else {
                appBean.pkg.contains(constraint.toString(), true)
                        || appBean.name.contains(constraint.toString(), true)
            }
        }

        fastAdapter.onClickListener = { _, _, item, _ ->
            if (item.appBean?.isSupport == true) {
                if (!adapterSelectHelper.isSelectable) {
                    item.appBean?.let {
                        startInstall(arrayListOf(it.pkg))
                    }
                }
            } else {
                showSnackBar(getString(R.string.no_suuport_abi))
            }
            true
        }

    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                filterStr = newText
                fastAdapter.filter(newText)
                return true
            }

            override fun onQueryTextCleared(): Boolean {
                filterStr = null
                fastAdapter.filter(null)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

        })
    }

    private fun loadData() {
        viewModel.boxActionState.observe(this, boxActionStateObserver)
        viewModel.getLocalAppList().observe(this) { appsState ->
            when (appsState) {
                is LocalAppState.Loading -> {
                    binding.stateView.showLoading()
                }

                is LocalAppState.Empty -> {
                    binding.stateView.showEmpty()
                }

                is LocalAppState.Success -> {
                    binding.stateView.showContent()

                    appsState.list.map {
                        LocalAppsItem().withBean(it)
                    }.apply {
                        fastAdapter.set(this)
                        fastAdapter.filter(filterStr)
                    }
                }
            }
        }

    }


    /*****************多选*************************************/

    private val selectNum = AtomicInteger(0)

    private fun initMultiSelect() {
        binding.fab.setOnClickListener {
            if (adapterSelectHelper.isSelectable) {
                val pkgList = arrayListOf<String>()
                adapterSelectHelper.selectedItems.forEach {
                    it.appBean?.pkg?.let(pkgList::add)
                }
                startInstall(pkgList)
            } else {
                enterMultiSelect()
            }
        }

        adapterSelectHelper.isSelectable = false
        adapterSelectHelper.multiSelect = true
        adapterSelectHelper.selectOnLongClick = false
        adapterSelectHelper.selectWithItemUpdate = true

        adapterSelectHelper.selectionListener = object : ISelectionListener<LocalAppsItem> {
            override fun onSelectionChanged(item: LocalAppsItem, selected: Boolean) {
                if (selected) {
                    selectNum.incrementAndGet()
                } else {
                    selectNum.decrementAndGet()
                }
                toolbar.title = getString(R.string.select_num, selectNum.get())
                binding.fab.isShow(selectNum.get() != 0)
            }
        }

        fastAdapter.onPreClickListener = { _, _, item, _ ->
            if (item.appBean?.isSupport == true) {
                false
            } else {
                showSnackBar(getString(R.string.no_suuport_abi))
                true
            }
        }

    }

    private fun startInstall(pkgList: ArrayList<String>) {
        BoxInstallActivity.start(this, currentUserID(), pkgList)
    }

    private fun enterMultiSelect() {
        adapterSelectHelper.isSelectable = true
        toolbar.title = getString(R.string.select_num, 0)
        binding.fab.setImageResource(R.drawable.ic_done_all_light)
        binding.fab.isShow(false)
        selectNum.set(0)
    }

    private fun exitMultiSelect() {
        adapterSelectHelper.isSelectable = false
        adapterSelectHelper.deselect()
        toolbar.setTitle(R.string.install_app)
        binding.fab.setImageResource(R.drawable.ic_menu_light)
    }


    private val openDocument = registerForActivityResult(OpenApkDocument()) {
        if (it != null) {
            BoxInstallActivity.start(this,currentUserID(),it)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.searchView.closeSearch()
    }

    override fun onBackPressed() {
        if (binding.searchView.onBackPressed()) {
            filterStr = null
            fastAdapter.filter(null)
            return
        }

        if (adapterSelectHelper.isSelectable) {
            exitMultiSelect()
            return
        }

        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.local_search) {
            binding.searchView.showSearch()
            return true
        } else if (item.itemId == R.id.local_file) {
            openDocument.launch(Unit)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_local_app, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BoxInstallActivity.REQUEST_CODE) {
            val installSuccess =
                data?.getBooleanExtra(InstallProgressFragment.INTENET_SUCCESS, false)
            if (installSuccess == true) {
                setResult(RESULT_OK, intent)
                finish()
            }
        }

    }

    companion object {

        const val REQUEST_CODE = 111

        fun start(activity: BaseActivity, userID: Int?) {
            val intent = Intent(activity, LocalAppActivity::class.java)
            intent.putExtra(INTENT_USERID, userID)

            activity.startActivityForResult(intent, REQUEST_CODE)
        }
    }


}
