package com.example.mywedding.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: String = "",

    val titleEn: String,
    val titleMk: String,

    val categoryEn: String,
    val categoryMk: String,

    val note: String = "",

    val reminder: String = "",

    val status: String = "TODO"
)