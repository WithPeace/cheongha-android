package com.withpeace.withpeace.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchKeywordEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchKeywordDao(): SearchKeywordDao
}



