package com.example.asfoapp.data.repositories

import com.example.asfoapp.data.api.RecipeApiService
import com.example.asfoapp.data.database.CategoryDao
import com.example.asfoapp.di.IoDispatcher
import com.example.asfoapp.model.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val dao: CategoryDao,
    private val apiService: RecipeApiService,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getCachedCategories(): List<Category> =
        withContext(ioDispatcher) { dao.getAllCategories() }

    suspend fun getCachedCategoryById(id: Int): Category =
        withContext(ioDispatcher) { dao.getCategoryById(id) }

    suspend fun getCategories(): List<Category> {
        val result = withContext(ioDispatcher) { apiService.getCategories() }
        dao.insertCategories(result)
        return result
    }

    suspend fun getCategoryById(id: Int): Category =
        withContext(ioDispatcher) { apiService.getCategoryById(id) }
}