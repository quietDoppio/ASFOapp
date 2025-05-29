package com.example.asfoapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.asfoapp.model.Recipe

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>)

    @Query("SELECT * FROM recipes WHERE recipe_id = :id")
    suspend fun getRecipesByIds(id: Int): Recipe

    @Query("SELECT * FROM recipes WHERE recipe_id IN (:ids)")
    suspend fun getRecipesByIds(ids: List<Int>): List<Recipe>

    @Query("SELECT * FROM recipes WHERE category_id = :id")
    suspend fun getRecipesByCategoryId(id: Int): List<Recipe>
}