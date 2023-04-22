package com.fvbox.util.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.fvbox.R
import com.fvbox.app.ui.info.permission.AppPermissionFragment

import com.fvbox.util.Log
inline fun <reified T : Fragment> showFragment(fragmentManager: FragmentManager, block: () -> T) {

    fragmentManager.commit {
        var instance: Fragment? = null

        fragmentManager.fragments.forEach {
            if (it is T) {
                instance = it
            } else {
                hide(it)
            }
        }

        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        if (instance != null && instance!!.isAdded) {
            show(instance!!)
        } else {
            add(R.id.fragment_container, instance ?: block.invoke())
            addToBackStack(T::class.java.name)
        }
    }

}
