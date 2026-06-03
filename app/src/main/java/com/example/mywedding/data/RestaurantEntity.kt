package com.example.mywedding.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: String = "",

    val name: String,
    val address: String,
    val phone: String = "",
    val rating: Double = 0.0,
    val latitude: Double,
    val longitude: Double
)