package com.fvbox.util.property

import com.fvbox.app.config.BoxConfig
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

import com.fvbox.util.Log

open class MMKVProperty<Data>(private val default: Data, private val mmkvImpl: MMKV = BoxConfig.mmkv) :
    ReadWriteProperty<Any, Data> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Data {
        return findData(property.name, default)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Data) {
        putData(property.name, value)
    }

    private fun findData(key: String, default: Data): Data {
        with(mmkvImpl) {
            val result: Any? = when (default) {
                is Int -> decodeInt(key, default)
                is Long -> decodeLong(key, default)
                is Float -> decodeFloat(key, default)
                is String -> decodeString(key, default)
                is Boolean -> decodeBool(key, default)
                else -> throw IllegalArgumentException("This type $default can not be saved into mmkv")
            }

            return result as Data ?: default
        }
    }

    private fun putData(key: String, value: Data?) {
        with(mmkvImpl) {
            if (value == null) {
                removeValueForKey(key)
            } else {
                when (value) {
                    is Int -> encode(key, value)
                    is Long -> encode(key, value)
                    is Float -> encode(key, value)
                    is String -> encode(key, value)
                    is Boolean -> encode(key, value)
                    else -> throw IllegalArgumentException("This type $default can not be saved into Preferences")
                }
            }
        }
    }
}

