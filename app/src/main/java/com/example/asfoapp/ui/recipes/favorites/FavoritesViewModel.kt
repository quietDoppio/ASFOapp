package com.example.asfoapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asfoapp.data.RecipeRepository
import com.example.asfoapp.model.Recipe
import com.example.asfoapp.ui.categories.TAG
import com.example.asfoapp.ui.recipes.recipe.ASFOAPP_PREFS_FILE_KEY
import com.example.asfoapp.ui.recipes.recipe.FAVORITES_PREFS_KEY

class FavoritesViewModel(private val application: Application) : AndroidViewModel(application) {
    init {
        Log.i(TAG, "FavoritesViewModel is created")
    }
    private val _favoritesState: MutableLiveData<FavoritesState> = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState

    fun loadRecipes(){
        _favoritesState.value = favoritesState.value?.copy(favoritesRecipes = getFavoriteRecipes())
    }

    private fun getFavoritesIds(): MutableSet<String> {
        val sharedPrefs = application.applicationContext.getSharedPreferences(
            ASFOAPP_PREFS_FILE_KEY,
            Context.MODE_PRIVATE
        )
        return HashSet(sharedPrefs?.getStringSet(FAVORITES_PREFS_KEY, emptySet()) ?: emptySet())
    }

    private fun getFavoriteRecipes(): List<Recipe>? {
        return RecipeRepository.getRecipes(getFavoritesIds().toList())
    }

    data class FavoritesState(
        val favoritesRecipes: List<Recipe>? = emptyList()
    )
}