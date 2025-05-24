package com.example.asfoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.asfoapp.model.Category

@Database(entities = [Category::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}