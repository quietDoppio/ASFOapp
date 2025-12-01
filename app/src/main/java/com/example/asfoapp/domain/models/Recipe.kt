package com.example.asfoapp.domain.models

data class Recipe(
    val categoryId: Int? = null,
    val recipeId: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: List<String>,
    val imageUrl: String,
    val isFavorite: Boolean = false,
)
