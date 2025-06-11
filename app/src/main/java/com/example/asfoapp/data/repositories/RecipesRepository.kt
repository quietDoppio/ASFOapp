package com.example.asfoapp.data.repositories

import com.example.asfoapp.data.api.RecipeApiService
import com.example.asfoapp.data.database.RecipeDao
import com.example.asfoapp.di.IoDispatcher
import com.example.asfoapp.model.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipesRepository @Inject constructor(
    private val dao: RecipeDao,
    private val service: RecipeApiService,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getFavoritesRecipes(): List<Recipe> =
        withContext(ioDispatcher) { dao.getFavoritesRecipes() }

    suspend fun isRecipeFavorite(id: Int): Boolean {
        return withContext(ioDispatcher) { dao.isRecipeFavorite(id) }
    }

    suspend fun setFavoriteState(id: Int, state: Boolean) {
        withContext(ioDispatcher) { dao.setFavoriteState(id, state) }
    }

    suspend fun getCachedRecipesByCategoryId(id: Int): List<Recipe> =
        withContext(ioDispatcher) { dao.getRecipesByCategoryId(id) }

    suspend fun getCachedRecipe(id: Int): Recipe {
        return withContext(ioDispatcher) { dao.getRecipeById(id) }
    }

    suspend fun getRecipesByCategoryId(id: Int): List<Recipe> {
        val recipes = withContext(ioDispatcher) { service.getRecipesByCategoryId(id) }
        val updatedRecipes = recipes.map { apiRecipes ->
            val cached = withContext(ioDispatcher) { dao.getRecipeById(apiRecipes.recipeId) }
            apiRecipes.copy(
                categoryId = id,
                isFavorite = cached.isFavorite
            )
        }
        dao.upsertRecipes(updatedRecipes)
        return recipes
    }

    suspend fun getRecipe(id: Int): Recipe {
        val recipe = withContext(ioDispatcher) { service.getRecipeById(id) }
        return recipe
    }
}