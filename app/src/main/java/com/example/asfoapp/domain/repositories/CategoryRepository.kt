package com.example.asfoapp.domain.repositories

import com.example.asfoapp.domain.models.Category

interface CategoryRepository {
    suspend fun getCachedCategories(): List<Category>
    suspend fun getCachedCategoryById(id: Int): Category
    suspend fun getCategories(): List<Category>
    suspend fun getCategoryById(id: Int): Category
}