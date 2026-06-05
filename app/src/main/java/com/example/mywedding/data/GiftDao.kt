package com.example.mywedding.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GiftDao {

    @Insert
    fun insertGift(gift: GiftEntity)

    @Update
    fun updateGift(gift: GiftEntity)

    @Delete
    fun deleteGift(gift: GiftEntity)

    @Query("SELECT * FROM gifts WHERE userId = :userId ORDER BY id DESC")
    fun getAllGifts(userId: String): Flow<List<GiftEntity>>
}