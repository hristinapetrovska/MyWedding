package com.example.mywedding.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plans")
data class PlanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: String = "",

    val time: String,
    val title: String,
    val location: String = "",
    val note: String = ""
)