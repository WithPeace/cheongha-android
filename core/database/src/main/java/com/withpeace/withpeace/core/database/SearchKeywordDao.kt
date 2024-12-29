package com.withpeace.withpeace.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchKeywordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeyword(keyword: SearchKeywordEntity)

    @Query("SELECT * FROM recent_search_keywords ORDER BY timestamp DESC")
    suspend fun getAllKeywords(): List<SearchKeywordEntity>

    @Delete
    suspend fun deleteKeyword(keyword: SearchKeywordEntity)

    @Query("DELETE FROM recent_search_keywords WHERE id = :id")
    suspend fun deleteKeywordById(id: Int)

    @Query("DELETE FROM recent_search_keywords WHERE keyword = :keyword")
    suspend fun deleteKeywordByValue(keyword: String)

    @Query("DELETE FROM recent_search_keywords")
    suspend fun clearAllKeywords()
}