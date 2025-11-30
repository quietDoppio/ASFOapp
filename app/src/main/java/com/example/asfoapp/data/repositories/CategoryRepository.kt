package com.example.asfoapp.data.repositories

import com.example.asfoapp.data.api.RecipeApiService
import com.example.asfoapp.data.database.dao.CategoryDao
import com.example.asfoapp.data.di.IoDispatcher
import com.example.asfoapp.data.mappers.toDomain
import com.example.asfoapp.data.mappers.toEntity
import com.example.asfoapp.domain.models.Category
import com.example.asfoapp.domain.repositories.CategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoryDao,
    private val apiService: RecipeApiService,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : CategoryRepository {
    override suspend fun getCachedCategories(): List<Category> =
        withContext(ioDispatcher) { dao.getAllCategories().map { it.toDomain() } }

    override suspend fun getCachedCategoryById(id: Int): Category =
        withContext(ioDispatcher) { dao.getCategoryById(id).toDomain() }

    override suspend fun getCategories(): List<Category> =
        withContext(ioDispatcher) {
            val remoteCategories = apiService.getCategories()
            dao.insertCategories(remoteCategories.map { it.toEntity() })
            remoteCategories.map { it.toDomain() }
        }

    override suspend fun getCategoryById(id: Int): Category =
        withContext(ioDispatcher) { apiService.getCategoryById(id).toDomain() }
}
