package com.fvbox.app.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.fvbox.app.ui.setting.SingleChoicePreference
import com.fvbox.app.ui.setting.search.SearchActivity


class LaunchSearchActivity : ActivityResultContract<SingleChoicePreference, Pair<Int, String>?>() {

    private var choicePreference: SingleChoicePreference? = null

    override fun createIntent(context: Context, input: SingleChoicePreference): Intent {
        choicePreference = input
        val intent = Intent(context, SearchActivity::class.java)
        intent.putExtra(SearchActivity.INTENT_TITLE, input.title)
        intent.putExtra(SearchActivity.INTENT_SELECT, input.valueDelegate.get())
        intent.putExtra(SearchActivity.INTENT_LIST, input.list.toTypedArray())
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Int, String>? {
        if (resultCode == Activity.RESULT_OK && intent != null) {
            val select = intent.getStringExtra(SearchActivity.INTENT_SELECT) ?: ""
            val title = intent.getIntExtra(SearchActivity.INTENT_TITLE, 0)
            choicePreference?.valueDelegate?.set(select)
            return title to select
        }
        return null
    }

}
