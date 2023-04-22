package com.fvbox.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fvbox.data.bean.db.RuleBean
import com.fvbox.data.bean.db.RulePackageBean


@Dao
interface RuleDao {

    @Query("select * from rule where pkg=:pkg and userID=:userID and type=:type")
    fun getComponentList(pkg: String, userID: Int, type: Int): List<RuleBean>

    @Query("select * from rule where pkg=:pkg and userID=:userID")
    fun getAllComponent(pkg: String, userID: Int): List<RuleBean>

    @Insert
    fun addComponent(ruleBean: RuleBean)

    @Query("delete from rule where pkg=:pkg and userID=:userID and type=:type and component=:componentName")
    fun removeComponent(pkg: String, userID: Int, type: Int, componentName: String)

    @Query("delete from rule where pkg=:pkg and userID=:userID")
    fun removeAppRule(pkg: String, userID: Int)

    @Query("select distinct userID,pkg from rule")
    fun getAllPackage(): List<RulePackageBean>

    @Query("select * from rule where pkg=:pkg and userID=:userID and type not in (97,98,99,112,115)")
    fun getStateRule(pkg: String, userID: Int): List<RuleBean>

    @Query("delete from rule where pkg=:pkg and userID=:userID and type=:type")
    fun removeStateRule(pkg: String, userID: Int, type: Int)
}
