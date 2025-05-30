package com.example.asfoapp.data.repositories

import com.example.asfoapp.data.api.RecipeApiService
import com.example.asfoapp.data.database.RecipeDao
import com.example.asfoapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipesRepository(
    private val dao: RecipeDao,
    private val service: RecipeApiService,
) {
    suspend fun getFavoritesRecipes(): List<Recipe> =
        withContext(Dispatchers.IO) { dao.getFavoritesRecipes() }

    suspend fun isRecipeFavorite(id: Int): Boolean {
        return withContext(Dispatchers.IO) { dao.isRecipeFavorite(id) }
    }

    suspend fun setFavoriteState(id: Int, state: Boolean) {
        withContext(Dispatchers.IO) { dao.setFavoriteState(id, state) }
    }

    suspend fun getCachedRecipesByCategoryId(id: Int): List<Recipe> =
        withContext(Dispatchers.IO) { dao.getRecipesByCategoryId(id) }

    suspend fun getCachedRecipe(id: Int): Recipe {
        return withContext(Dispatchers.IO) { dao.getRecipesByIds(id) }
    }

    suspend fun getRecipesByCategoryId(id: Int): List<Recipe> {
        val recipes = withContext(Dispatchers.IO) { service.getRecipesByCategoryId(id) }
        val updatedRecipes = recipes.map { it.copy(categoryId = id) }
        dao.insertRecipes(updatedRecipes)
        return recipes
    }

    suspend fun getRecipe(id: Int): Recipe {
        val recipe = withContext(Dispatchers.IO) { service.getRecipeById(id) }
        return recipe
    }
}