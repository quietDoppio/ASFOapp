package com.example.asfoapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.asfoapp.model.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<Category>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): Category

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categoryList: List<Category>)
}