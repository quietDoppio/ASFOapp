package com.example.asfoapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asfoapp.data.STUB
import com.example.asfoapp.model.Recipe

class RecipeViewModel(private val application: Application) : AndroidViewModel(application) {

    private val _recipeState: MutableLiveData<RecipeState> = MutableLiveData()
    val recipeState: LiveData<RecipeState> get() = _recipeState

    init {
        Log.i("LiveDataViewModel", "RecipeViewModel init. recipeState changed")
        _recipeState.value = RecipeState()
    }

    fun loadRecipe(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        val isFavorite = getFavoritesIds().contains(recipeId.toString())
        _recipeState.value = recipe?.let {
            recipeState.value?.copy(recipe = it, isFavorite = isFavorite)
        }
    }

    private fun getFavoritesIds(): MutableSet<String> {
        val sharedPreferences = application.applicationContext.getSharedPreferences(
            ASFOAPP_PREFS_FILE_KEY,
            Context.MODE_PRIVATE
        )
        return HashSet(
            sharedPreferences?.getStringSet(FAVORITES_PREFS_KEY, emptySet()) ?: emptySet()
        )
    }

    private fun saveFavoritesIds(recipesIdSet: Set<String>) {
        val sharedPreferences = application.applicationContext.getSharedPreferences(
            ASFOAPP_PREFS_FILE_KEY,
            Context.MODE_PRIVATE
        )
        with(sharedPreferences.edit()) {
            putStringSet(FAVORITES_PREFS_KEY, recipesIdSet)
            apply()
        }
    }

    fun toggleFavoriteState() {
        val favoritesIds = getFavoritesIds()
        _recipeState.value?.recipe?.id?.let { id ->
            val isFavorite = favoritesIds.contains(id.toString())
            if (isFavorite) {
                favoritesIds.remove(id.toString())
                saveFavoritesIds(favoritesIds.toSet())
            } else {
                favoritesIds.add(id.toString())
                saveFavoritesIds(favoritesIds)
            }
            _recipeState.value = recipeState.value?.copy(isFavorite = !isFavorite)
        }
    }

    data class RecipeState(
        val recipe: Recipe = Recipe(
            id = 0,
            title = "Рецепт",
            ingredients = emptyList(),
            method = emptyList(),
            imageUrl = "null",
        ),
        val portionsCount: Int = 1,
        val isFavorite: Boolean = false,
    )
}