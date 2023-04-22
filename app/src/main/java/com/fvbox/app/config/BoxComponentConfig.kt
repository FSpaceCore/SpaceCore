package com.fvbox.app.config

import com.fvbox.R
import com.fvbox.data.BoxDatabase
import com.fvbox.data.bean.db.RuleBean
import com.fvbox.data.bean.db.RuleState


object BoxComponentConfig {

    private val cache = hashMapOf<String, List<RuleBean>>()

    fun getBlockComponentList(pkg: String, userID: Int, type: Int): List<String> {
        return try {
            val key = "$pkg#$userID"
            val blockComponentList = if (cache.containsKey(key)) {
                cache[key]
            } else {
                BoxDatabase.instance.ruleDao().getComponentList(pkg, userID, type)
            }

            if (blockComponentList.isNullOrEmpty()) {
                return emptyList()
            }

            blockComponentList.map { it.componentName }
        }catch (e:Exception){
            emptyList()
        }
    }

    fun getStateRule(pkg: String, userID: Int): List<RuleBean> {
        return try {
            BoxDatabase.instance.ruleDao().getStateRule(pkg, userID)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun addStateRule(pkg: String, userID: Int, type: Int) {
        runCatching {
            val bean = RuleBean(0, pkg, userID, type, "")
            BoxDatabase.instance.ruleDao().addComponent(bean)
        }
    }

    fun removeStateRule(pkg: String,userID: Int,type: Int){
        runCatching {
            BoxDatabase.instance.ruleDao().removeStateRule(pkg, userID, type)
        }
    }

    fun addBlockComponent(pkg: String, userID: Int, componentName: String, type: Int) {
        runCatching {
            val key = "$pkg#$userID"
            val rule = RuleBean(0, pkg, userID, type, componentName)
            BoxDatabase.instance.ruleDao().addComponent(rule)
            cache.remove(key)
        }
    }

    fun removeBlockComponent(pkg: String, userID: Int, componentName: String, type: Int) {
        runCatching {
            val key = "$pkg#$userID"
            BoxDatabase.instance.ruleDao().removeComponent(pkg, userID, type, componentName)
            cache.remove(key)
        }
    }

}
