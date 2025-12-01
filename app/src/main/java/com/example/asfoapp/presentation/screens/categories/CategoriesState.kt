package com.example.asfoapp.presentation.screens.categories

import com.example.asfoapp.domain.models.Category

data class CategoriesState(
    val categoriesList: List<Category> = emptyList(),
)