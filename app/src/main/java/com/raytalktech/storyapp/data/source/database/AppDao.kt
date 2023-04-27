package com.raytalktech.storyapp.data.source.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raytalktech.storyapp.model.StoriesResult

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: List<StoriesResult>)

    @Query("SELECT * FROM story")
    fun getAllQuote(): PagingSource<Int, StoriesResult>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}