package com.withpeace.withpeace.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_search_keywords")
data class SearchKeywordEntity(
    @PrimaryKey(autoGenerate = false) val keyword: String,
    val timestamp: Long = System.currentTimeMillis(), // 저장 시각
)