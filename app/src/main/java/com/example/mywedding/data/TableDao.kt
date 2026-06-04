package com.example.mywedding.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TableDao {

    @Insert
    fun insertTable(table: TableEntity)

    @Update
    fun updateTable(table: TableEntity)

    @Delete
    fun deleteTable(table: TableEntity)

    @Query("SELECT * FROM tables WHERE userId = :userId ORDER BY id ASC")
    fun getAllTables(userId: String): Flow<List<TableEntity>>
}
