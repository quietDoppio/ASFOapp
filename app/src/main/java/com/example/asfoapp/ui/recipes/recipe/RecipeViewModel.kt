package com.example.asfoapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.asfoapp.model.Recipe

class RecipeViewModel : ViewModel() {
    data class RecipeState(
        val recipe: Recipe = Recipe(
            0,
            "Рецепт",
            emptyList(),
            emptyList(),
            "burger.png"
        ),
        val favoritesState: Boolean = false,
    )
}