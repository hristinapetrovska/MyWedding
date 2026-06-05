package com.example.mywedding.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        TaskEntity::class,
        GuestEntity::class,
        BudgetEntity::class,
        RestaurantEntity::class,
        TableEntity::class,
        MemoryEntity::class,
        NoteEntity::class,
        GiftEntity::class,
        PlanEntity::class,
        ContactEntity::class
    ],
    version = 14,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun guestDao(): GuestDao

    abstract fun budgetDao(): BudgetDao

    abstract fun restaurantDao(): RestaurantDao

    abstract fun tableDao(): TableDao

    abstract fun memoryDao(): MemoryDao

    abstract fun noteDao(): NoteDao

    abstract fun giftDao(): GiftDao

    abstract fun planDao(): PlanDao

    abstract fun contactDao(): ContactDao
}