package com.example.mywedding.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {

    @Insert
    fun insertRestaurant(restaurant: RestaurantEntity)

    @Update
    fun updateRestaurant(restaurant: RestaurantEntity)

    @Delete
    fun deleteRestaurant(restaurant: RestaurantEntity)

    @Query("SELECT * FROM restaurants WHERE userId = :userId")
    fun getAllRestaurants(userId: String): Flow<List<RestaurantEntity>>
}