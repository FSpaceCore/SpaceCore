package com.fvbox.app.ui.setting.search

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.ferfalk.simplesearchview.SimpleSearchView
import com.fvbox.R
import com.fvbox.app.base.BaseActivity
import com.fvbox.databinding.ActivitySettingSearchBinding
import com.fvbox.util.property.viewBinding
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.select.getSelectExtension

/**
 *
 * @description:
 * @author: Jack
 * @create: 2022-07-07
 */
class SearchActivity : BaseActivity() {

    private val binding by viewBinding(ActivitySettingSearchBinding::bind)

    private val fastAdapter by lazy { FastItemAdapter<SettingSearchItem>() }

    private val selectHelper by lazy { fastAdapter.getSelectExtension() }

    private var titleID = 0

    private var filterStr: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_search)
        setUpToolbar(showBack = true)
        initAdapter()
        initData()
        initSearchView()
    }

    private fun initData() {
        titleID = intent.getIntExtra(INTENT_TITLE, 0)
        setToolbarTitle(titleID)

        val list = intent.getStringArrayExtra(INTENT_LIST) ?: emptyArray()
        val select = intent.getStringExtra(INTENT_SELECT)

        var selectIndex = 0
        val itemList = mutableListOf<SettingSearchItem>()

        list.forEachIndexed { index, s ->
            val item = SettingSearchItem().withText(s)
            itemList.add(item)
            if (s == select) {
                selectIndex = index
            }
        }

        fastAdapter.set(itemList)
        selectHelper.select(selectIndex)
        fastAdapter.filter(filterStr)

    }

    private fun initAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = fastAdapter

        selectHelper.isSelectable = true
        selectHelper.selectWithItemUpdate = true

        fastAdapter.onClickListener = { _, _, _, position ->
            selectHelper.select(position)
            selectAndFinish()
            true
        }

        fastAdapter.itemFilter.filterPredicate =
            { item: SettingSearchItem, constraint: CharSequence? ->
                item.text.contains(constraint.toString(), true)
            }
    }

    private fun selectAndFinish() {
        val intent = Intent()
        intent.putExtra(INTENT_TITLE, titleID)
        intent.putExtra(INTENT_SELECT, selectHelper.selectedItems.firstOrNull()?.text)
        setResult(RESULT_OK, intent)
        finish()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.local_search) {
            binding.searchView.showSearch()
            return true
        }
        return onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_local_app, menu)
        return true
    }

    override fun onBackPressed() {
        if (binding.searchView.onBackPressed()) {
            filterStr = null
            fastAdapter.filter(null)
            return
        }

        finish()
    }


    companion object {

        const val INTENT_TITLE = "intent_title"

        const val INTENT_SELECT = "intent_select"

        const val INTENT_LIST = "intent_list"

    }
}
