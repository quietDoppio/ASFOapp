package com.example.asfoapp.presentation.screens.favorites

import com.example.asfoapp.domain.models.Recipe

data class FavoritesState(
    val favoritesRecipes: List<Recipe> = emptyList()
)