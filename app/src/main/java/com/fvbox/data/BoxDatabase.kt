package com.fvbox.data

import android.annotation.SuppressLint
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fvbox.data.bean.db.RuleBean
import com.fvbox.data.dao.RuleDao
import com.fvbox.util.ContextHolder


@Database(entities = [RuleBean::class], version = 1)
abstract class BoxDatabase : RoomDatabase() {
    abstract fun ruleDao(): RuleDao

    companion object {
        @SuppressLint("StaticFieldLeak")
        val instance = Room.databaseBuilder(ContextHolder.get(), BoxDatabase::class.java, "boxConfig").build()
    }
}
