package com.example.mywedding.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoryDao {

    @Insert
    fun insertMemory(memory: MemoryEntity)

    @Update
    fun updateMemory(memory: MemoryEntity)

    @Delete
    fun deleteMemory(memory: MemoryEntity)

    @Query("SELECT * FROM memories WHERE userId = :userId ORDER BY id DESC")
    fun getAllMemories(userId: String): Flow<List<MemoryEntity>>
}