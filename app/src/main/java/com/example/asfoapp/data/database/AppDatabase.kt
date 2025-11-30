package com.example.asfoapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.asfoapp.data.database.dao.CategoryDao
import com.example.asfoapp.data.database.dao.RecipeDao
import com.example.asfoapp.data.database.entities.CategoryEntity
import com.example.asfoapp.data.database.entities.RecipeEntity

@Database(entities = [CategoryEntity::class, RecipeEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                ).build().also { instance ->
                    INSTANCE = instance
                }
            }
    }
}
