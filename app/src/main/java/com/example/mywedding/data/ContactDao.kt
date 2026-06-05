package com.example.mywedding.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert
    fun insertContact(contact: ContactEntity)

    @Delete
    fun deleteContact(contact: ContactEntity)

    @Query("SELECT * FROM contacts WHERE userId = :userId ORDER BY name ASC")
    fun getAllContacts(userId: String): Flow<List<ContactEntity>>
}