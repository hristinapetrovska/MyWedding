package com.example.mywedding.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        TaskEntity::class,
        GuestEntity::class,
        BudgetEntity::class,
        RestaurantEntity::class,
        TableEntity::class
    ],
    version = 10,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun guestDao(): GuestDao

    abstract fun budgetDao(): BudgetDao

    abstract fun restaurantDao(): RestaurantDao

    abstract fun tableDao(): TableDao
}