package com.example.asfoapp.data.repositories

import com.example.asfoapp.data.api.RecipeApiService
import com.example.asfoapp.data.database.CategoryDao
import com.example.asfoapp.interfaces.SafeRequestExecutorImpl
import com.example.asfoapp.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository(
    private val dao: CategoryDao,
    private val apiService: RecipeApiService,
    private val executor: SafeRequestExecutorImpl
) {
    suspend fun getCategoriesFromCash(): List<Category> =
        withContext(Dispatchers.IO) { dao.getAllCategories() }

    suspend fun getCategoryByIdFromCash(id: Int): Category? =
        withContext(Dispatchers.IO) { dao.getCategoryById(id) }

    suspend fun getCategories(): List<Category> {
        val result = executor.safeExecuteRequest { apiService.getCategories() } ?: emptyList()
        dao.insertCategories(result)
        return result
    }

    suspend fun getCategoryById(id: Int): Category? =
        executor.safeExecuteRequest { apiService.getCategoryById(id) }
}