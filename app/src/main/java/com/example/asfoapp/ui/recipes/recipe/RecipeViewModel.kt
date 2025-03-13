package com.example.asfoapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.asfoapp.model.Recipe

class RecipeViewModel : ViewModel() {
    private val _recipeState: MutableLiveData<RecipeState> = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState

    init {
        Log.i("!!!", "RecipeViewModel init. recipeState changed")
            _recipeState.value = RecipeState(isFavorite = true)
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val portionsCount: Int = 1,
        val isFavorite: Boolean = false,
    )
}