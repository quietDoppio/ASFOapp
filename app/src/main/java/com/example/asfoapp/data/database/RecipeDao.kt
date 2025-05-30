package com.example.asfoapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.asfoapp.model.Recipe

@Dao
interface RecipeDao {
    @Query("SELECT recipe_id FROM recipes WHERE is_favorite = 1")
    suspend fun getFavoritesRecipesIds(): List<Int>

    @Query("SELECT * FROM recipes WHERE is_favorite = 1")
    suspend fun getFavoritesRecipes(): List<Recipe>

    @Query("SELECT EXISTS (SELECT 1 FROM recipes WHERE recipe_id = :id AND is_favorite = 1)")
    suspend fun isRecipeFavorite(id: Int): Boolean

    @Query("UPDATE recipes SET is_favorite = :state WHERE recipe_id = :id")
    suspend fun setFavoriteState(id: Int, state: Boolean)

    @Query("SELECT * FROM recipes WHERE recipe_id = :id")
    suspend fun getRecipesByIds(id: Int): Recipe

    @Query("SELECT * FROM recipes WHERE category_id = :id")
    suspend fun getRecipesByCategoryId(id: Int): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>)
}