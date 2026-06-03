package com.example.mywedding.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert
    fun insertBudgetItem(item: BudgetEntity)

    @Update
    fun updateBudgetItem(item: BudgetEntity)

    @Delete
    fun deleteBudgetItem(item: BudgetEntity)

    @Query("SELECT * FROM budget_items WHERE userId = :userId ORDER BY id DESC")
    fun getAllBudgetItems(userId: String): Flow<List<BudgetEntity>>

    @Query("SELECT SUM(amount) FROM budget_items WHERE userId = :userId")
    fun getTotalSpent(userId: String): Flow<Double?>
}