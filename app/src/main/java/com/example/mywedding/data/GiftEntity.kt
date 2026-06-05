package com.example.mywedding.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gifts")
data class GiftEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: String = "",

    val giftName: String,
    val fromPerson: String = "",
    val note: String = ""
)