package com.example.mywedding.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GuestDao {

    @Insert
    fun insertGuest(guest: GuestEntity)

    @Update
    fun updateGuest(guest: GuestEntity)

    @Delete
    fun deleteGuest(guest: GuestEntity)

    @Query("SELECT * FROM guests WHERE userId = :userId ORDER BY name ASC")
    fun getAllGuests(userId: String): Flow<List<GuestEntity>>

    @Query("SELECT COUNT(*) FROM guests WHERE userId = :userId")
    fun getGuestsCount(userId: String): Int
}