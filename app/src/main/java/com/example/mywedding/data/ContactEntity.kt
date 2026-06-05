package com.example.mywedding.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: String = "",

    val name: String,
    val category: String,

    val phone: String = "",

    val note: String = ""
)