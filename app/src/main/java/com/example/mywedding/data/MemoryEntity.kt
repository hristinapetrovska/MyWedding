package com.example.mywedding.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: String = "",

    val imageUri: String,

    val caption: String = ""
)