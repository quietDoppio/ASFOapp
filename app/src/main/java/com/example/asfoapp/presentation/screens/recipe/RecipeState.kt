package com.example.asfoapp.presentation.screens.recipe

import com.example.asfoapp.domain.models.Recipe

data class RecipeState(
    val recipe: Recipe? = null,
    val apiHeaderImageUrl: String = "",
    val portionsCount: Int = 1,
    val isFavorite: Boolean = false,
)