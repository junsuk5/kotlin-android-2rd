package com.survivalcoding.todolist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao    // 1
interface TodoDao {
    @Query("SELECT * FROM todo ORDER BY date DESC")     // 2
    fun getAll(): Flow<List<Todo>>      // 3

    @Insert(onConflict = OnConflictStrategy.REPLACE)    // 2
    suspend fun insert(entity: Todo)        // 4

    @Update     // 2
    suspend fun update(entity: Todo)        // 4

    @Delete     // 2
    suspend fun delete(entity: Todo)        // 4
}