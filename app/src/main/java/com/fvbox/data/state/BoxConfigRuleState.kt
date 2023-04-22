package com.fvbox.data.state

import com.fvbox.data.bean.db.RuleBean
import com.fvbox.lib.rule.common.ConfigRule


sealed interface BoxConfigRuleState {
    object Loading:BoxConfigRuleState

    object Updated:BoxConfigRuleState

    data class Success(val list: List<RuleBean>):BoxConfigRuleState
}
