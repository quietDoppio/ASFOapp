package com.example.asfoapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.asfoapp.model.Recipe

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes WHERE is_favorite = 1")
    suspend fun getFavoritesRecipes(): List<Recipe>

    @Query("SELECT EXISTS (SELECT 1 FROM recipes WHERE recipe_id = :id AND is_favorite = 1)")
    suspend fun isRecipeFavorite(id: Int): Boolean

    @Query("UPDATE recipes SET is_favorite = :state WHERE recipe_id = :id")
    suspend fun setFavoriteState(id: Int, state: Boolean)

    @Query("SELECT * FROM recipes WHERE recipe_id = :id")
    suspend fun getRecipeById(id: Int): Recipe

    @Query("SELECT * FROM recipes WHERE category_id = :id")
    suspend fun getRecipesByCategoryId(id: Int): List<Recipe>

    @Upsert
    suspend fun upsertRecipes(recipes: List<Recipe>)
}