package com.example.asfoapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.asfoapp.model.Ingredient
import com.example.asfoapp.model.Recipe

class RecipeViewModel : ViewModel() {
    data class RecipeState(
        val recipeId: Int = 0,
        val recipeTitle: String = "Рецепт",
        val recipeImageUrl: String? = null,
        val ingredients: List<Ingredient> = emptyList(),
        val method: List<String> = emptyList(),
        val favoritesState: Boolean = false
    )
}