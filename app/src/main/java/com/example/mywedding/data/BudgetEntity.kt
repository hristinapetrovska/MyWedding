package com.example.mywedding.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_items")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: String = "",

    val title: String,
    val category: String = "Other",

    val amount: Double = 0.0,

    val note: String = ""
)