package com.fvbox.app.ui.setting.device

import com.fvbox.R
import com.fvbox.lib.FCore
import com.fvbox.util.ContextHolder
import com.fvbox.util.extension.getString


class LanguageMapping(private val userID: Int) {

    private val withSystem = getString(R.string.same_with_system)

    private val languageMapping by lazy {
        readDataForAssets("language_code.txt")
    }

    var language: String = ""
        get() {
            val language = FCore.get().getSpaceLanguage(userID,null,null)
            return languageMapping[language] ?: ""
        }
        set(value) {
            FCore.get().setSpaceLanguage(userID, findKey(languageMapping, value))
            field = value
        }


    private val timeZoneList by lazy {
        readDataForAssets("timezone_code.txt")
    }

    var timeZone: String = ""
        get() {
            val timeZone = FCore.get().getSpaceTimeZone(userID, null, null)
            if (timeZone.isEmpty()) {
                return withSystem
            }
            return timeZone
        }
        set(value) {
            if (value == withSystem) {
                FCore.get().setSpaceTimeZone(userID, "")
            } else {
                FCore.get().setSpaceTimeZone(userID, value)
            }
            field = value
        }

    private val regionMapping by lazy {
        readDataForAssets("country_code.txt")
    }


    var region: String = ""
        get() {
            val region = FCore.get().getSpaceRegion(userID,null,null)
            return regionMapping[region] ?: ""
        }
        set(value) {
            FCore.get().setSpaceRegion(userID, findKey(regionMapping, value))
            field = value
        }

    fun getLanguageList(): List<String> {
        return languageMapping.values.toList()
    }

    fun getTimeZoneList(): List<String> {
        return timeZoneList.values.toList()
    }

    fun getRegionList(): List<String> {
        return regionMapping.values.toList()
    }

    private fun findKey(mapping: Map<String, String>, value: String): String {
        for (key in mapping.keys) {
            if (mapping[key] == value) {
                return key
            }
        }
        return ""
    }

    private fun readDataForAssets(filename: String): Map<String, String> {
        val map = LinkedHashMap<String, String>()
        map[""] = withSystem

        runCatching {
            ContextHolder.get().assets.open(filename).use { input ->
                input.bufferedReader().readLines().forEach {
                    val subList = it.split("\t")
                    map[subList[0]] = subList[1]
                }
            }
        }
        return map
    }

}
