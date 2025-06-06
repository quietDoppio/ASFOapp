package com.example.asfoapp.data.repositories

import com.example.asfoapp.data.api.RecipeApiService
import com.example.asfoapp.data.database.RecipeDao
import com.example.asfoapp.model.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RecipesRepository(
    private val dao: RecipeDao,
    private val service: RecipeApiService,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun getFavoritesRecipes(): List<Recipe> =
        withContext(dispatcher) { dao.getFavoritesRecipes() }

    suspend fun isRecipeFavorite(id: Int): Boolean {
        return withContext(dispatcher) { dao.isRecipeFavorite(id) }
    }

    suspend fun setFavoriteState(id: Int, state: Boolean) {
        withContext(dispatcher) { dao.setFavoriteState(id, state) }
    }

    suspend fun getCachedRecipesByCategoryId(id: Int): List<Recipe> =
        withContext(dispatcher) { dao.getRecipesByCategoryId(id) }

    suspend fun getCachedRecipe(id: Int): Recipe {
        return withContext(dispatcher) { dao.getRecipeById(id) }
    }

    suspend fun getRecipesByCategoryId(id: Int): List<Recipe> {
        val recipes = withContext(dispatcher) { service.getRecipesByCategoryId(id) }
        val updatedRecipes = recipes.map { apiRecipes ->
            val cached = withContext(dispatcher) { dao.getRecipeById(apiRecipes.recipeId) }
            apiRecipes.copy(
                categoryId = id,
                isFavorite = cached.isFavorite
            )
        }
        dao.upsertRecipes(updatedRecipes)
        return recipes
    }

    suspend fun getRecipe(id: Int): Recipe {
        val recipe = withContext(dispatcher) { service.getRecipeById(id) }
        return recipe
    }
}