package com.example.mywedding.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

    @Insert
    fun insertPlan(plan: PlanEntity)

    @Delete
    fun deletePlan(plan: PlanEntity)

    @Query("SELECT * FROM plans WHERE userId = :userId ORDER BY time ASC")
    fun getAllPlans(userId: String): Flow<List<PlanEntity>>
}