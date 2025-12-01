package com.example.asfoapp.presentation.screens.recipes

import com.example.asfoapp.domain.models.Recipe

data class RecipesListState(
    val categoryTitle: String = "",
    val recipes: List<Recipe> = emptyList(),
    val apiHeaderImageUrl: String = "",
)