package com.example.asfoapp.data.repositories

import com.example.asfoapp.data.api.RecipeApiService
import com.example.asfoapp.data.database.CategoryDao
import com.example.asfoapp.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository(
    private val dao: CategoryDao,
    private val apiService: RecipeApiService,
) {
    suspend fun getCachedCategories(): List<Category> =
        withContext(Dispatchers.IO) { dao.getAllCategories() }

    suspend fun getCachedCategoryById(id: Int): Category =
        withContext(Dispatchers.IO) { dao.getCategoryById(id) }

    suspend fun getCategories(): List<Category> {
        val result = withContext(Dispatchers.IO) { apiService.getCategories() }
        dao.insertCategories(result)
        return result
    }

    suspend fun getCategoryById(id: Int): Category =
        withContext(Dispatchers.IO) { apiService.getCategoryById(id) }
}