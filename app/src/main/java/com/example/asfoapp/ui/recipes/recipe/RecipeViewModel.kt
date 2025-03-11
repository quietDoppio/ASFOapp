package com.example.asfoapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.asfoapp.model.Recipe

class RecipeViewModel : ViewModel() {
    data class RecipeState(
        val recipe: Recipe = Recipe(
            id = 0,
            title = "Рецепт",
            ingredients = emptyList(),
            method = emptyList(),
            imageUrl = "null",
        ),
        val portionsCount: Int = 1,
        val favoritesState: Boolean = false,
    )
}