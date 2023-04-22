package com.fvbox.app.ui.tab

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.afollestad.materialdialogs.MaterialDialog
import com.fvbox.R
import com.fvbox.app.base.BaseActivity
import com.fvbox.data.bean.box.BoxUserInfo
import com.fvbox.databinding.ActivityUserSelectBinding
import com.fvbox.util.CaptureUtil
import com.fvbox.util.property.viewBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.diff.DiffCallback
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.select.SelectExtension
import com.mikepenz.fastadapter.select.getSelectExtension


class UserSelectActivity : BaseActivity() {

    private val binding by viewBinding(ActivityUserSelectBinding::bind)

    private val viewModel by viewModels<UserSelectViewModel>()

    private lateinit var selectHelper: SelectExtension<UserInfoItem>

    private val fastAdapter by lazy { FastItemAdapter<UserInfoItem>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_select)

        setUpToolbar(R.string.app_name)
        initOtherView()
        initRecyclerView()
        observeData()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initOtherView() {
        binding.addUser.setOnClickListener {
            viewModel.addUser()
        }

        binding.image.load(CaptureUtil.getCapturePath()) {
            placeholder(R.drawable.blur_def)
            error(R.drawable.blur_def)
        }

        binding.image.setOnClickListener {
            changeUserAndFinish()
        }

        binding.recyclerView.setOnTouchListener { v, event ->
            if (v is RecyclerView) {
                return@setOnTouchListener recyclerViewGestureDetector.onTouchEvent(event)
            }
            return@setOnTouchListener false
        }
    }

    private val recyclerViewGestureDetector by lazy {
        GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                changeUserAndFinish()
                return true
            }
        })
    }


    private fun initRecyclerView() {

        selectHelper = fastAdapter.getSelectExtension()
        selectHelper.isSelectable = true
        selectHelper.multiSelect = false
        selectHelper.selectWithItemUpdate = true

        binding.recyclerView.adapter = fastAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        fastAdapter.onClickListener = { _, _, item, _ ->
            changeUserAndFinish(item.userInfo?.userID)
            true
        }
        fastAdapter.addEventHook(object : ClickEventHook<UserInfoItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return viewHolder.itemView.findViewById(R.id.deleteUser)
            }

            override fun onClick(
                v: View,
                position: Int,
                fastAdapter: FastAdapter<UserInfoItem>,
                item: UserInfoItem
            ) {
                deleteUser(item.userInfo?.userID)
            }
        })
    }

    private fun observeData() {

        viewModel.userLiveData.observe(this) {
            initAdapter(it)
        }

        viewModel.boxActionState.observe(this, boxActionStateObserver)
    }

    private fun initAdapter(list: List<BoxUserInfo>) {

        if (list.isEmpty()) {
            noSelectFinish()
            return
        }

        var selectIndex = 0

        val newList = list.sortedBy { it.userID }.mapIndexed { index, boxUserInfo ->
            if (boxUserInfo.userID == currentUserID()) {
                selectIndex = index
            }
            UserInfoItem().withBean(boxUserInfo).withIdentifier(boxUserInfo.userID)
        }

        val diffCallback =
            FastAdapterDiffUtil.calculateDiff(fastAdapter.itemAdapter, newList, diffCallback)
        FastAdapterDiffUtil[fastAdapter.itemAdapter] = diffCallback

        selectHelper.select(selectIndex)
        binding.recyclerView.smoothScrollToPosition(selectIndex)
    }

    private val diffCallback = object : DiffCallback<UserInfoItem> {

        override fun areContentsTheSame(oldItem: UserInfoItem, newItem: UserInfoItem): Boolean {
            return oldItem.userInfo?.userID == newItem.userInfo?.userID
        }

        override fun areItemsTheSame(oldItem: UserInfoItem, newItem: UserInfoItem): Boolean {
            return oldItem.userInfo?.userID == newItem.userInfo?.userID
        }

        override fun getChangePayload(
            oldItem: UserInfoItem,
            oldItemPosition: Int,
            newItem: UserInfoItem,
            newItemPosition: Int
        ): Any {
            return 0
        }

    }


    private fun deleteUser(userID: Int?) {
        userID ?: return

        MaterialDialog(this).show {
            title(R.string.delete_user)
            message(R.string.delete_user_msg)
            negativeButton(R.string.cancel)
            positiveButton(R.string.done) {
                viewModel.deleteUser(userID)
            }
        }
    }

    private fun changeUserAndFinish(userID: Int? = null) {

        var mUserID = userID
        if (mUserID == null) {
            mUserID = selectHelper.selectedItems.firstOrNull()?.userInfo?.userID
        }
        //没传就取选中的用户，也就是当前用户
        val intent = Intent()
        intent.putExtra(INTENT_USERID, mUserID)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun noSelectFinish() {
        setResult(RESULT_EMPTY)
        finish()
    }

    override fun onBackPressed() {
        changeUserAndFinish()
    }

    companion object {
        private const val TAG = "UserSelectActivity"

        const val REQUEST_CODE = 112

        const val RESULT_EMPTY = 113

        fun start(activity: BaseActivity, userID: Int?) {
            val intent = Intent(activity, UserSelectActivity::class.java)
            intent.putExtra(INTENT_USERID, userID)
            activity.startActivityForResult(intent, REQUEST_CODE)
        }
    }

}
