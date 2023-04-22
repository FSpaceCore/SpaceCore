package com.fvbox.util.preference

import androidx.preference.PreferenceDataStore

import com.fvbox.util.Log
object EmptyPreferenceDataStore: PreferenceDataStore() {
    override fun putString(key: String?, value: String?) {

    }

    override fun putStringSet(key: String?, values: MutableSet<String>?) {

    }

    override fun putInt(key: String?, value: Int) {

    }

    override fun putLong(key: String?, value: Long) {
    }

    override fun putFloat(key: String?, value: Float) {
    }

    override fun putBoolean(key: String?, value: Boolean) {
    }

    override fun getString(key: String?, defValue: String?): String? {
        return defValue
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? {
        return defValues
    }

    override fun getInt(key: String?, defValue: Int): Int {
        return defValue
    }

    override fun getLong(key: String?, defValue: Long): Long {
        return defValue
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        return defValue
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return defValue
    }
}
