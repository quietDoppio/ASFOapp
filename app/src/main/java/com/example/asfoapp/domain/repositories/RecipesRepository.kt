package com.example.asfoapp.domain.repositories

import com.example.asfoapp.domain.models.Recipe

interface RecipesRepository {
    suspend fun getFavoritesRecipes(): List<Recipe>
    suspend fun isRecipeFavorite(id: Int): Boolean
    suspend fun setFavoriteState(id: Int, state: Boolean)
    suspend fun getCachedRecipesByCategoryId(id: Int): List<Recipe>
    suspend fun getCachedRecipe(id: Int): Recipe
    suspend fun getRecipesByCategoryId(id: Int): List<Recipe>
    suspend fun getRecipe(id: Int): Recipe
}