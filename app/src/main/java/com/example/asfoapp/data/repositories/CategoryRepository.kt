package com.example.asfoapp.data.repositories

import com.example.asfoapp.data.api.RecipeApiService
import com.example.asfoapp.data.database.CategoryDao
import com.example.asfoapp.model.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CategoryRepository(
    private val dao: CategoryDao,
    private val apiService: RecipeApiService,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun getCachedCategories(): List<Category> =
        withContext(dispatcher) { dao.getAllCategories() }

    suspend fun getCachedCategoryById(id: Int): Category =
        withContext(dispatcher) { dao.getCategoryById(id) }

    suspend fun getCategories(): List<Category> {
        val result = withContext(dispatcher) { apiService.getCategories() }
        dao.insertCategories(result)
        return result
    }

    suspend fun getCategoryById(id: Int): Category =
        withContext(dispatcher) { apiService.getCategoryById(id) }
}