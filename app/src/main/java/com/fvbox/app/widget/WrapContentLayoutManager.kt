package com.fvbox.app.widget

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @description:
 * @author: Jack
 * @create: 2022-06-17
 */
class WrapContentLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        runCatching {
            super.onLayoutChildren(recycler, state)
        }
    }

}
