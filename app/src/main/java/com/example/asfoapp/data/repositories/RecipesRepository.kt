package com.example.asfoapp.data.repositories

import com.example.asfoapp.data.api.RecipeApiService
import com.example.asfoapp.data.database.dao.RecipeDao
import com.example.asfoapp.data.di.IoDispatcher
import com.example.asfoapp.data.mappers.toDomain
import com.example.asfoapp.data.mappers.toEntity
import com.example.asfoapp.domain.models.Recipe
import com.example.asfoapp.domain.repositories.RecipesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
    private val dao: RecipeDao,
    private val service: RecipeApiService,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : RecipesRepository {
    override suspend fun getFavoritesRecipes(): List<Recipe> =
        withContext(ioDispatcher) { dao.getFavoritesRecipes().map { it.toDomain() } }

    override suspend fun isRecipeFavorite(id: Int): Boolean =
        withContext(ioDispatcher) { dao.isRecipeFavorite(id) }

    override suspend fun setFavoriteState(id: Int, state: Boolean) {
        withContext(ioDispatcher) { dao.setFavoriteState(id, state) }
    }

    override suspend fun getCachedRecipesByCategoryId(id: Int): List<Recipe> =
        withContext(ioDispatcher) { dao.getRecipesByCategoryId(id).map { it.toDomain() } }

    override suspend fun getCachedRecipe(id: Int): Recipe =
        withContext(ioDispatcher) { dao.getRecipeById(id).toDomain() }

    override suspend fun getRecipesByCategoryId(id: Int): List<Recipe> =
        withContext(ioDispatcher) {
            val cachedById = dao.getRecipesByCategoryId(id).associateBy { it.recipeId }
            val remoteRecipes = service.getRecipesByCategoryId(id)
            val entities = remoteRecipes.map { dto ->
                val cachedFavorite = cachedById[dto.id]?.isFavorite ?: false
                dto.toEntity(isFavorite = cachedFavorite, categoryIdOverride = id)
            }
            dao.upsertRecipes(entities)
            entities.map { it.toDomain() }
        }

    override suspend fun getRecipe(id: Int): Recipe =
        withContext(ioDispatcher) {
            val cached = runCatching { dao.getRecipeById(id) }.getOrNull()
            val remote = service.getRecipeById(id)
            val entity = remote.toEntity(isFavorite = cached?.isFavorite ?: false)
            dao.upsertRecipes(listOf(entity))
            entity.toDomain()
        }
}
