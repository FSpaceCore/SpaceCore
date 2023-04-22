package com.fvbox.app.config

import com.fvbox.data.BoxDatabase
import com.fvbox.data.bean.db.RuleBean
import com.fvbox.data.bean.db.RuleState
import com.fvbox.lib.FCore
import com.fvbox.lib.rule.common.PackageRule
import com.fvbox.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


object BoxConfigLoader {

    fun loadRule() {
        runBlocking {
            withContext(Dispatchers.IO) {
                runCatching {
                    BoxDatabase.instance.ruleDao().getAllPackage().forEach {
                        val builder = PackageRule.Builder(it.packageName).addUserId(it.userID)
                        val ruleList = BoxDatabase.instance.ruleDao().getAllComponent(it.packageName, it.userID)
                        ruleList.forEach { rule ->
                            addRule(builder, rule)
                        }
                        FCore.get().addRule(builder.build())
                    }
                }
            }
        }
    }

    private fun addRule(builder: PackageRule.Builder, rule: RuleBean) {
        val name = rule.componentName
        when (rule.type) {
            RuleState.ACTIVITY -> builder.addBlackActivity(name)
            RuleState.SERVICE -> builder.addBlackService(name)
            RuleState.BROADCAST -> builder.addBlackBroadcast(name)
            RuleState.CONTENT_PROVIDER -> builder.addBlackContentProvider(name)
            RuleState.PROCESS -> builder.addBlackProcessName(name)
            RuleState.HIDE_PATH -> builder.isHidePath(true)
            RuleState.HIDE_ROOT -> builder.isHideRoot(true)
            RuleState.HIDE_SIM -> builder.isHideSim(true)
            RuleState.HIDE_VPN -> builder.isHideVpn(true)
            RuleState.VISIBLE_APP -> builder.isVisibleExternalApp(true)
            RuleState.DISABLE_NETWORK -> builder.isDisableNetwork(true)
            RuleState.ANTI_CRASH -> builder.isDisableKill(true)
        }
    }

}
