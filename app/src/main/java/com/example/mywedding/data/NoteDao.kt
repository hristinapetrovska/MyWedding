package com.example.mywedding.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    fun insertNote(note: NoteEntity)

    @Delete
    fun deleteNote(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE userId = :userId ORDER BY id DESC")
    fun getAllNotes(userId: String): Flow<List<NoteEntity>>
}